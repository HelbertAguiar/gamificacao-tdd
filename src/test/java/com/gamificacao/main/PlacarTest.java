package com.gamificacao.main;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlacarTest {

    @Test
    void deveRegistrarPontosDelegandoParaORepositorio() {
        RepositorioDePontosMock repositorio = new RepositorioDePontosMock();
        Placar placar = new Placar(repositorio);
        placar.registrarPontos("helbert", "estrela", 10);
        assertEquals("helbert", repositorio.ultimoUsuarioRegistrado);
        assertEquals("estrela", repositorio.ultimoTipoRegistrado);
        assertEquals(10, repositorio.ultimaQuantidadeRegistrada);
    }

    @Test
    void deveRetornarTodosOsPontosDeUmUsuario() {
        RepositorioDePontosMock repositorio = new RepositorioDePontosMock();
        Placar placar = new Placar(repositorio);
        repositorio.registrarPontos("helbert", "moeda", 20);
        repositorio.registrarPontos("helbert", "estrela", 25);
        repositorio.registrarPontos("ana", "moeda", 100);
        Map<String, Integer> pontos = placar.recuperarPontosDoUsuario("helbert");
        assertEquals(
            Map.of(
                    "moeda", 20,
                    "estrela", 25
            ),
            pontos
        );
    }

}