package com.gamificacao.main;

import org.junit.jupiter.api.Assertions;
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
        int pontuacaoUsuario = armazenamento.recuperarPontos("helbert", "estrela");
        Assertions.assertEquals(0, pontuacaoUsuario);
    }

    @Test
    void deveRegistrarPontosUsuario() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        armazenamento.registrarPontos("helbert", "estrela", 34);
        int pontuacaoUsuario = armazenamento.recuperarPontos("helbert", "estrela");
        Assertions.assertEquals(34, pontuacaoUsuario);
    }

    @Test
    void deveSomarPontosDoMesmoUsuarioEMesmoTipoMultiplasChamadas() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        int pontuacaoUsuarioPrimeiraChamada = 0;
        armazenamento.registrarPontos("joao", "ouro", 10);
        pontuacaoUsuarioPrimeiraChamada+=armazenamento.recuperarPontos("joao", "ouro");
        assertEquals(10, pontuacaoUsuarioPrimeiraChamada);
        int pontuacaoUsuarioSegundaChamada = 0;
        armazenamento.registrarPontos("joao", "ouro", 20);
        pontuacaoUsuarioSegundaChamada+=armazenamento.recuperarPontos("joao", "ouro");
        assertEquals(30, pontuacaoUsuarioSegundaChamada);
    }

}