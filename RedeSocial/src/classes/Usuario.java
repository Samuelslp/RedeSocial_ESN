package classes;


import conexao.ConexaoMsg;
import conexao.ConexaoUser;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nome;
    private String username;
    private String email;
    private String password;

    private ArrayList<String> amigos;
    private ArrayList<Mensagem> mensagens; //adiciona a lsta mensagem

    public Usuario(int id, String nome, String username, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.username = username;
        this.amigos = new ArrayList<>();
        this.mensagens = new ArrayList<>();
    }

    public void adicionarAmigo(String amigo) {
        ConexaoUser conexaoUser = new ConexaoUser();
        conexaoUser.adicionarAmizade(username, amigo);
        this.amigos.add(amigo);
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public boolean removerAmigo(String nomeAmigo) {
        ConexaoUser conexaoUser = new ConexaoUser();
        conexaoUser.removerAmizade(username, nomeAmigo);
        return amigos.remove(nomeAmigo);
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void adicionarMensagem(Mensagem mensagem) {
        this.mensagens.add(mensagem);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean enviarMensagem(String destinario, String msg){
        ConexaoMsg conexaoMsg = new ConexaoMsg();
        if(amigos.contains(destinario)){//Sao amigos
            // System.out.println("São amigos.");
            return conexaoMsg.enviarMensagem(username, destinario, msg);
        }
        else {
            System.out.println("O usuario nao é seu amigo.");
            return false;
        }
    }
}
