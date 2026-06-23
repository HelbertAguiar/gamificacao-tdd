package com.gamificacao.main;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @Test
    void naoDeveRetornarTipoDePontoComValorZeroParaUsuario() {
        RepositorioDePontosMock repositorio = new RepositorioDePontosMock();
        Placar placar = new Placar(repositorio);
        repositorio.registrarPontos("helbert", "estrela", 25);
        repositorio.registrarPontos("fernandes", "energia", 10);
        Map<String, Integer> pontos = placar.recuperarPontosDoUsuario("helbert");
        assertFalse(pontos.containsKey("energia"));
    }

    @Test
    void deveRetornarRankingDeUmTipoDePontoEmOrdemDecrescente() {
        RepositorioDePontosMock repositorio = new RepositorioDePontosMock();
        Placar placar = new Placar(repositorio);
        repositorio.registrarPontos("rodrigo", "estrela", 17);
        repositorio.registrarPontos("helbert", "estrela", 25);
        repositorio.registrarPontos("fernandes", "estrela", 19);
        List<RankingItem> ranking = placar.recuperarRanking("estrela");
        assertEquals(
            List.of(
                new RankingItem("helbert", 25),
                new RankingItem("fernandes", 19),
                new RankingItem("rodrigo", 17)
            ),
            ranking
        );
    }

}