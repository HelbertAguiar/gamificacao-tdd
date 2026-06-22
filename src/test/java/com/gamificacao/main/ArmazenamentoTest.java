package com.gamificacao.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArmazenamentoTest {

    @TempDir
    Path diretorioTemporario;

    @Test
    void deveRetornarZeroQuandoUsuarioNaoPossuiPontosDoTipo() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);

        int pontos = armazenamento.recuperarPontos("helbert", "estrela");

        assertEquals(0, pontos);
    }
}