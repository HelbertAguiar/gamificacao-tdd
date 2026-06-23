package com.gamificacao.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlacarArmazenamentoIntegracaoTest {

    @TempDir
    Path diretorioTemporario;

    @Test
    void deveRegistrarEConsultarPontosUsandoArmazenamentoReal() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        Placar placar = new Placar(armazenamento);
        placar.registrarPontos("helbert", "moeda", 20);
        placar.registrarPontos("helbert", "estrela", 25);
        assertEquals(
            Map.of(
                "moeda", 20,
                "estrela", 25
            ),
            placar.recuperarPontosDoUsuario("helbert")
        );
    }

    @Test
    void deveGerarRankingUsandoArmazenamentoReal() {
        Path arquivo = diretorioTemporario.resolve("pontos.txt");
        Armazenamento armazenamento = new Armazenamento(arquivo);
        Placar placar = new Placar(armazenamento);
        placar.registrarPontos("rodrigo", "estrela", 17);
        placar.registrarPontos("helbert", "estrela", 25);
        placar.registrarPontos("fernandes", "estrela", 19);
        placar.registrarPontos("toco", "moeda", 100);

        assertEquals(
            List.of(
                new RankingItem("helbert", 25),
                new RankingItem("fernandes", 19),
                new RankingItem("rodrigo", 17)
            ),
            placar.recuperarRanking("estrela")
        );
    }

}
