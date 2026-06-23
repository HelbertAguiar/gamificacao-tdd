package com.gamificacao.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class RepositorioDePontosMock implements RepositorioPontos {

    private final Map<String, Map<String, Integer>> dados = new HashMap<>();

    String ultimoUsuarioRegistrado;
    String ultimoTipoRegistrado;
    int ultimaQuantidadeRegistrada;

    @Override
    public void registrarPontos(String usuario, String tipo, int quantidade) {
        ultimoUsuarioRegistrado = usuario;
        ultimoTipoRegistrado = tipo;
        ultimaQuantidadeRegistrada = quantidade;

        dados.computeIfAbsent(usuario, chave -> new HashMap<>());
        dados.get(usuario).merge(tipo, quantidade, Integer::sum);
    }

    @Override
    public int recuperarPontos(String usuario, String tipo) {
        return dados.getOrDefault(usuario, Map.of())
                .getOrDefault(tipo, 0);
    }

    @Override
    public Set<String> recuperarUsuarios() {
        return dados.keySet();
    }

    @Override
    public Set<String> recuperarTiposDePonto() {
        Set<String> tipos = new HashSet<>();

        for (Map<String, Integer> pontosPorTipo : dados.values()) {
            tipos.addAll(pontosPorTipo.keySet());
        }

        return tipos;
    }
}