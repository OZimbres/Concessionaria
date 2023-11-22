package main.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.control.CarrosControl;
import main.control.CarrosDAO;
import main.control.PessoasControl;
import main.control.PessoasDAO;
//import Controller.VendasControl;
//import Controller.VendasDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;

import main.model.Carro;
import main.model.Pessoa;
//import main.model.Venda;

public class PainelVendas extends JPanel {

    private JTextField inputData;
    private JTextField inputCliente;
    private JTextField inputValor;
    private JTextField inputCarro;

    private JLabel labelData;
    private JLabel labelCliente;
    private JLabel labelValor;
    private JLabel labelCarro;

    private DefaultTableModel tableModel;
    private JTable table;
    //private List<Vendas> vendas = new ArrayList<>();
    private List<Carro> carros;
    private List<Pessoa> pessoas;
    private int linhaSelecionada = -1;
    private JButton cadastrarButton, apagarButton, editarButton, atualizarButton;

    JComboBox<String> carrosComboBox;
    JComboBox<String> clientesComboBox;

    // Construtor
    public PainelVendas() {
        JPanel painel1 = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();
        JPanel buttons = new JPanel();

        carrosComboBox = new JComboBox<>();
        clientesComboBox = new JComboBox<>();

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Cliente");
        tableModel.addColumn("Data");
        tableModel.addColumn("Carro");
        tableModel.addColumn("Valor");
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);

        // Campos de entrada
        inputCliente = new JTextField(20);
        inputData = new JTextField(10);
        inputCarro = new JTextField(20);
        inputValor = new JTextField(10);

        // Rótulos
        labelData = new JLabel("Data");
        labelCarro = new JLabel("Carro");
        labelValor = new JLabel("Valor");

        // Botões
        cadastrarButton = new JButton("Comprar");
        apagarButton = new JButton("Apagar");
        editarButton = new JButton("Editar");
        atualizarButton = new JButton("Atualizar");

        // Adicionar rótulos e campos de entrada ao painel
        inputPanel.add(labelData);
        inputPanel.add(inputData);
        inputPanel.add(labelValor);
        inputPanel.add(inputValor);

        // Adicionar botões ao painel
        buttons.add(cadastrarButton);
        buttons.add(editarButton);
        buttons.add(apagarButton);
        buttons.add(atualizarButton);

        // Adicionar elementos ao painel principal
        this.add(painel1);
        painel1.add(scrollPane, BorderLayout.CENTER);
        painel1.add(inputPanel, BorderLayout.NORTH);
        painel1.add(buttons, BorderLayout.SOUTH);

        // Configurar ComboBox de carros
        carrosComboBox.addItem("Selecione um Carro");
        carros = new CarrosDAO().readAll();
        for (Carro carro : carros) {
            carrosComboBox.addItem(carro.getMarca() + " " + carro.getModelo());
        }

        // Configurar ComboBox de clientes
        clientesComboBox.addItem("Selecione um cliente");
        pessoas = new PessoasDAO().readAll();
        for (Pessoa cliente : pessoas) {
            clientesComboBox.addItem(cliente.getNome() + " " + cliente.getCpf());
        }

        // Adicionar ComboBox de carros e clientes ao painel de entrada
        inputPanel.add(clientesComboBox);
        inputPanel.add(carrosComboBox);

        // Criar tabela e atualizar
        //new VendasDAO().createTable();
        //atualizarTabela();

        // Configurar ouvinte de eventos do mouse para a tabela
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    inputData.setText((String) table.getValueAt(linhaSelecionada, 0));
                    inputCliente.setText((String) table.getValueAt(linhaSelecionada, 1));
                    inputValor.setText((String) table.getValueAt(linhaSelecionada, 2));
                    inputCarro.setText((String) table.getValueAt(linhaSelecionada, 3));
                }
            }
        });

        // Criar instância do controlador para operações nas vendas
       // VendasControl operacoes = new VendasControl(vendas, tableModel, table);

        // Configurar ouvinte de eventos para o botão de cadastrar
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obter dados dos campos e ComboBoxes
                String data = inputData.getText();
                String valor = inputValor.getText();
                String clienteSelecionado = (String) clientesComboBox.getSelectedItem();
                String carroSelecionado = (String) carrosComboBox.getSelectedItem();

                // Validar campos
                if (data.isEmpty() || valor.isEmpty() || clienteSelecionado.equals("Selecione um cliente")
                        || carroSelecionado.equals("Selecione um Carro")) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
                } else {
                    if (!valor.matches("[0-9]+")) {
                        JOptionPane.showMessageDialog(null, "O campo 'Valor' deve conter apenas números.");
                    } else {
                        // Validar formato de data
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        dateFormat.setLenient(false);

                        try {
                            Date parsedDate = dateFormat.parse(data);
                            if (!data.equals(dateFormat.format(parsedDate))) {
                                throw new ParseException("Formato inválido", 0);
                            }

                            // Extrair CPF do cliente
                            String cliente = clienteSelecionado.split(" ")[0];
                            // Chamar método para cadastrar venda
                            operacoes.cadastrar(data, cliente, valor, carroSelecionado);

                            // Limpar campos após cadastrar
                            inputData.setText("");
                            inputValor.setText("");
                            clientesComboBox.setSelectedIndex(0);
                            carrosComboBox.setSelectedIndex(0);

                            // Deletar carro após venda
                            new CarrosDAO().delete(carroSelecionado);

                            JOptionPane.showMessageDialog(null, "Venda cadastrada!");
                        } catch (ParseException ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Formato de data inválido. Utilize o formato dd/MM/yyyy.");
                        }
                    }
                }
            }
        });

        // Configurar ouvinte de eventos para o botão de editar
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clienteSelecionado = (String) clientesComboBox.getSelectedItem();
                String carroSelecionado = (String) carrosComboBox.getSelectedItem();
                if (inputCarro.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione algo para editar");
                } else {
                    operacoes.atualizar(inputData.getText(), clienteSelecionado, inputValor.getText(),
                            carroSelecionado);

                    // Limpar campos após editar
                    inputData.setText("");
                    inputValor.setText("");
                    clientesComboBox.setSelectedIndex(0);
                    carrosComboBox.setSelectedIndex(0);

                    JOptionPane.showMessageDialog(null, "Informação editada com Sucesso!");
                }

            }
        });

        // Configurar ouvinte de eventos para o botão de apagar
        apagarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String carroSelecionado = (String) carrosComboBox.getSelectedItem();
                if (inputCarro.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione um anúncio para excluir.");
                } else {
                    int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja excluir os dados?",
                            "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (resposta == JOptionPane.YES_OPTION) {
                        // Chamar método para apagar venda
                        operacoes.apagar(inputCarro.getText());
                        JOptionPane.showMessageDialog(null, "O anúncio foi excluído!");

                        // Limpar campos após excluir
                        inputData.setText("");
                        inputValor.setText("");
                        clientesComboBox.setSelectedIndex(0);
                        carrosComboBox.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "A venda não foi excluída");
                    }
                }
            }
        });

        // Configurar ouvinte de eventos para o botão de atualizar ComboBoxes
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarComboBoxClientes();
                atualizarComboBoxCarros();
            }
        });

    }

    // Método para atualizar os dados na tabela
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        vendas = new VendasDAO().listAll();
        for (Vendas venda : vendas) {
            tableModel.addRow(
                    new Object[] { venda.getCliente(), venda.getData(), venda.getTipoCarro(), venda.getValor() });
        }
    }

    // Método para atualizar os dados no ComboBox de clientes
    private void atualizarComboBoxClientes() {
        clientesComboBox.removeAllItems();
        clientesComboBox.addItem("Selecione um cliente");
        clientes = new ClientesDAO().listAll();
        for (Clientes cliente : clientes) {
            clientesComboBox.addItem(cliente.getNome() + " " + cliente.getCpf());
        }
    }

    // Método para atualizar os dados no ComboBox de carros
    private void atualizarComboBoxCarros() {
        carrosComboBox.removeAllItems();
        carrosComboBox.addItem("Selecione um Carro");
        carros = new CarrosDAO().listAll();
        for (Carros carro : carros) {
            carrosComboBox.addItem(carro.getMarca() + " " + carro.getModelo());
        }
    }
}