package main.view;

import javax.swing.JTabbedPane;

import main.model.Login;

public class TabbedApp extends JTabbedPane {
    PainelCarros painelCarros;
    PainelPessoas painelPessoas = new PainelPessoas();
    PainelVendas painelVendas;

    public TabbedApp(Login logado) {
        super();
        painelCarros = new PainelCarros(logado);
        painelVendas = new PainelVendas(logado);
        this.add("Carros", painelCarros);
        this.add("Pessoas", painelPessoas);
        this.add("Venda", painelVendas);

        this.addChangeListener(e -> {
            painelVendas.atualizarClientesComboBox();
            painelVendas.atualizarCarrosComboBox();
            painelCarros.atualizarPainelCarros();
        });
    }
}
