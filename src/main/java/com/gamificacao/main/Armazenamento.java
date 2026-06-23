package com.gamificacao.main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Set;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Armazenamento implements RepositorioPontos {

    private final Path arquivo;

    public Armazenamento(Path arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public Set<String> recuperarUsuarios() {
        if (!Files.exists(arquivo)) {
            return Set.of();
        }

        try {
            return Files.readAllLines(arquivo, StandardCharsets.UTF_8)
                    .stream()
                    .map(linha -> linha.split(";"))
                    .map(partes -> partes[0])
                    .collect(java.util.stream.Collectors.toSet());
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler usuários do arquivo.", e);
        }
    }

    @Override
    public void registrarPontos(String usuario, String tipoPonto, int pontuacao) {
        String linha = usuario + ";" + tipoPonto + ";" + pontuacao + System.lineSeparator();
        try {
            Files.writeString(arquivo, linha, StandardCharsets.UTF_8, CREATE, APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao gravar pontos no arquivo.", e);
        }
    }

    @Override
    public int recuperarPontos(String usuario, String tipo) {
        if (!Files.exists(arquivo)) {
            return 0;
        }

        try {
            return Files.readAllLines(arquivo, StandardCharsets.UTF_8)
                    .stream()
                    .map(linha -> linha.split(";"))
                    .filter(partes -> partes[0].equals(usuario))
                    .filter(partes -> partes[1].equals(tipo))
                    .mapToInt(partes -> Integer.parseInt(partes[2]))
                    .sum();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler pontos do arquivo.", e);
        }
    }
}
