package interfacegrafica;

import conexao.ConexaoMsg;
import conexao.ConexaoUser;
import classes.Mensagem;
import classes.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InterfaceUsuario extends JFrame {
    private JPanel painel = new JPanel();
    private JLabel tituloLabel = new JLabel("ESN");
    private JButton jButtonIncluirAmigo = new JButton("Adicionar Amigo");
    private JButton jButtonConsultarAmigos = new JButton("Consultar Amigos");
    private JButton jButtonExcluirAmigo = new JButton("Excluir Amigo");
    private JButton jButtonEnviarMsg = new JButton("Enviar Mensagem");
    private JButton jButtonVisualizarMsg = new JButton("Visualizar Mensagem");
    private JButton jButtonSair = new JButton("Sair");

    // REFERENCIA AO USUARIO
    private Usuario usuario;

    public InterfaceUsuario(Usuario usuario) {

        this.usuario = usuario;
        this.tituloLabel.setText(usuario.getNome());
        this.setTitle("ESN");
        this.setSize(440, 440);
        painel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));
        painel.setBackground(new Color(173, 216, 230));
        padronizarBotoes();
        this.getContentPane().add(painel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        ConexaoUser conexaoUser = new ConexaoUser();
        this.usuario.setAmigos(conexaoUser.receberAmigos(usuario.getUsername()));
        ConexaoMsg conexaoMsg = new ConexaoMsg();
        conexaoMsg.criarTabela(this.usuario.getUsername());

        // ADD ACTIONLISTENER AOS BOTOES

        jButtonIncluirAmigo.addActionListener(new IncluirAmigoActionListener());
        jButtonConsultarAmigos.addActionListener(new ConsultarAmigosActionListener());
        jButtonExcluirAmigo.addActionListener(new ExcluirAmigoActionListener());
        jButtonEnviarMsg.addActionListener(new EnviarMensagemActionListener());
        jButtonSair.addActionListener(new SairActionListener());
    }

    private void padronizarBotoes() {
        Dimension tamanhoPadrao = new Dimension(150, 30);
        tituloLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        painel.add(tituloLabel);

        jButtonIncluirAmigo.setPreferredSize(tamanhoPadrao);
        jButtonConsultarAmigos.setPreferredSize(tamanhoPadrao);
        jButtonExcluirAmigo.setPreferredSize(tamanhoPadrao);
        jButtonEnviarMsg.setPreferredSize(tamanhoPadrao);
        jButtonVisualizarMsg.setPreferredSize(tamanhoPadrao);
        jButtonSair.setPreferredSize(tamanhoPadrao);

        painel.add(jButtonIncluirAmigo);
        painel.add(jButtonConsultarAmigos);
        painel.add(jButtonExcluirAmigo);
        painel.add(jButtonEnviarMsg);
        painel.add(jButtonVisualizarMsg);
        painel.add(jButtonSair);

        // ActionListener (mensagem de retorno)  Visualizar Mensagem

        // formatação do texto usando algumas tags HTML básicas(negrito quebra de linha etc)
        jButtonVisualizarMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConexaoMsg conexaoMsg = new ConexaoMsg();
                ArrayList<Mensagem> mensagens = conexaoMsg.receberMensagens(usuario.getUsername());
                StringBuilder listaMensagens = new StringBuilder("<html><b>Mensagens:</b><br>");
                for (Mensagem mensagem : mensagens) {
                    listaMensagens.append("<br><b>Remetente:</b> ").append(mensagem.getRemetente());
                    listaMensagens.append("<br><b>Mensagem:</b> ").append(mensagem.getMensagem());
                    listaMensagens.append("<br><b>Data:</b> ").append(mensagem.getData()).append("<br>");
                }

                //BOTAO VOLTAR
                JButton botaoVoltar = new JButton("Voltar");

                JPanel panelMensagens = new JPanel();
                panelMensagens.setLayout(new BorderLayout());
                JLabel labelMensagens = new JLabel(listaMensagens.toString());
                JScrollPane scrollPane = new JScrollPane(labelMensagens);
                panelMensagens.add(scrollPane, BorderLayout.CENTER);
                panelMensagens.add(botaoVoltar, BorderLayout.SOUTH);

                // JANELA QUE EXIBE MENSAGEM
                JFrame frameMensagens = new JFrame("Mensagens");
                frameMensagens.setSize(360, 305);
                frameMensagens.setLocationRelativeTo(null);
                frameMensagens.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameMensagens.add(panelMensagens);
                frameMensagens.setVisible(true);

                botaoVoltar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frameMensagens.dispose(); // FECHANDO A JANELA MSG
                    }
                });
            }
        });
    }

    class IncluirAmigoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nomeAmigo = JOptionPane.showInputDialog(null, "Digite o usuário do amigo que deseja adicionar:");
            ConexaoUser conexaoUser = new ConexaoUser();
            if (conexaoUser.verificaSeExisteUsuario(nomeAmigo)) {
                usuario.adicionarAmigo(nomeAmigo);
                JOptionPane.showMessageDialog(null, "Amigo incluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Usuario nao encontrado!");
            }
        }
    }

    class ConsultarAmigosActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> amigos = usuario.getAmigos();
            StringBuilder listaAmigos = new StringBuilder("Amigos:\n");
            for (String amigo : amigos) {
                listaAmigos.append(amigo).append("\n");
            }
            JOptionPane.showMessageDialog(null, listaAmigos.toString(), "Lista de Amigos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class ExcluirAmigoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nomeAmigo = JOptionPane.showInputDialog(null, "Digite o usuario do amigo a ser excluído:");
            if (usuario.removerAmigo(nomeAmigo)) {
                JOptionPane.showMessageDialog(null, "Amigo excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Amigo não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class EnviarMensagemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String destinatario = JOptionPane.showInputDialog(null, "Digite o nome do destinatário:");
            String mensagem = JOptionPane.showInputDialog(null, "Digite sua mensagem:");
            Mensagem novaMensagem = new Mensagem(1, usuario.getUsername(), "data", "horario", mensagem);
            if (usuario.enviarMensagem(destinatario, mensagem))
                JOptionPane.showMessageDialog(null, "Mensagem enviada com sucesso para " + destinatario + "!");
            else
                JOptionPane.showMessageDialog(null, "O usuário " + destinatario + " não é seu amigo!");
        }
    }

    class SairActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose(); // FECHA JANELA AO CLICAR SAIR
            abrirInterfaceLogin(); //
        }

        private void abrirInterfaceLogin() {

            JFrame frame = InterfaceUsuario.this;

            //NOVA INSTANCIA INTERFACE LOGIN
            InterfaceLogin interfaceLogin = new InterfaceLogin();


            interfaceLogin.setVisible(true);

            // FECHA JANELA
            frame.dispose();
        }
    }
}

