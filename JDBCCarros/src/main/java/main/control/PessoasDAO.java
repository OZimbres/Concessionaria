package main.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.connection.ConnectionFactory;
import main.model.Pessoa;

public class PessoasDAO {
    //-----===| ATRIBUTOS |===-----//
    private Connection connection; // Conexão
    private List<Pessoa> pessoas; // Lista de Pessoas

    //-----===| CONSTRUTOR |===-----//
    public PessoasDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    //-----===| MÉTODOS CRUD |===-----//
    //---=| CREATE |=---//
    public void create(Long cpf, String nome, Long telefone, String rua, String numero, Integer cep, String senha, boolean funcionario) throws SQLException {
        String query = "INSERT INTO Pessoas (cpf, nome, telefone, rua, numero, cep, senha, funcionario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setLong(1, cpf);
            preparedStatement.setString(2, nome);
            preparedStatement.setLong(3, telefone);
            preparedStatement.setString(4, rua);
            preparedStatement.setString(5, numero);
            preparedStatement.setDouble(6, cep);
            preparedStatement.setString(7, senha);
            preparedStatement.setBoolean(8, funcionario);
            preparedStatement.execute();

            System.out.println("Dados inseridos com sucesso");

            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir dados no banco de dados.", e);
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    //---=| READ ALL |=---//
    public List<Pessoa> readAll() throws SQLException {
        ResultSet resultSet = null; // Objeto que armazena
        pessoas = new ArrayList<>();

        String query = "SELECT * FROM Pessoas;"; // SQL Query
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            resultSet = preparedStatement.executeQuery();
            // Loop para armazenar as informações do resultSet para a List<Pessoa>

            while(resultSet.next()){
                Pessoa Pessoa = new Pessoa(resultSet.getLong("cpf"), resultSet.getString("nome"), resultSet.getLong("telefone"), resultSet.getString("rua"), resultSet.getString("numero"), resultSet.getInt("cep"), resultSet.getString("senha"), resultSet.getBoolean("funcionario")); // Instanciando Pessoa com as informações optidas pela query

                //Adicionando objeto instanciado à lista
                pessoas.add(Pessoa);
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

        // Retornando lista (list<Pessoa>)
        return pessoas;
    }
    //---=| READ |=---//
    public List<Pessoa> read(String cpf) throws SQLException {
        ResultSet resultSet = null; // Objeto que armazena
        pessoas = new ArrayList<>();

        String query = "SELECT * FROM Pessoas WHERE cpf = ?;"; // SQL Query
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, cpf);

            resultSet = preparedStatement.executeQuery();
            // Loop para armazenar as informações do resultSet para a List<Pessoa>

            while(resultSet.next()){
                Pessoa Pessoa = new Pessoa(resultSet.getLong("cpf"), resultSet.getString("nome"), resultSet.getLong("telefone"), resultSet.getString("rua"), resultSet.getString("numero"), resultSet.getInt("cep"), resultSet.getString("senha"), resultSet.getBoolean("funcionario")); // Instanciando Pessoa com as informações optidas pela query

                //Adicionando objeto instanciado à lista
                pessoas.add(Pessoa);
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
        // Retornando lista (list<Pessoa>)
        return pessoas;
    }

    //---=| UPDATE |=---//
    public void update(Long cpf, String nome, Long telefone, String rua, String numero, Integer cep, String senha, boolean funcionario) throws SQLException {
        String query = "UPDATE Pessoas SET nome = ?, telefone = ?, rua = ?, numero = ?, cep = ?, senha = ?, funcionario = ? WHERE cpf = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, nome);
            preparedStatement.setLong(2, telefone);
            preparedStatement.setString(3, rua);
            preparedStatement.setString(4, numero);
            preparedStatement.setInt(5, cep);
            preparedStatement.setString(6, senha);
            preparedStatement.setBoolean(7, funcionario);
            preparedStatement.setLong(8, cpf);
            preparedStatement.execute();

            System.out.println("Dados atualizados com sucesso");

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
    public void delete(Long cpf) throws SQLException {
        String query = "DELETE FROM Pessoas WHERE cpf = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setLong(1, cpf);
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
