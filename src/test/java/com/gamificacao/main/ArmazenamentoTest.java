package com.gamificacao.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Set;

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

    @Test
    void deveSomarPontosDoMesmoUsuarioNumCenarioMultiplosUsuariosMultiplasChamadas() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        int pontuacaoUsuarioPrimeiraChamada = 0;
        armazenamento.registrarPontos("ana", "ouro", 10);
        pontuacaoUsuarioPrimeiraChamada+=armazenamento.recuperarPontos("ana", "ouro");
        assertEquals(10, pontuacaoUsuarioPrimeiraChamada);
        int pontuacaoUsuarioSegundaChamada = 0;
        armazenamento.registrarPontos("joao", "ouro", 20);
        pontuacaoUsuarioSegundaChamada+=armazenamento.recuperarPontos("ana", "ouro");
        assertEquals(10, pontuacaoUsuarioSegundaChamada);
        int pontuacaoUsuarioTerceiraChamada = 0;
        armazenamento.registrarPontos("ana", "ouro", 20);
        pontuacaoUsuarioTerceiraChamada +=armazenamento.recuperarPontos("ana", "ouro");
        assertEquals(30, pontuacaoUsuarioTerceiraChamada);
        int pontuacaoUsuarioQuartaChamada = 0;
        armazenamento.registrarPontos("ana", "estrelas", 50);
        pontuacaoUsuarioQuartaChamada +=armazenamento.recuperarPontos("ana", "ouro");
        assertEquals(30, pontuacaoUsuarioQuartaChamada);
    }

    @Test
    void deveSomarSemMisturarUsuariosDistintos() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        armazenamento.registrarPontos("eduardo", "estrela", 10);
        armazenamento.registrarPontos("fernandes", "estrela", 25);
        assertEquals(10, armazenamento.recuperarPontos("eduardo", "estrela"));
        assertEquals(25, armazenamento.recuperarPontos("fernandes", "estrela"));
    }

    @Test
    void deveRecuperarTodosOsUsuariosQueReceberamPontos() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        armazenamento.registrarPontos("helbert", "estrela", 10);
        armazenamento.registrarPontos("helena", "moeda", 20);
        armazenamento.registrarPontos("marcos", "moeda", 5);
        Assertions.assertEquals(
            Set.of("helbert", "helena", "marcos"),
            armazenamento.recuperarUsuarios()
        );
    }

    @Test
    void deveRecuperarTodosOsTiposDePontoRegistrados() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        armazenamento.registrarPontos("helbert", "estrela", 10);
        armazenamento.registrarPontos("ana", "moeda", 20);
        armazenamento.registrarPontos("rodrigo", "curtida", 5);
        assertEquals(
                Set.of("estrela", "moeda", "curtida"),
                armazenamento.recuperarTiposDePonto()
        );
    }

}