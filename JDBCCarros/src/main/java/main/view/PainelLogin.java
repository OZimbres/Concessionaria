package main.view;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.control.LoginControl;
import main.model.Login;

public class PainelLogin extends JFrame {
    //-----===| ATRIBUTOS |===-----//
    // Criando componentes
    private JPanel mainPanel = new JPanel();

    //---=| Login |=---//
    // Título Login
    private JLabel tituloLogin = new JLabel("LOGIN");

    // User CPF
    private JLabel cpfLabel = new JLabel("Insira seu CPF:");
    private JTextField cpfInput = new JTextField(20);

    // User Senha
    private JLabel senhaLabel = new JLabel("Insira sua Senha:");
    private JTextField senhaInput = new JTextField(20);
    // Botão Entrar/Criar
    private JButton botaoAcessar = new JButton("Acessar");
    
    private LoginControl loginControl = new LoginControl();

    // ArrayList dos componentes a serem exibidos
    ArrayList<JComponent> componentes = new ArrayList<JComponent>() {
        {
            // Títulos
            add(tituloLogin); // Título da página de login
            // Componetes da página de login
            add(cpfLabel); // label CPF
            add(cpfInput); // input CPF
            add(senhaLabel); // label Senha
            add(senhaInput); // input Senha
            add(botaoAcessar); // Botão para efetuar o login/cadastro
        }
    };
    
    Insets insets; // Chamando insets para estlização do GridBagLayout

    //-----===| CONSTRUTOR |===-----//
    public PainelLogin() {
        super();
        // Adicionando mainPanel ao JFrame
        this.add(mainPanel);
        // Setando layout
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Criando as telas
        JPanel login = criarLogin();

        //Adicionando tela ao Painel de telas
        mainPanel.add(login);
    }

    //-----===| MÉTODOS |===-----//
    //---=| Página Login |=---//
    private JPanel criarLogin(){
        JPanel telaLogin = new JPanel();
        // Setando Layout
        telaLogin.setLayout(new GridBagLayout());
        // Setando Background
        // telaLogin.setBackground(Color.DARK_GRAY);

        // Configurando Painel
        GridBagConstraints elemento = new GridBagConstraints(); // Váriavel de cntrole de exibição de cada elemento

        // Declarando os itens a serem utilizados (sua posição no ArrayList)
        int[] posicaoNoArray = {
            0, // Título
            1, // CPF
            2, // CPF
            3, // Senha
            4, // Senha
            5 // Botão
        };
        // Declarando valores de cada item
        int[][] posicaoComponentes = {
            { 0, 0, 2, 1, 2, 1, 3, 3, 3, 3 }, // Título login
            { 0, 3, 2, 1, 2, 1, 3, 3, 3, 3 }, // cpf label
            { 0, 4, 2, 1, 2, 1, 3, 3, 3, 3 }, // cpf input
            { 0, 5, 2, 1, 2, 1, 3, 3, 3, 3 }, // senha label
            { 0, 6, 2, 1, 2, 1, 3, 3, 3, 3 }, // senha input
            { 0, 7, 1, 1, 1, 1, 3, 3, 3, 3 }, // botão trocar
            { 1, 7, 1, 1, 1, 1, 3, 3, 3, 3 }, // botão acessar
        };

        // Configurand modelo de exibição
        for(int i = 0; i < 6; i++){ // Menor que 7 pois 7 é a quantidade de item da janela
            elemento.gridx = posicaoComponentes[i][0];
            elemento.gridy = posicaoComponentes[i][1];
            elemento.gridwidth = posicaoComponentes[i][2];
            elemento.gridheight = posicaoComponentes[i][3];
            elemento.weightx = posicaoComponentes[i][4];
            elemento.weighty = posicaoComponentes[i][5];
            elemento.insets = new Insets(posicaoComponentes[i][6], posicaoComponentes[i][7], posicaoComponentes[i][8], posicaoComponentes[i][9]);

            telaLogin.add(componentes.get(posicaoNoArray[i]), elemento);
        }

        // Handler para acessar
        botaoAcessar.addActionListener(e -> { // Listener do botão
            try {
                if(loginControl.checkLogin(cpfInput.getText(), senhaInput.getText())){
                    Login logado = new Login(Long.valueOf(cpfInput.getText()));
                    dispose();
                    new FrameApp(logado);
                    JOptionPane.showMessageDialog(null, "Login realizado com sucesso");
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        return telaLogin;
    }

    //---=| Rodar Aplicação |=---//
    public void run(){
        //---=| Setando o Frame |=---//
        this.setTitle("CONSCESSIONÁRIA ASJ");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
    }
}
