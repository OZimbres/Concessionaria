package main.control;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.connection.ConnectionFactory;
import main.model.Venda;

public class VendasDAO {
    //-----===| ATRIBUTOS |===-----//
    private Connection connection; // Conexão
    private List<Venda> vendas; // Lista de Vendas
    
    //-----===| CONSTRUTOR |===-----//
    public VendasDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    //-----===| MÉTODOS |===-----//
    //---=| CREATE |=---//
    /**
     * @param placa_carro
     * @param cpf_cliente
     * @param cpf_vendedor
     * @param data_venda
     * @throws SQLException
     */
    public void create(String placa_carro, Long cpf_cliente, Long cpf_vendedor, Date data_venda) throws SQLException {
        String query = "INSERT INTO vendas(placa_carro, cpf_cliente, cpf_vendedor, data_venda) VALUES (?, ?, ?, ?); UPDATE carros SET vendido = TRUE WHERE placa = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, placa_carro);
            preparedStatement.setLong(2, cpf_cliente);
            preparedStatement.setLong(3, cpf_vendedor);
            preparedStatement.setDate(4, data_venda);
            preparedStatement.setString(5, placa_carro);
            preparedStatement.execute();

            JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir: " + ex);
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    //---=| READ ALL |=---//
    public List<Venda> readAll() throws SQLException {
        ResultSet resultSet = null; // Objeto que armazena valor retornado pela consulta
        vendas = new ArrayList<>();

        String query = "SELECT * FROM vendas;"; // SQL Query

        PreparedStatement preparedStatement = connection.prepareCall(query);
        try {
            resultSet = preparedStatement.executeQuery();
            
            // Loop para armazenar as informações do resultSet para a List<Venda>
            while(resultSet.next()){
                Venda venda = new Venda(resultSet.getInt("id_venda"), resultSet.getString("placa_carro"), resultSet.getLong("cpf_cliente"), resultSet.getLong("cpf_vendedor"), resultSet.getDate("data_venda")); // Instanciando Venda com as informações obtidas pela query

                // Adicionando objeto instanciado à lista
                vendas.add(venda);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar:"+ ex);
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        }

        // Retornando lista (list<Venda>)
        return vendas;
    }

    //---=| GET ID_VENDA |=---//
    public Integer get_id_venda(String placa_carro) throws SQLException {
        ResultSet resultSet = null;
        Integer id_venda = -1;

        String query = "SELECT * FROM vendas WHERE placa_carro = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, placa_carro);

            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()){
                id_venda = resultSet.getInt("id_venda");
                return id_venda;
            }
            else{
                return id_venda;
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar:"+ ex);
            return id_venda;
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        }
    }
}
