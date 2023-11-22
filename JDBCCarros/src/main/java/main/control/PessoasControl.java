package main.control;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.model.Pessoa;

public class PessoasControl {
    // -----===| ATRIBUTOS |===-----//
    private PessoasDAO pessoasDAO = new PessoasDAO();
    private List<Pessoa> pessoas;
    private DefaultTableModel tableModel;
    private JTable table;

    // -----===| CONSTRUTOR |===-----//
    public PessoasControl(List<main.model.Pessoa> pessoas, DefaultTableModel tableModel, JTable table) {
        this.pessoas = pessoas;
        this.tableModel = tableModel;
        this.table = table;
    }

    // -----===| MÉTODOS CRUD |===-----//
    // ---=| CREATE |=---//
    public void createPessoa(Long cpf, String nome, Long telefone, String rua, String numero, Integer cep, String senha, boolean funcionario) {
        try {
            pessoasDAO.create(cpf, nome, telefone, rua, numero, cep, senha, funcionario);

            Pessoa pessoa = new Pessoa(cpf, nome, telefone, rua, numero, cep, senha, funcionario);
            pessoas.add(pessoa);

            atualizarTabela();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---=| UPDATE |=---//
    public void updatePessoa(int linhaSelecionada, Long cpf, String nome, Long telefone, String rua, String numero, Integer cep, String senha, boolean funcionario) {
        if (linhaSelecionada != -1) {
            try {
                pessoasDAO.update(cpf, nome, telefone, rua, numero, cep, senha, funcionario);
                
                Pessoa pessoa = new Pessoa(cpf, nome, telefone, rua, numero, cep, senha, funcionario);
                pessoas.set(linhaSelecionada, pessoa);
    
                atualizarTabela();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //---=| DELETE |=---//
    public void deletePessoa(int linhaSelecionada, Long cpf) {
        try {
            if(linhaSelecionada != 1){
                pessoasDAO.delete(cpf);
                pessoas.remove(linhaSelecionada);
                
                atualizarTabela();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---=| Atualizar Tabela |=---//
    private void atualizarTabela() {
        tableModel.setRowCount(0);
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
        }
    }
}