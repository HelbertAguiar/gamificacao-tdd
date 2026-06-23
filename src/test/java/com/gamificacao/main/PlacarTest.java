package com.gamificacao.main;

import org.junit.jupiter.api.Test;
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
}