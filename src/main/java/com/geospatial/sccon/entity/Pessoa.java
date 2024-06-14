package com.geospatial.sccon.entity;

public class Pessoa {
    private int id;
    private String nome;
    private String dataDeNascimento;
    private String dataDeAdmissao;

    // Construtor para inicializar os atributos
    public Pessoa(int id, String nome, String dataDeNascimento, String dataDeAdmissao) {
        this.id = id;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.dataDeAdmissao = dataDeAdmissao;
    }

    // Métodos getters


    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public void setDataDeAdmissao(String dataDeAdmissao) {
        this.dataDeAdmissao = dataDeAdmissao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public String getDataDeAdmissao() {
        return dataDeAdmissao;
    }

    // Método toString para representação da instância
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Nome: " + nome + "\n" +
                "Data de Nascimento: " + dataDeNascimento + "\n" +
                "Data de Admissão: " + dataDeAdmissao;
    }


}
