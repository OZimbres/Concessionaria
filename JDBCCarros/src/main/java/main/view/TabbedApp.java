package main.view;

import javax.swing.JTabbedPane;

public class TabbedApp extends JTabbedPane {
    PainelCarros painelCarros = new PainelCarros();
    PainelPessoas painelPessoas = new PainelPessoas();
    PainelVendas painelVendas = new PainelVendas();

    public TabbedApp() {
        super();
        this.add("Carros", painelCarros);
        this.add("Pessoas", painelPessoas);
        this.add("Venda", painelVendas);
    }
}
