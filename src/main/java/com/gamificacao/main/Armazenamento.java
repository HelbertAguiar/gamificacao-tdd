package com.gamificacao.main;

import java.nio.file.Path;

public class Armazenamento implements RepositorioPontos {

    private final Path arquivo;

    public Armazenamento(Path arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public void registrarPontos(String usuario, String tipoPonto, int pontuacao) {
        return;
    }

    @Override
    public int recuperarPontos(String usuario, String tipoPonto) {
        return 0;
    }
}
