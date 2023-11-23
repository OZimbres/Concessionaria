package main.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import main.connection.ConnectionFactory;

public class LoginDAO {
    //-----===| ATRIBUTOS |===-----//
    private Connection connection; // Conexão

    //-----===| CONSTRUTOR |===-----//
    public LoginDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    //-----===| MÉTODOS |===-----//
    public boolean consulta(Long cpf, String senha) throws SQLException{
        ResultSet resultSet = null;

        String query = "SELECT * FROM pessoas WHERE cpf = ? AND senha = ? AND funcionario = TRUE;";
        PreparedStatement preparedStatement = connection.prepareCall(query);
        try {
            preparedStatement.setLong(1, cpf);
            preparedStatement.setString(2, senha);

            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir: " + ex);
            // Vai retornar False por "default" em caso de erro
            return false;
        } finally {
            ConnectionFactory.closePreparedStatement(preparedStatement);
            ConnectionFactory.closeConnection(this.connection);
        }

    }
}
