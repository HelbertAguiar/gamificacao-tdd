package com.gamificacao.main;

import java.util.Set;

public interface RepositorioPontos {
    void registrarPontos(String usuario, String tipoPonto, int pontuacao);
    int recuperarPontos(String usuario, String tipoPonto);
    Set<String> recuperarUsuarios();
}
