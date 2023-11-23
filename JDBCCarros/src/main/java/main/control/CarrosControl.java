package main.control;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.model.Carro;

public class CarrosControl {
    // -----===| ATRIBUTOS |===-----//
    private CarrosDAO carrosDAO = new CarrosDAO();
    private List<Carro> carros;
    private DefaultTableModel tableModel;
    private JTable table;

    // -----===| CONSTRUTOR |===-----//
    public CarrosControl(List<main.model.Carro> carros, DefaultTableModel tableModel, JTable table) {
        this.carros = carros;
        this.tableModel = tableModel;
        this.table = table;
    }

    // -----===| MÉTODOS CRUD |===-----//
    // ---=| CREATE |=---//
    public void createCarro(String placa, Short ano, String marca, String modelo, String cor, Double preco) {
        try {
            carrosDAO.create(placa, ano, marca, modelo, cor, preco);

            Carro carro = new Carro(placa, ano, marca, modelo, cor, preco, false);
            carros.add(carro);

            atualizarTabela();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---=| UPDATE |=---//
    public void updateCarro(int linhaSelecionada, String placa, Short ano, String marca, String modelo, String cor, Double preco) {
        if (linhaSelecionada != -1) {
            try {
                carrosDAO.update(placa, ano, marca, modelo, cor, preco);
                
                Carro carro = new Carro(placa, ano, marca, modelo, cor, preco, false);
                carros.set(linhaSelecionada, carro);
    
                atualizarTabela();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //---=| DELETE |=---//
    public void deleteCarro(int linhaSelecionada, String placa) {
        try {
            if(linhaSelecionada != 1){
                carrosDAO.delete(placa);
                carros.remove(linhaSelecionada);
                
                atualizarTabela();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---=| Atualizar Tabela |=---//
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        Object linha[] = new Object[6];

        for (int i = 0; i < carros.size(); i++) {
            linha[0] = carros.get(i).getPlaca();
            linha[1] = carros.get(i).getAno();
            linha[2] = carros.get(i).getPlaca();
            linha[3] = carros.get(i).getPlaca();
            linha[4] = carros.get(i).getCor();
            linha[5] = carros.get(i).getPreco();
        }
    }

    //---=| CHECAGEM DE CAMPO |=---//
    public boolean checkCarroCampos(Integer linhaSelecionada, String operacao, String placa, String ano, String marca, String modelo, String cor, String preco) {
        // Verifica se os campos estão preenchidos
        if (placa.isEmpty() || ano.isEmpty() || marca.isEmpty() || modelo.isEmpty() || cor.isEmpty() || preco.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ATENÇÃO!\nExiste campos em branco");
            return false;
        }    
        // Verifica o formato da placa
        if (!validarFormatoPlaca(placa)) {
            JOptionPane.showMessageDialog(null, "Insira o formato válido de placa!\nAAA-1111");
            return false;
        }
        // Verifica se o ano é numérico
        if (!validarFormatoAno(ano)) {
            JOptionPane.showMessageDialog(null, "Insira um formato válido de ano!\n1111");
            return false;
        }    
        // Verifica se o preço é numérico
        if (!validarFormatoPreco(preco)) {
            JOptionPane.showMessageDialog(null, "Insira um formato válido de preço!");
            return false;
        }
    
        if(operacao.equals("cadastrar")){
            int resposta = JOptionPane.showConfirmDialog(null,"Realizar cadastro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                // Executa a operação de cadastrar
                createCarro(placa, Short.valueOf(ano), marca, modelo, cor, Double.valueOf(preco));
            }
        }
        else if(operacao.equals("atualizar")){
            int resposta = JOptionPane.showConfirmDialog(null,"Realizar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                deleteCarro(linhaSelecionada, placa);
            }
        }
        else{
            int resposta = JOptionPane.showConfirmDialog(null,"Realizar edição?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                updateCarro(linhaSelecionada, placa, Short.valueOf(ano), marca, modelo, cor, Double.valueOf(preco));
            }
        }
        return true;
    }

    // Método para validar o formato da placa
    private boolean validarFormatoPlaca(String placa) {
        // Expressão regular para validar o formato da placa
        String regex = "[A-Z]{3}-\\d{4}$";

        // Remove espaços em branco antes e depois da placa
        placa = placa.trim();

        // Verifica se a placa corresponde à expressão regular
        return placa.matches(regex);
    }
    // Método para validar o formato do número (máximo 4 dígitos)
    private boolean validarFormatoAno(String numero) {
        // Expressão regular para validar o formato do número
        String regex = "^[0-9]{1,4}$";

        // Remove espaços em branco antes e depois do número
        numero = numero.trim();

        // Verifica se o número corresponde à expressão regular
        return numero.matches(regex);
    }
    // Método para validar o formato do preço
    private boolean validarFormatoPreco(String preco) {
        // Expressão regular para validar o formato do preço
        String regex = "^[0-9]+(\\.[0-9]+)?$";

        // Remove espaços em branco antes e depois do preço
        preco = preco.trim();

        // Verifica se o preço corresponde à expressão regular
        return preco.matches(regex);
    }
}