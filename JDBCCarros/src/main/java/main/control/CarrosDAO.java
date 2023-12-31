package main.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.connection.ConnectionFactory;
import main.model.Carro;

public class CarrosDAO {
    //-----===| ATRIBUTOS |===-----//
    private Connection connection; // Conexão
    private List<Carro> carros; // Lista de carros

    //-----===| CONSTRUTOR |===-----//
    public CarrosDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    //-----===| MÉTODOS CRUD |===-----//
    //---=| CREATE |=---//
    public void create(String placa, Short ano, String marca, String modelo, String cor, double preco) throws SQLException {
        String query = "INSERT INTO carros (placa, ano, marca, modelo, cor, preco) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, placa);
            preparedStatement.setShort(2, ano);
            preparedStatement.setString(3, marca);
            preparedStatement.setString(4, modelo);
            preparedStatement.setString(5, cor);
            preparedStatement.setDouble(6, preco);
            preparedStatement.execute();

            JOptionPane.showMessageDialog(null, "Carro cadastrado com sucesso!");

            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não é possível cadastrar novo carro com esta placa.\n Placa está em uso!");
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    //---=| READ ALL |=---//
    public List<Carro> readAll() throws SQLException {
        ResultSet resultSet = null; // Objeto que armazena
        carros = new ArrayList<>();

        String query = "SELECT * FROM carros;"; // SQL Query
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            resultSet = preparedStatement.executeQuery();
            // Loop para armazenar as informações do resultSet para a List<Carro>

            while(resultSet.next()){
                Carro carro = new Carro(resultSet.getString("placa"), resultSet.getShort("ano"), resultSet.getString("marca"), resultSet.getString("modelo"), resultSet.getString("cor"), resultSet.getDouble("preco"), resultSet.getBoolean("vendido")); // Instanciando carro com as informações optidas pela query

                //Adicionando objeto instanciado à lista
                carros.add(carro);
            }
            // Fechando prepared statement 
            ConnectionFactory.closePreparedStatement(preparedStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally { // Independente se deu certo ou não, tem que fechar a conexão
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        }

        // Retornando lista (list<carro>)
        return carros;
    }
    //---=| READ |=---//
    public Carro read(String placa) throws SQLException {
        ResultSet resultSet = null; // Objeto que armazena
        carros = new ArrayList<>();

        String query = "SELECT * FROM carros WHERE placa = ?;"; // SQL Query
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, placa);

            resultSet = preparedStatement.executeQuery();
            // Loop para armazenar as informações do resultSet para a List<Carro>

            if(resultSet.next()){
                Carro carro = new Carro(resultSet.getString("placa"), resultSet.getShort("ano"), resultSet.getString("marca"), resultSet.getString("modelo"), resultSet.getString("cor"), resultSet.getDouble("preco"), resultSet.getBoolean("vendido")); // Instanciando carro com as informações optidas pela query

                return carro;
            }
            else{
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        finally { // Independente se deu certo ou não, tem que fechar a conexão
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        }
    }
    //---=| READ IS VENDIDO|=---//
    public List<Carro> readIs_vendido() throws SQLException {
        ResultSet resultSet = null; // Objeto que armazena
        carros = new ArrayList<>();

        String query = "SELECT * FROM carros WHERE vendido = FALSE;"; // SQL Query
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            resultSet = preparedStatement.executeQuery();
            // Loop para armazenar as informações do resultSet para a List<Carro>

            while(resultSet.next()){
                Carro carro = new Carro(resultSet.getString("placa"), resultSet.getShort("ano"), resultSet.getString("marca"), resultSet.getString("modelo"), resultSet.getString("cor"), resultSet.getDouble("preco"), resultSet.getBoolean("vendido")); // Instanciando carro com as informações optidas pela query

                //Adicionando objeto instanciado à lista
                carros.add(carro);
            }
            // Fechando prepared statement 
            ConnectionFactory.closePreparedStatement(preparedStatement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally { // Independente se deu certo ou não, tem que fechar a conexão
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        }
        // Retornando lista (list<carro>)
        return carros;
    }

    //---=| UPDATE |=---//
    public void update(String placa, Short ano, String marca, String modelo, String cor, Double preco) throws SQLException {
        String query = "UPDATE carros SET ano = ?, marca = ?, modelo = ?, cor = ?, preco = ? WHERE placa = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setShort(1, ano);
            preparedStatement.setString(2, marca);
            preparedStatement.setString(3, modelo);
            preparedStatement.setString(4, cor);
            preparedStatement.setDouble(5, preco);
            preparedStatement.setString(6, placa);
            preparedStatement.execute();
            
            JOptionPane.showMessageDialog(null, "Carro atualizado com sucesso!");

            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir dados no banco de dados.", e);
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    //---=| DELETE |=---//
    public void delete(String placa) throws SQLException {
        String query = "DELETE FROM carros WHERE placa = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, placa);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluido com sucesso!");

            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir: " + ex);
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        }
    }
}
