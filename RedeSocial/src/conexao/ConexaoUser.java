package conexao;

import classes.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class ConexaoUser {
    private final String user = "postgres";
    private final String password = "123456";
    Connection conn = null;

    private static final String urlDB_USER = "jdbc:postgresql://localhost/DBRS_USER";
    private static String QUERY_SELECT_USER = "select * from usuario where username =?";
    private static final String SELECT_ALL_QUERY = "select * from usuario";
    public ConexaoUser() {
    }
    public Connection connect(String urlDB) {

        try {
            conn = DriverManager.getConnection(urlDB, user, password);

            if (conn != null) {
                //System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make connection!");
            }
            //versão do postgreeSQL
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT VERSION()");
            if (resultSet.next()) {
                //System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return conn;
        //conn.close();
    }
    public void criarConta(String nome, String email, String password, String username){
        connect(urlDB_USER);
        if(inserirDadosConta(nome, email, username, password))
            System.out.println("Conta criada com sucesso");
        else
            System.out.println("Erro ao criar a conta");
        encerrarConexao();
    }
    public void adicionarAmizade(String nome1, String nome2){
        connect(urlDB_USER);
        if(inserirAmizade(nome1, nome2))
            System.out.println("Amigo adicionado.");
        else
            System.out.println("Erro ao adicionar.");
        encerrarConexao();
    }
    public ArrayList<String> receberAmigos(String usuario){
        connect(urlDB_USER);
        ArrayList<String> amigos = getAllAmigos(usuario);
        encerrarConexao();
        return amigos;
    }
    public void removerAmizade(String usuario, String amigo){
        connect(urlDB_USER);
        removerAmigo(usuario, amigo);
        encerrarConexao();
    }

    public Usuario fazerLogin(String usuario, String pass){
        connect(urlDB_USER);
        Usuario user = getUserByName(QUERY_SELECT_USER, usuario);
        encerrarConexao();
        if (user == null)
            return null;
        if(user.getPassword().equals(pass))
            return user;
        else
            return null;
    }

    public boolean verificaSeExisteUsuario(String nome){
        connect(urlDB_USER);
        ArrayList<Usuario> usuarios = getAllUsers(SELECT_ALL_QUERY);
        encerrarConexao();
        for(Usuario x : usuarios){
            if(nome.equals(x.getUsername()))
                return true;
        }
        return false;
    }

    public void encerrarConexao(){
        try {
            conn.close();
        }catch (SQLException e){
            printSQLException(e);
        }
    }
    public ArrayList<Usuario> getAllUsers(String query) {
        ArrayList<Usuario> retorno = new ArrayList<>();

        try {

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String pass = rs.getString("senha");
                Usuario usuario = new Usuario(id, nome, username, email, pass);
                retorno.add(usuario);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return retorno;
    }

    public Usuario getUserByName(String query, String valor) {

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, valor);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String pass = rs.getString("senha");
                Usuario usuario = new Usuario(id, nome, username, email, pass);
                return usuario;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return null;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
    public boolean inserirDadosConta(String nome, String email, String username, String pass) {
        PreparedStatement preparedStatement = null;

        try {
            // Query SQL para a inserção
            String sql = "INSERT INTO usuario (nome, email, senha, username) VALUES (?, ?, ?, ?)";

            preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, pass);
            preparedStatement.setString(4, username);

            // Executar a instrução SQL
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean inserirAmizade(String nome1, String nome2) {
        PreparedStatement preparedStatement = null;

        try {
            // Query SQL para a inserção
            String sql = "INSERT INTO amizade (username1, username2) VALUES (?, ?)";

            // instrução preparada
            preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, nome1);
            preparedStatement.setString(2, nome2);

            // Executar  SQL
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<String> getAllAmigos(String usuario) {
        String query = "select * from Amizade;";
        ArrayList<String> retorno = new ArrayList<>();

        try {

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_amizade");
                String username1 = rs.getString("username1");
                String username2 = rs.getString("username2");
                if(username1.equals(usuario))
                    retorno.add(username2);
                else if(username2.equals(usuario))
                    retorno.add(username1);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return retorno;
    }
    public boolean removerAmigo(String usuario, String amigo){
        PreparedStatement preparedStatement = null;
        try{

            String query1 = "DELETE FROM Amizade WHERE username1 = ? AND username2 = ?";
            String query2 = "DELETE FROM Amizade WHERE username2 = ? AND username1 = ?";


            preparedStatement = conn.prepareStatement(query1);


            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, amigo);

            // Executar a instrução SQL
            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas > 0) {
                return true;
            } else {

                preparedStatement = conn.prepareStatement(query2);


                preparedStatement.setString(1, amigo);
                preparedStatement.setString(2, usuario);

                // Executar a instrução SQL
                linhasAfetadas = preparedStatement.executeUpdate();

                if (linhasAfetadas > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}