package main.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.SQLException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.control.CarrosDAO;
import main.control.LoginControl;
import main.control.PessoasDAO;
import main.control.VendasControl;
import main.control.VendasDAO;

import java.sql.Date;

import main.model.Carro;
import main.model.Login;
import main.model.Pessoa;
import main.model.Venda;

public class PainelVendas extends JPanel {

    private JButton cadastrarVenda;

    private JLabel infoCliente = new JLabel();
    private JLabel infoCarro = new JLabel();

    private DefaultTableModel tableModel;
    private JTable table;
    private List<Venda> vendas = new ArrayList<>();
    private List<Carro> carros;
    private List<Pessoa> pessoas;

    private VendasControl vendasControl;

    JComboBox<String> carrosComboBox;
    JComboBox<String> clientesComboBox;

    private java.util.Date dataAtual;
    private Date dataAtualSql;

    // Construtor
    public PainelVendas(Login logado) {
        super();

        // Entrada de dados
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Venda"));
        // Painel de entrada de dados
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4,2));

        // ComboBox para escolha de clientes/carros
        // Configurar ComboBox de carros
        carrosComboBox = new JComboBox<>();
        carrosComboBox.addItem(null);
        try {
            carros = new CarrosDAO().readIs_vendido();
            for (Carro carro : carros) {
                carrosComboBox.addItem(carro.getPlaca());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Configurar ComboBox de clientes
        clientesComboBox = new JComboBox<>();
        clientesComboBox.addItem(null);
        try {
            pessoas = new PessoasDAO().readAll();
            for (Pessoa cliente : pessoas) {
                clientesComboBox.addItem(String.valueOf(cliente.getCpf()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Adicionando itens ao InputPanel
        inputPanel.add(new JLabel("Cliente "));
        inputPanel.add(clientesComboBox);
        inputPanel.add(infoCliente);
        inputPanel.add(new JLabel()); // Só pra ajustar visualmente
        inputPanel.add(new JLabel("Carros "));
        inputPanel.add(carrosComboBox);
        inputPanel.add(infoCarro);
        this.add(inputPanel);

        // Painel de botões
        JPanel botoes = new JPanel();
        botoes.add(cadastrarVenda = new JButton("Vender"));
        this.add(botoes);

        // Tabela das Vendas
        JScrollPane jSPane = new JScrollPane();
        this.add(jSPane);
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] {"Venda ID", "Placa", "Cliente", "Vendedor", "Data"});
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        new VendasDAO();

        atualizarTabela();

        // Botoes de eventos
        // Atualizar Info quando Item for selecionado (comboBox)
        clientesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PessoasDAO pessoasDAO = new PessoasDAO();
                    Pessoa pessoa = new Pessoa();
                    pessoa = pessoasDAO.read(Long.valueOf((String) clientesComboBox.getSelectedItem()));
                    infoCliente.setText("Nome: "+ pessoa.getNome() +" | CPF: "+ pessoa.getCpf());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        carrosComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CarrosDAO carrosDAO = new CarrosDAO();
                    Carro carro = new Carro();
                    carro = carrosDAO.read((String) carrosComboBox.getSelectedItem());
                    infoCarro.setText("Placa: "+ carro.getPlaca() +" | Marca: "+ carro.getMarca() +" | Modelo: "+ carro.getModelo() +" | Cor: "+ carro.getCor() +" | Ano: "+ carro.getAno() +" | Preço: "+ carro.getPreco());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Tratamento de Evento
        cadastrarVenda.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt){
                vendasControl = new VendasControl(vendas, tableModel, table);

                // variáveis temporárias
                String placaCarroSelecionado = (String) carrosComboBox.getSelectedItem();
                Long cpfClienteSelecionado = Long.valueOf((String) clientesComboBox.getSelectedItem());
                dataAtual = Calendar.getInstance().getTime();
                dataAtualSql = new Date(dataAtual.getTime());

                vendasControl.createVenda(placaCarroSelecionado, cpfClienteSelecionado, logado.getCpf(), dataAtualSql);
            }
        });
    }

    // Método para atualizar os dados na tabela
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        try {
            vendas = new VendasDAO().readAll();
            for (Venda venda : vendas) {
                tableModel.addRow(new Object[] { venda.getId_venda(), venda.getPlaca_carro(), venda.getCpf_cliente(), venda.getCpf_vendedor(), venda.getData_venda() });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}