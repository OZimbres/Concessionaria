package main.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

    JComboBox<String> carrosComboBox = new JComboBox<>();
    JComboBox<String> clientesComboBox = new JComboBox<>();

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
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] {"ID Venda", "Vendedor", "Cliente", "Placa", "Marca", "Modelo", "Cor", "Data Venda", "Preço Venda"});
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
                    if(clientesComboBox.getSelectedItem() != null){
                        pessoa = pessoasDAO.read(Long.valueOf((String) clientesComboBox.getSelectedItem()));
                        infoCliente.setText("Nome: "+ pessoa.getNome() +" | CPF: "+ pessoa.getCpf());
                    }
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
                    if(carrosComboBox.getSelectedItem() != null){
                        carro = carrosDAO.read((String) carrosComboBox.getSelectedItem());
                        infoCarro.setText("Placa: "+ carro.getPlaca() +" | Marca: "+ carro.getMarca() +" | Modelo: "+ carro.getModelo() +" | Cor: "+ carro.getCor() +" | Ano: "+ carro.getAno() +" | Preço: "+ carro.getPreco());
                    }
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
                dataAtual = Calendar.getInstance().getTime();
                dataAtualSql = new Date(dataAtual.getTime());

                if(carrosComboBox.getSelectedItem() == null || clientesComboBox.getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "ATENÇÃO!\nExiste campos em branco");
                }
                else{
                    if(vendasControl.checkVendaCampos(String.valueOf(carrosComboBox.getSelectedItem()), String.valueOf(clientesComboBox.getSelectedItem()), String.valueOf(logado.getCpf()), dataAtualSql)){
                        // Resetando campos
                        clientesComboBox.setSelectedIndex(0);
                        infoCliente.setText("");
                        carrosComboBox.setSelectedIndex(0);
                        infoCarro.setText("");
                        atualizarClientesComboBox();
                        atualizarCarrosComboBox();
        
                        atualizarTabela();
                    }
                }
            }
        });
    }

    // Método para atualizar os dados na tabela
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        try {
            vendas = new VendasDAO().readAll();
            for (Venda venda : vendas) {
                // Pegando valor do cliente e do vendedor envolvidos na compra
                Pessoa cliente = new PessoasDAO().read(Long.valueOf(venda.getCpf_cliente()));
                Pessoa vendedor = new PessoasDAO().read(Long.valueOf(venda.getCpf_vendedor()));
                Carro carro = new CarrosDAO().read(venda.getPlaca_carro());

                tableModel.addRow(new Object[] { venda.getId_venda(), vendedor.getNome(), cliente.getNome(), venda.getPlaca_carro(), carro.getMarca(), carro.getModelo(), carro.getCor(), venda.getData_venda(), String.format("%,.2f", carro.getPreco()) });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar combobox
    public void atualizarClientesComboBox(){
        clientesComboBox.removeAllItems();
        clientesComboBox.addItem(null);
        try {
            pessoas = new PessoasDAO().readAll();
            for (Pessoa cliente : pessoas) {
                clientesComboBox.addItem(String.valueOf(cliente.getCpf()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        atualizarTabela();
    }

    public void atualizarCarrosComboBox(){
        carrosComboBox.removeAllItems();
        carrosComboBox.addItem(null);
        try {
            carros = new CarrosDAO().readIs_vendido();
            for (Carro carro : carros) {
                carrosComboBox.addItem(carro.getPlaca());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        atualizarTabela();
    }
}