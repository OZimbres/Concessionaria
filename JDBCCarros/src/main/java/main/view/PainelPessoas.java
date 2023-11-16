package main.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import main.control.PessoasControl;
import main.control.PessoasDAO;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import main.model.Pessoa;

public class PainelPessoas extends JPanel {
    // Atributos(componentes)
    private JButton cadastrar, apagar, editar;
    private JTextField pessoaCpfField, pessoaNomeField, pessoaTelefoneField, pessoaRuaField, pessoaNumeroField, pessoaCepField, pessoaSenhaField;
    private JCheckBox pessoaFuncionarioField;
    private List<Pessoa> pessoas;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    private PessoasControl pessoasControl;

    // Construtor(GUI-JPanel)
    public PainelPessoas() {
        super();

        // entrada de dados
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Cadastro Pessoas"));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2));
        inputPanel.add(new JLabel("CPF"));
        pessoaCpfField = new JTextField(20);
        inputPanel.add(pessoaCpfField);

        inputPanel.add(new JLabel("Nome"));
        pessoaNomeField = new JTextField(20);
        inputPanel.add(pessoaNomeField);

        inputPanel.add(new JLabel("Telefone"));
        pessoaTelefoneField = new JTextField(20);
        inputPanel.add(pessoaTelefoneField);

        inputPanel.add(new JLabel("Rua"));
        pessoaRuaField = new JTextField(20);
        inputPanel.add(pessoaRuaField);

        inputPanel.add(new JLabel("Número"));
        pessoaNumeroField = new JTextField(20);
        inputPanel.add(pessoaNumeroField);

        inputPanel.add(new JLabel("CEP"));
        pessoaCepField = new JTextField(20);
        inputPanel.add(pessoaCepField);

        inputPanel.add(new JLabel("Senha"));
        pessoaSenhaField = new JTextField(20);
        inputPanel.add(pessoaSenhaField);

        inputPanel.add(new JLabel("Funcionário"));
        pessoaFuncionarioField = new JCheckBox("Funcionário");
        inputPanel.add(pessoaFuncionarioField);

        this.add(inputPanel);

        JPanel botoes = new JPanel();
        botoes.add(cadastrar = new JButton("Cadastrar"));
        botoes.add(editar = new JButton("Editar"));
        botoes.add(apagar = new JButton("Apagar"));

        add(botoes);
        // tabela de Pessoas
        JScrollPane jSPane = new JScrollPane();
        add(jSPane);
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "CPF", "Nome", "Telefone", "Rua", "Número", "CEP", "Senha", "Funcionário" });
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        new PessoasDAO();

        atualizarTabela();

        // botoes de eventos
        // tratamento de Eventos
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    pessoaCpfField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 0)));
                    pessoaNomeField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 1)));
                    pessoaTelefoneField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 2).toString()));
                    pessoaRuaField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 3)));
                    pessoaNumeroField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 4).toString()));
                    pessoaCepField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 5).toString()));
                    pessoaSenhaField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 6).toString()));
                    pessoaFuncionarioField.setText(String.valueOf(table.getValueAt(linhaSelecionada, 7).toString()));
                }
            }
        });

        // Botão de cadastrar
        cadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                pessoasControl = new PessoasControl(pessoas, tableModel, table);

                pessoasControl.createPessoa(Integer.valueOf(pessoaCpfField.getText()), pessoaNomeField.getText(), Integer.valueOf(pessoaTelefoneField.getText()), pessoaRuaField.getText(), pessoaNumeroField.getText(), Integer.valueOf(pessoaNomeField.getText()), pessoaSenhaField.getText(), pessoaFuncionarioField.isSelected());

                // Resetando os campos
                pessoaCpfField.setText("");
                pessoaNomeField.setText("");
                pessoaTelefoneField.setText("");
                pessoaRuaField.setText("");
                pessoaNumeroField.setText("");
                pessoaCepField.setText("");
                pessoaSenhaField.setText("");
                pessoaFuncionarioField.setText("");

                atualizarTabela();
            }
        });

        // Botão de editar
        editar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt){
                pessoasControl = new PessoasControl(pessoas, tableModel, table);

                pessoasControl.updatePessoa(linhaSelecionada, Integer.valueOf(pessoaCpfField.getText()), pessoaNomeField.getText(), Integer.valueOf(pessoaTelefoneField.getText()), pessoaRuaField.getText(), pessoaNumeroField.getText(), Integer.valueOf(pessoaNomeField.getText()), pessoaSenhaField.getText(), Boolean.valueOf(pessoaFuncionarioField.getText()));

                atualizarTabela();
            }
        });

        // Botão de apagar
        apagar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt){
                pessoasControl = new PessoasControl(pessoas, tableModel, table);

                pessoasControl.deletePessoa(linhaSelecionada, Integer.valueOf(pessoaCpfField.getText()));

                // Resetando os campos
                pessoaCpfField.setText("");
                pessoaNomeField.setText("");
                pessoaTelefoneField.setText("");
                pessoaRuaField.setText("");
                pessoaNumeroField.setText("");
                pessoaCepField.setText("");

                atualizarTabela();
            }
        });
    }

    private void atualizarTabela() {
        try {
            tableModel.setRowCount(0);
            pessoas = new PessoasDAO().readAll();
            Object linha[] = new Object[8];
        for (int i = 0; i < pessoas.size(); i++) {
            linha[0] = pessoas.get(i).getCpf();
            linha[1] = pessoas.get(i).getNome();
            linha[2] = pessoas.get(i).getTelefone();
            linha[3] = pessoas.get(i).getRua();
            linha[4] = pessoas.get(i).getNumero();
            linha[5] = pessoas.get(i).getCep();
            linha[6] = pessoas.get(i).getSenha();
            linha[7] = pessoas.get(i).getFuncionario();
                tableModel.addRow(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}