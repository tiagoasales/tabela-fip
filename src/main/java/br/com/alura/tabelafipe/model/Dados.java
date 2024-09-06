package br.com.alura.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dados(Integer codigo, String nome) {
    @Override
    public String toString() {
        return "Cód: " + codigo + ", Descrição: " + nome;
    }
}
