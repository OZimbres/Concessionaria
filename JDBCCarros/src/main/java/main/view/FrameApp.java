package main.view;

import javax.swing.JFrame;

import main.model.Login;

public class FrameApp extends JFrame {
    public FrameApp(Login logado) {
        super("Concessionária ASJ");

        this.add(new TabbedApp(logado));

        //Setando janela
        this.setBounds(550, 250, 1350, 500);
        this.setDefaultCloseOperation(2);
        this.setVisible(true);
    }
}
