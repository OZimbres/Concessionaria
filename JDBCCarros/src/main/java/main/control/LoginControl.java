package main.control;

import java.sql.SQLException;

import main.model.Pessoa;

public class LoginControl {
    //-----===| ATRIBUTOS |===-----//
    private static Pessoa usuario;
    private LoginDAO loginDAO;

    //-----===| MÉTODOS |===-----//
    public boolean verifyLogin(Long cpf, String senha) throws SQLException{
        loginDAO = new LoginDAO();
        return loginDAO.consulta(cpf, senha);
    }
}
