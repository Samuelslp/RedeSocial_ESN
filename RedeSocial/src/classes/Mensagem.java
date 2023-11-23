package classes;


public class Mensagem {
    private int id;
    private String remetente;
    private String data;
    private String horario;
    private String mensagem;

    public Mensagem(int id, String remetente, String data, String horario, String mensagem) {
        this.id = id;
        this.remetente = remetente;
        this.data = data;
        this.horario = horario;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
