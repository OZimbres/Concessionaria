package main.control;

import java.sql.SQLException;

import javax.swing.JOptionPane;

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

    //---=| CHECAGEM DE CAMPO |=---//
    public boolean checkLogin(String cpf, String senha){
        if(cpf.isEmpty() || senha.isEmpty()){
            JOptionPane.showMessageDialog(null, "ATENÇÃO! \nPreencha todos os campos!");

            return false;
        } 
        else{
            if(!validarFormatoCPF(cpf)){
                JOptionPane.showMessageDialog(null, "CPF inválido! O CPF deve conter apenas números e ter 11 dígitos.");

                return false;
            }
            else if(!validarFormatoSenha(senha)){
                JOptionPane.showMessageDialog(null, "Senha deve conter no máximo 24 caracteres");

                return false;
            }
            else{
                try {
                    if(verifyLogin(Long.valueOf(cpf), senha)){
                        return true;
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Acesso Negado!");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } 
    }

    // Método para validar o formato do CPF
    private boolean validarFormatoCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.length() == 11;
    }
    // Método para validar o formato da senha
    private boolean validarFormatoSenha(String senha) {
        if (senha == null || senha.length() > 24) {
            return false;
        }
        return true;
    }
}
