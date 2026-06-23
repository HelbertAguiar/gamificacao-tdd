package com.gamificacao.main;

import com.gamificacao.main.RepositorioPontos;

public class Placar {

    private final RepositorioPontos repositorio;

    public Placar(RepositorioPontos repositorio) {
        this.repositorio = repositorio;
    }

    public void registrarPontos(String usuario, String tipo, int quantidade) {
        repositorio.registrarPontos(usuario, tipo, quantidade);
    }
}