package conexao;

import classes.Mensagem;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class ConexaoMsg {
    private final String user = "postgres";
    private final String password = "123456";
    Connection conn = null;
    private static final String urlDB_MSG = "jdbc:postgresql://localhost/DBRS_MSG";

    private static final String select_msg = "select * from ";
    public ConexaoMsg(){}
    public Connection connect(String urlDB) {

        try {
            conn = DriverManager.getConnection(urlDB, user, password);

            if (conn != null) {
                //System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                //System.out.println("Failed to make connection!");
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
    public boolean enviarMensagem(String remetente, String destinatario, String msg) {
        connect(urlDB_MSG);

        LocalDateTime agora = LocalDateTime.now();

        // Formatar a data e hora como strings separadas
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

        String dia = agora.format(formatterData);
        String hora = agora.format(formatterHora);
        if (inserirMensagem(remetente, destinatario, msg, dia, hora)) {
            encerrarConexao();
            return true;
        }
        else{
            encerrarConexao();
            return false;
        }
    }
    public Connection criarTabelaMsg(String urlDB, String usuario) {

        try {
            conn = DriverManager.getConnection(urlDB, user, password);

            if (conn != null) {
                // Create a table para cada usario no banco msg
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + usuario + " (id_msg SERIAL PRIMARY KEY, " +
                        "remetente VARCHAR(255) NOT NULL," +
                        "mensagem TEXT NOT NULL," +
                        "dia VARCHAR(20) NOT NULL," +
                        "hora VARCHAR(12) NOT NULL);";
                try (PreparedStatement preparedStatement = conn.prepareStatement(createTableSQL)) {
                    preparedStatement.execute();
                }
                //System.out.println("Connected to the PostgreSQL server successfully." + usuario);
            } else {
                //System.out.println("Failed to make connection!");
            }
            //versão do postgreeSQL
            // Get PostgreSQL version
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT VERSION()")) {
                if (resultSet.next()) {
                    System.out.println("PostgreSQL Version: " + resultSet.getString(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return conn;
        //conn.close();
    }
    public ArrayList<Mensagem> receberMensagens(String usuario){

        connect(urlDB_MSG);

        ArrayList<Mensagem> ret = getAllMsgs(select_msg + usuario + ";");

        encerrarConexao();

        return ret;
    }
    public boolean inserirMensagem(String remetente, String dest, String msg, String dia, String hora){
        PreparedStatement preparedStatement = null;
        try {
            // Query SQL para a inserção
            String sql = "INSERT INTO " + dest + " (remetente, mensagem, dia, hora) VALUES (?, ?, ?, ?)";


            preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setString(1, remetente);
            preparedStatement.setString(2, msg);
            preparedStatement.setString(3, dia);
            preparedStatement.setString(4, hora);

            // Executar a instrução SQL
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void criarTabela(String user){

        criarTabelaMsg(urlDB_MSG, user);

        encerrarConexao();

    }
    public ArrayList<Mensagem> getAllMsgs(String query) {
        ArrayList<Mensagem> retorno = new ArrayList<>();

        try {

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            System.out.println(preparedStatement);
           // Executa query
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_msg");
                String mensagem = rs.getString("mensagem");
                String remetente = rs.getString("remetente");
                String data = rs.getString("dia");
                String horario = rs.getString("hora");
                Mensagem msg = new Mensagem(id, remetente, data, horario, mensagem);
                retorno.add(msg);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return retorno;
    }
    public void encerrarConexao(){
        try {
            conn.close();
        }catch (SQLException e){
            printSQLException(e);
        }
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
}