package main.model;

import java.sql.Date;

public class Venda {
    //-----===| ATRIBUTOS |===-----//
    private Integer id_venda;
    private String placa_carro;
    private Long cpf_cliente;
    private Long cpf_vendedor;
    private Date data_venda;

    //-----===| CONSTRUTOR |===-----//
    public Venda() {}

    public Venda(String placa_carro, Long cpf_cliente, Long cpf_vendedor, Date data_venda) {
        this.placa_carro = placa_carro;
        this.cpf_cliente = cpf_cliente;
        this.cpf_vendedor = cpf_vendedor;
        this.data_venda = data_venda;
    }

    public Venda(Integer id_venda, String placa_carro, Long cpf_cliente, Long cpf_vendedor, Date data_venda) {
        this.id_venda = id_venda;
        this.placa_carro = placa_carro;
        this.cpf_cliente = cpf_cliente;
        this.cpf_vendedor = cpf_vendedor;
        this.data_venda = data_venda;
    }

    //-----===| GETTERS & SETTERS |===-----//
    public Integer getId_venda() {
        return id_venda;
    }
    public void setId_venda(Integer id_venda) {
        this.id_venda = id_venda;
    }

    public String getPlaca_carro() {
        return placa_carro;
    }
    public void setPlaca_carro(String placa_carro) {
        this.placa_carro = placa_carro;
    }

    public Long getCpf_cliente() {
        return cpf_cliente;
    }
    public void setCpf_cliente(Long cpf_cliente) {
        this.cpf_cliente = cpf_cliente;
    }

    public Long getCpf_vendedor() {
        return cpf_vendedor;
    }
    public void setCpf_vendedor(Long cpf_vendedor) {
        this.cpf_vendedor = cpf_vendedor;
    }

    public Date getData_venda() {
        return data_venda;
    }
    public void setData_venda(Date data_venda) {
        this.data_venda = data_venda;
    }
}
