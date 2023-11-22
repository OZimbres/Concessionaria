package main.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import main.control.CarrosControl;
import main.control.CarrosDAO;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import main.model.Carro;

public class PainelCarros extends JPanel {
    // Atributos(componentes)
    private JButton cadastrar, apagar, editar;
    private JTextField carPlacaField, carAnoField, carMarcaField, carModeloField, carCorField, carPrecoField;
    private List<Carro> carros;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    private CarrosControl carrosControl;

    // Construtor(GUI-JPanel)
    public PainelCarros() {
        super();

        // entrada de dados
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Cadastro Carros"));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));

        inputPanel.add(new JLabel("Placa"));
        carPlacaField = new JTextField(20);
        inputPanel.add(carPlacaField);

        inputPanel.add(new JLabel("Ano"));
        carAnoField = new JTextField(20);
        inputPanel.add(carAnoField);

        inputPanel.add(new JLabel("Marca"));
        carMarcaField = new JTextField(20);
        inputPanel.add(carMarcaField);

        inputPanel.add(new JLabel("Modelo"));
        carModeloField = new JTextField(20);
        inputPanel.add(carModeloField);

        inputPanel.add(new JLabel("Cor"));
        carCorField = new JTextField(20);
        inputPanel.add(carCorField);

        inputPanel.add(new JLabel("Preço"));
        carPrecoField = new JTextField(20);
        inputPanel.add(carPrecoField);

        this.add(inputPanel);

        JPanel botoes = new JPanel();
        botoes.add(cadastrar = new JButton("Cadastrar"));
        botoes.add(editar = new JButton("Editar"));
        botoes.add(apagar = new JButton("Apagar"));

        this.add(botoes);
        // tabela de carros
        JScrollPane jSPane = new JScrollPane();
        this.add(jSPane);
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "Placa", "Ano", "Marca", "Modelo", "Cor", "Preço" });
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        new CarrosDAO();

        atualizarTabela();

        // botoes de eventos
        // tratamento de Eventos
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    carPlacaField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 0)));
                    carAnoField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 1)));
                    carMarcaField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 2).toString()));
                    carModeloField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 3)));
                    carCorField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 4).toString()));
                    carPrecoField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 5).toString()));
                }
            }
        });

        // Botão de cadastrar
        cadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                carrosControl = new CarrosControl(carros, tableModel, table);

                carrosControl.createCarro(carPlacaField.getText(), Short.valueOf(carAnoField.getText()), carMarcaField.getText(), carModeloField.getText(), carCorField.getText(), Double.valueOf(carPrecoField.getText()));

                // Resetando os campos
                carPlacaField.setText("");
                carAnoField.setText("");
                carMarcaField.setText("");
                carModeloField.setText("");
                carCorField.setText("");
                carPrecoField.setText("");

                atualizarTabela();
            }
        });

        // Botão de editar
        editar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt){
                carrosControl = new CarrosControl(carros, tableModel, table);

                carrosControl.updateCarro(linhaSelecionada, carPlacaField.getText(), Short.valueOf(carAnoField.getText()), carMarcaField.getText(), carModeloField.getText(), carCorField.getText(), Double.valueOf(carPrecoField.getText()));

                atualizarTabela();
            }
        });

        // Botão de apagar
        apagar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt){
                carrosControl = new CarrosControl(carros, tableModel, table);

                carrosControl.deleteCarro(linhaSelecionada, carPlacaField.getText());

                // Resetando os campos
                carPlacaField.setText("");
                carAnoField.setText("");
                carMarcaField.setText("");
                carModeloField.setText("");
                carCorField.setText("");
                carPrecoField.setText("");

                atualizarTabela();
            }
        });
    }

    private void atualizarTabela() {
        try {
            tableModel.setRowCount(0);
            carros = new CarrosDAO().readAll();
            Object linha[] = new Object[6];
            for(int i=0; i < carros.size(); i++){
                linha[0] = carros.get(i).getPlaca();
                linha[1] = carros.get(i).getAno();
                linha[2] = carros.get(i).getMarca();
                linha[3] = carros.get(i).getModelo();
                linha[4] = carros.get(i).getCor();
                linha[5] = carros.get(i).getPreco();
                tableModel.addRow(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}







//Exemplo do VendasView com combobox (mostrar apenas o que for selecionado)
// public class VendasView extends JPamel{
//     JComboBox<String> carrosComboBox;
//     List<Carros> carros;
//     public VendasView() {
//         carrosComboBox = new JComboBox<>();
//         //Preencher o comboBox com os carros
//         carros = new CarrosDAO().readAll();
//         carrosComboBox.addItem("Selecione o Carro");
    
//         for (Carro carro : carros){
//             carrosComboBox.addItem(carro.getMarca() +" "+ carro.getModelo() +" "+ carro.getPlaca());
//         }
//         add(carrosComboBox);
//     }
// }