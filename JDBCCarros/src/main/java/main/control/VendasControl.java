package main.control;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.model.Venda;

public class VendasControl {
    // -----===| ATRIBUTOS |===-----//
    private VendasDAO vendasDAO = new VendasDAO();
    private List<Venda> vendas;
    private DefaultTableModel tableModel;
    private JTable table;


    // -----===| CONSTRUTOR |===-----//
    public VendasControl(List<main.model.Venda> vendas, DefaultTableModel tableModel, JTable table){
        this.vendas = vendas;
        this.tableModel = tableModel;
        this.table = table;
    }

    // -----===| MÉTODOS |===-----//
    //---=| CREATE |=---//
    public void createVenda(String placa_carro, Long cpf_cliente, Long cpf_vendedor, Date data_venda){
        try {
            vendasDAO.create(placa_carro, cpf_cliente, cpf_vendedor, data_venda);

            System.out.println("msg1");
            // Atributos temporários para a instanciação do objeto Venda
            Venda venda = new Venda(vendasDAO.get_id_venda(placa_carro), placa_carro, cpf_cliente, cpf_vendedor, data_venda);
            System.out.println("msg2");
            vendas.add(venda);
            System.out.println("msg3");

            atualizarTabela();
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro sql: "+ e);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: "+ e);
        } 
    }

    //---=| Atualizar Tabela |=---//
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        Object linha[] = new Object[5];
        for (int i = 0; i < vendas.size(); i++) {
            linha[0] = vendas.get(i).getId_venda();
            linha[1] = vendas.get(i).getData_venda();
            linha[2] = vendas.get(i).getPlaca_carro();
            linha[3] = vendas.get(i).getCpf_cliente();
            linha[4] = vendas.get(i).getCpf_vendedor();
        }
    }
}
