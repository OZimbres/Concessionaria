package main.model;

public class Login {
    //-----===| ATRIBUTOS |===-----//
    private Long cpf;

    //-----===| CONSTRUTOR |===-----//
    public Login() {}

    public Login(Long cpf) {
        this.cpf = cpf;
    }

    //-----===| MÉTODOS |===-----//
    public Long getCpf(){
        return cpf;
    }
    public void setCpf(Long cpf){
        this.cpf = cpf;
    }
}
