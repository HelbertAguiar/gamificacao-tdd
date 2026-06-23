# Gamificação TDD

Projeto desenvolvido como exercício de **TDD (Test-Driven Development)** em Java, com foco em um componente simples de gamificação.

O sistema permite registrar pontos para usuários, consultar os pontos acumulados por tipo e gerar rankings. Os dados são persistidos em arquivo texto por meio da classe `Armazenamento`, enquanto a classe `Placar` concentra as operações principais da aplicação.

---

## Objetivo

Implementar, de forma incremental e guiada por testes, um componente capaz de:

- registrar pontos de diferentes tipos para usuários;
- recuperar a pontuação de um usuário em determinado tipo;
- listar todos os pontos de um usuário;
- gerar ranking por tipo de ponto;
- persistir os dados em arquivo;
- testar separadamente armazenamento, placar e integração entre as classes.

---

## Tecnologias utilizadas

- Java
- Maven
- JUnit
- IntelliJ IDEA

O projeto não utiliza Mockito. Os testes da classe `Placar` usam um mock manual implementado no próprio código de teste.

---

## Estrutura do projeto

```text
src
├── main
│   └── java
│       └── br/com/gamificacao
│           ├── Armazenamento.java
│           ├── Placar.java
│           ├── RankingItem.java
│           └── RepositorioPontos.java
└── test
    └── java
        └── br/com/gamificacao
            ├── ArmazenamentoTest.java
            ├── PlacarTest.java
            ├── PlacarArmazenamentoIntegracaoTest.java
            └── RepositorioDePontosMock.java
```

---

## Como o armazenamento funciona

A persistência foi feita usando um arquivo texto simples. Cada pontuação registrada é adicionada ao final do arquivo em uma nova linha, no formato:

```text
usuario;tipo;quantidade
```

Exemplo:

```text
guerra;estrela;10
guerra;estrela;15
fernandes;moeda;20
```

Essa abordagem é incremental: o sistema não sobrescreve registros anteriores. Para recuperar a pontuação total, o arquivo é lido, os registros são filtrados por usuário e tipo, e as quantidades encontradas são somadas.

---

## Principais classes

### `Armazenamento`

Responsável por gravar e recuperar os dados em arquivo.

Principais operações:

- registrar pontos;
- recuperar pontos de um usuário por tipo;
- listar usuários registrados;
- listar tipos de pontos registrados.

### `Placar`

Responsável pela regra principal do componente.

Principais operações:

- registrar pontos para um usuário;
- recuperar todos os pontos de um usuário;
- gerar ranking de usuários por tipo de ponto.

### `RankingItem`

Representa uma linha do ranking, contendo:

- nome do usuário;
- quantidade de pontos.

### `RepositorioPontos`

Interface usada para desacoplar o `Placar` da implementação concreta de armazenamento.

Isso permite testar o `Placar` com um mock manual, sem depender de arquivos reais.

---

## Como executar o projeto

### 1. Verificar se o Java está instalado

No terminal, execute:

```bash
java -version
```

Também é recomendável verificar o compilador:

```bash
javac -version
```

Se o projeto estiver configurado para Java 25, confirme se a versão instalada é compatível.

---

### 2. Verificar se o Maven está instalado

Execute:

```bash
mvn -version
```

O comando deve exibir a versão do Maven e a versão do Java usada por ele.

---

A pasta raiz deve conter o arquivo:

```text
pom.xml
```

---

## Como executar os testes

### Pelo terminal

Na raiz do projeto, execute:

```bash
mvn test
```

Esse comando compila o projeto e executa todos os testes automatizados.

Resultado esperado:

```text
BUILD SUCCESS
```

---

### Pelo IntelliJ IDEA

Também é possível executar os testes diretamente pela IDE:

1. abra a pasta `src/test/java`;
2. clique com o botão direito sobre a classe de teste desejada;
3. selecione **Run**.

Para executar todos os testes:

1. clique com o botão direito na pasta `test`;
2. selecione **Run 'All Tests'**.

---

## Tipos de teste

O projeto possui três grupos de teste.

### Testes de `Armazenamento`

Validam a persistência em arquivo real, usando diretórios temporários criados durante a execução dos testes.

Esses testes verificam se:

- os pontos são gravados;
- os pontos são recuperados corretamente;
- registros repetidos são somados;
- usuários e tipos diferentes não são misturados;
- todos os usuários são listados;
- todos os tipos de ponto são listados.

### Testes de `Placar`

Validam a regra de negócio do placar usando um mock manual.

Esses testes verificam se:

- o registro de pontos é delegado ao repositório;
- todos os pontos de um usuário são retornados;
- tipos sem pontuação não aparecem no resultado;
- o ranking é ordenado do maior para o menor;
- usuários sem pontos no tipo consultado não aparecem no ranking.

### Testes de integração

Validam `Placar` e `Armazenamento` funcionando juntos.

Esses testes usam arquivo real e verificam o fluxo completo:

```text
Placar → Armazenamento → Arquivo → Consulta → Resultado
```

---

## Exemplo de uso

```java
Path arquivo = Path.of("pontos.txt");

Armazenamento armazenamento = new Armazenamento(arquivo);
Placar placar = new Placar(armazenamento);

placar.registrarPontos("guerra", "estrela", 10);
placar.registrarPontos("guerra", "estrela", 15);
placar.registrarPontos("fernandes", "estrela", 20);

System.out.println(placar.recuperarPontosDoUsuario("guerra"));
System.out.println(placar.recuperarRanking("estrela"));
```

Saída esperada, de forma aproximada:

```text
{estrela=25}
[RankingItem[usuario=guerra, pontos=25], RankingItem[usuario=fernandes, pontos=20]]
```

---

## Observações sobre TDD

O desenvolvimento foi feito seguindo o ciclo:

```text
Red → Green → Refactor
```

Ou seja:

1. primeiro foi escrito um teste que falhava;
2. depois foi criada a implementação mínima para o teste passar;
3. por fim, o código foi refatorado mantendo todos os testes passando.

Essa abordagem ajudou a manter o código simples, coeso e diretamente ligado aos requisitos da atividade.

---

## Comandos úteis

Executar todos os testes:

```bash
mvn test
```

Limpar arquivos gerados pelo Maven:

```bash
mvn clean
```

Limpar e testar novamente:

```bash
mvn clean test
```

Compilar sem executar testes:

```bash
mvn compile
```

---

## Status

Requisitos principais atendidos:

- armazenamento em arquivo;
- recuperação de pontos por usuário e tipo;
- listagem de usuários;
- listagem de tipos de ponto;
- consulta de todos os pontos de um usuário;
- ranking por tipo de ponto;
- testes unitários;
- testes com mock manual;
- testes de integração.
