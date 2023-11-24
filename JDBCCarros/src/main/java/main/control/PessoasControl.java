package main.control;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
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

    //---=| CHECAGEM DE CAMPO |=---//
    public boolean checkPessoasCampos(int linhaSelecionada, String operacao, String cpf, String nome, String telefone, String rua, String numero, String cep, String senha, boolean funcionario) {
        // Verifica se os campos estão preenchidos
        if (cpf.isEmpty() || nome.isEmpty() || telefone.isEmpty() || rua.isEmpty() || numero.isEmpty() || cep.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ATENÇÃO!\nExiste campos em branco");
            return false;
        }    
        if(!validarFormatoCPF(cpf)){
            JOptionPane.showMessageDialog(null, "CPF inválido!\nO CPF deve conter apenas números e ter 11 dígitos!");
            return false;
        }
        if(!validarFormatoTelefone(telefone)){
            JOptionPane.showMessageDialog(null, "Telefone inválido!\nO Telefone deve conter apenas números e ter entre 9 e 11 dígitos!");
            return false;
        }
        if(!validarFormatoNumero(numero)){
            JOptionPane.showMessageDialog(null, "Número inválido!\nO Número deve conter até 10 dígitos e no máximo 1 letra!");
            return false;
        }
        if(!validarFormatoCEP(cep)){
            JOptionPane.showMessageDialog(null, "CEP inválido!\nO CEP deve conter apenas números e ter 8 dígitos!");
            return false;
        }

        if(operacao.equals("cadastrar")){
            int resposta = JOptionPane.showConfirmDialog(null,"Realizar cadastro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                // Executa a operação de cadastrar
                createPessoa(Long.valueOf(cpf.trim()), nome.trim(), Long.valueOf(telefone.trim()), rua.trim(), numero.trim(), Integer.valueOf(cep.trim()), senha.trim(), funcionario);
            }
        }
        else if(operacao.equals("atualizar")){
            int resposta = JOptionPane.showConfirmDialog(null,"Realizar edição?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                // Executa a operação de editar
                updatePessoa(linhaSelecionada, Long.valueOf(cpf.trim()), nome.trim(), Long.valueOf(telefone.trim()), rua.trim(), numero.trim(), Integer.valueOf(cep.trim()), senha, funcionario);
            }
        }
        else{
            int resposta = JOptionPane.showConfirmDialog(null,"Realizar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                // Executa a opção de deletar
                deletePessoa(linhaSelecionada, Long.valueOf(cpf.trim()));
            }
        }
        return true;
    }

    // Método para validar o formato do CPF
    private boolean validarFormatoCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.length() == 11;
    }
    private static boolean validarFormatoTelefone(String telefone) {
        // Remove qualquer caractere não numérico
        String numeroLimpo = telefone.replaceAll("[^0-9]", "");
        // Verifica se o número de caracteres está entre 9 e 11
        return numeroLimpo.length() >= 9 && numeroLimpo.length() <= 11;
    }
    // Método para validar o formato da entrada (máximo 10 caracteres, permitindo números e até 1 letra)
    private boolean validarFormatoNumero(String numero) {
        // Expressão regular para validar o formato da entrada
        String regex = "^[a-zA-Z0-9]{0,1}[0-9]{0,9}$";
        // Remove espaços em branco antes e depois da entrada
        numero = numero.trim();
        // Verifica se a entrada corresponde à expressão regular
        return numero.matches(regex);
    }
    // Método para validar a quantidade de caracteres
    private boolean validarFormatoCEP(String cep) {
        cep = cep.replaceAll("[^0-9]", "");
        return cep.length() == 8;
    }
}