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
        return lerRegistros()
                .stream()
                .map(partes -> partes[0])
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public int recuperarPontos(String usuario, String tipo) {
        return lerRegistros()
                .stream()
                .filter(partes -> partes[0].equals(usuario))
                .filter(partes -> partes[1].equals(tipo))
                .mapToInt(partes -> Integer.parseInt(partes[2]))
                .sum();
    }

    @Override
    public Set<String> recuperarTiposDePonto() {
        return lerRegistros()
                .stream()
                .map(partes -> partes[1])
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public void registrarPontos(String usuario, String tipo, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de pontos deve ser positiva.");
        }

        String linha = usuario + ";" + tipo + ";" + quantidade + System.lineSeparator();

        try {
            Files.writeString(arquivo, linha, StandardCharsets.UTF_8, CREATE, APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao gravar pontos no arquivo.", e);
        }
    }

    private java.util.List<String[]> lerRegistros() {
        if (!Files.exists(arquivo)) {
            return java.util.List.of();
        }

        try {
            return Files.readAllLines(arquivo, StandardCharsets.UTF_8)
                    .stream()
                    .map(linha -> linha.split(";"))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler arquivo de pontos.", e);
        }
    }

}
