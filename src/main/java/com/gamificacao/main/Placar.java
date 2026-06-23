package com.gamificacao.main;

import com.gamificacao.main.RepositorioPontos;

import java.util.Map;

public class Placar {

    private final RepositorioPontos repositorio;

    public Placar(RepositorioPontos repositorio) {
        this.repositorio = repositorio;
    }

    public void registrarPontos(String usuario, String tipo, int quantidade) {
        repositorio.registrarPontos(usuario, tipo, quantidade);
    }

    public Map<String, Integer> recuperarPontosDoUsuario(String usuario) {
        Map<String, Integer> pontos = new java.util.HashMap<>();

        for (String tipo : repositorio.recuperarTiposDePonto()) {
            int quantidade = repositorio.recuperarPontos(usuario, tipo);

            if (quantidade > 0) {
                pontos.put(tipo, quantidade);
            }
        }

        return pontos;
    }

}