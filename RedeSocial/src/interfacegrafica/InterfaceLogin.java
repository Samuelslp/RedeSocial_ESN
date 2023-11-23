package interfacegrafica;


import conexao.ConexaoUser;
import classes.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceLogin extends JFrame {
    private JPanel painelLogin = new JPanel();
    private JTextField campoUsername = new JTextField(15);
    private JPasswordField campoSenha = new JPasswordField(15);
    private JButton botaoLogin = new JButton("Entrar");
    private JButton botaoCriarConta = new JButton("Criar Conta");

    public InterfaceLogin() {
        this.setTitle("ESN");
        this.setSize(1000, 800);
        painelLogin.setLayout(new GridBagLayout());
        painelLogin.setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel tituloLabel = new JLabel("ESN");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24));
        tituloLabel.setForeground(Color.DARK_GRAY);
        painelLogin.add(tituloLabel, gbc);

        adicionarCampo("Username:", campoUsername);
        adicionarCampo("Senha:", campoSenha);

        painelLogin.add(botaoLogin, gbc);
        painelLogin.add(botaoCriarConta, gbc);

        this.getContentPane().add(painelLogin);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        // ACTIONLISTENER LOGIN
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        // ACTIONLISTENER  CRIAR CONTA
        botaoCriarConta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInterfaceCriarConta();
            }
        });
    }

    private void realizarLogin() {
        String email = campoUsername.getText();
        String senha = new String(campoSenha.getPassword());
        //System.out.println(senha);

        Usuario usuarioLogado = obterUsuarioDoBanco(email, senha);
        if (usuarioLogado != null) {
            new InterfaceUsuario(usuarioLogado);
            // Feche a tela de login após o login bem-sucedido
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login falhou. Verifique seu email e senha e tente novamente.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Usuario obterUsuarioDoBanco(String username, String senha) {
        ConexaoUser conexaoUser = new ConexaoUser();
        return conexaoUser.fazerLogin(username, senha);
    }

    private void abrirInterfaceCriarConta() {
        new InterfaceCriarConta(this); // VOLTA INTERFACE LOGIN SE NECESSARIO
        setVisible(false); // Esconde a InterfaceLogin
    }

    private void adicionarCampo(String rotulo, JTextField campo) {
        JLabel label = new JLabel(rotulo);
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);

        painelLogin.add(label, gbc);
        painelLogin.add(campo, gbc);
    }

    public static void main(String[] args) {
        new InterfaceLogin();
    }
}
