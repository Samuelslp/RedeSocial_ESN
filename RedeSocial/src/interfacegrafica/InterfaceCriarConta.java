package interfacegrafica;

import conexao.ConexaoUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceCriarConta extends JFrame {
    private JPanel painelCriarConta = new JPanel();
    private JTextField campoNome = new JTextField(15);
    private JTextField campoUsername = new JTextField(15);
    private JTextField campoEmailCriar = new JTextField(15);
    private JPasswordField campoSenhaCriar = new JPasswordField(15);
    private JButton botaoCriarContaCriar = new JButton("Criar Conta");
    private JButton botaoVoltar = new JButton("Voltar");

    private InterfaceLogin interfaceLogin; // Referência à InterfaceLogin

    public InterfaceCriarConta(InterfaceLogin interfaceLogin) {
        this.interfaceLogin = interfaceLogin; // Armazena a referência

        this.setTitle("Criar Conta");
        this.setSize(1200, 800);
        painelCriarConta.setLayout(new GridBagLayout());
        painelCriarConta.setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel tituloLabel = new JLabel("ESN");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24));
        tituloLabel.setForeground(Color.DARK_GRAY);
        painelCriarConta.add(tituloLabel, gbc);

        adicionarCampo("Nome:", campoNome);
        adicionarCampo("Username:", campoUsername);
        adicionarCampo("Email:", campoEmailCriar);
        adicionarCampo("Senha:", campoSenhaCriar);

        painelCriarConta.add(botaoCriarContaCriar, gbc);
        painelCriarConta.add(botaoVoltar, gbc);

        this.getContentPane().add(painelCriarConta);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        botaoCriarContaCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarConta();
            }
        });

        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarParaLogin();
            }
        });
    }

    private void criarConta() {
        String nome = campoNome.getText();
        String email = campoEmailCriar.getText();
        String senha = new String(campoSenhaCriar.getPassword());
        String username = campoUsername.getText();
        System.out.println(senha);
        ConexaoUser conexaoUser = new ConexaoUser();
        conexaoUser.criarConta(nome, email, senha, username);

        // Após criar a conta com sucesso, redireciona para InterfaceLogin
        JOptionPane.showMessageDialog(this, "Conta criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        abrirInterfaceLogin();
    }

    private void voltarParaLogin() {
        // Exibe a InterfaceLogin ao clicar em Voltar
        interfaceLogin.setVisible(true);
        this.dispose(); // Fecha a janela de criar conta
    }

    private void adicionarCampo(String rotulo, JTextField campo) {
        JLabel label = new JLabel(rotulo);
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);

        painelCriarConta.add(label, gbc);
        painelCriarConta.add(campo, gbc);
    }

    private void abrirInterfaceLogin() {
        // Abre a InterfaceLogin e fecha a janela
        interfaceLogin.setVisible(true);
        this.dispose();
    }
}
