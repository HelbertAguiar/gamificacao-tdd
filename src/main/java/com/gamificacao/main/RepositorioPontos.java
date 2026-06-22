package com.gamificacao.main;

public interface RepositorioPontos {
    void registrarPontos(String usuario, String tipoPonto, int pontuacao);
    int recuperarPontos(String usuario, String tipoPonto);
}
