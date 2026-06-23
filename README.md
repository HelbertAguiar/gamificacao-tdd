# Análise do Projeto `gamificacao-tdd`

## Parte 1 — Cumprimento dos Requisitos

### 1.1 Classe `Armazenamento`

#### Requisito: Armazenar que um usuário recebeu uma quantidade de um tipo de ponto.
**✅ Atendido.** O método `registrarPontos(String usuario, String tipo, int quantidade)` grava uma linha no formato `usuario;tipo;quantidade` no arquivo via `Files.writeString(arquivo, linha, UTF_8, CREATE, APPEND)`. A opção `APPEND` garante a abordagem incremental: cada registro é acrescentado ao final do arquivo, sem sobrescrever dados anteriores.

```java
// Armazenamento.java – linha 52
String linha = usuario + ";" + tipo + ";" + quantidade + System.lineSeparator();
Files.writeString(arquivo, linha, StandardCharsets.UTF_8, CREATE, APPEND);
```

---

#### Requisito: Recuperar quantos pontos de um tipo tem um usuário.
**✅ Atendido.** O método `recuperarPontos(String usuario, String tipo)` lê todas as linhas do arquivo (via `lerRegistros()`), filtra pelo usuário e pelo tipo e soma todas as ocorrências. Isso garante correta acumulação mesmo com múltiplos registros incrementais.

```java
// Armazenamento.java – linhas 29-36
return lerRegistros().stream()
    .filter(partes -> partes[0].equals(usuario))
    .filter(partes -> partes[1].equals(tipo))
    .mapToInt(partes -> Integer.parseInt(partes[2]))
    .sum();
```

---

#### Requisito: Retornar todos os usuários que já receberam algum tipo de ponto.
**✅ Atendido.** O método `recuperarUsuarios()` extrai o primeiro campo de cada linha do arquivo e os agrega em um `Set<String>`, eliminando duplicatas naturalmente.

---

#### Requisito: Retornar todos os tipos de ponto que já foram registrados para algum usuário.
**✅ Atendido.** O método `recuperarTiposDePonto()` extrai o segundo campo de cada linha e os agrega em um `Set<String>`.

---

#### Requisito: Os dados devem ser armazenados em arquivo.
**✅ Atendido.** A classe usa exclusivamente `java.nio.file.Files` para leitura e escrita. O formato CSV simples (`usuario;tipo;quantidade`, uma entrada por linha) é transparente ao restante do sistema.

---

#### Requisito: Se a aplicação cair, deve-se recuperar todos os dados armazenados.
**✅ Atendido.** Não há cache em memória; cada leitura percorre o arquivo completo (`lerRegistros()`). Portanto, em caso de queda e reinício, todos os dados persistidos no arquivo são lidos corretamente.

---

#### Requisito: Validação de quantidade positiva.
**✅ Atendido** (além do enunciado base, mas demonstrado em teste). O método `registrarPontos` lança `IllegalArgumentException` quando `quantidade <= 0`, protegendo a integridade dos dados.

---

### 1.2 Classe `Placar`

#### Requisito: Registrar um tipo de ponto para um usuário.
**✅ Atendido.** O método `registrarPontos` simplesmente delega para `repositorio.registrarPontos`, cumprindo o princípio de que `Placar` coordena e `Armazenamento` persiste.

---

#### Requisito: Retornar todos os pontos de um usuário (sem incluir tipos com valor zero).
**✅ Atendido.** O método `recuperarPontosDoUsuario` itera sobre todos os tipos registrados no repositório, consulta a pontuação de cada um e inclui no mapa de retorno apenas aqueles com `quantidade > 0`. Usuário sem nenhum ponto de determinado tipo não recebe essa entrada no resultado.

```java
// Placar.java – linhas 21-31
for (String tipo : repositorio.recuperarTiposDePonto()) {
    int quantidade = repositorio.recuperarPontos(usuario, tipo);
    if (quantidade > 0) {
        pontos.put(tipo, quantidade);
    }
}
```

---

#### Requisito: Retornar ranking de um tipo de ponto, ordenado do maior para o menor, excluindo usuários sem pontos daquele tipo.
**✅ Atendido.** O método `recuperarRanking` mapeia cada usuário para um `RankingItem`, filtra os que possuem zero pontos e ordena em ordem decrescente.

```java
// Placar.java – linhas 35-40
return repositorio.recuperarUsuarios().stream()
    .map(usuario -> new RankingItem(usuario, repositorio.recuperarPontos(usuario, tipo)))
    .filter(item -> item.pontos() > 0)
    .sorted(Comparator.comparingInt(RankingItem::pontos).reversed())
    .toList();
```

---

### 1.3 Estrutura e Responsabilidades

#### Requisito: `Placar` é composta por uma instância de `Armazenamento` (ou de `RepositorioPontos`), a quem delega armazenamento e recuperação.
**✅ Atendido.** `Placar` recebe no construtor uma instância de `RepositorioPontos` (interface), que pode ser `Armazenamento` (produção) ou `RepositorioDePontosMock` (testes). Isso aplica corretamente **injeção de dependência** e **inversão de dependência**.

---

#### Requisito: Nenhum método público além de construtores e métodos de acesso nas classes `Armazenamento` e `Placar`.
**✅ Atendido.**
- `Armazenamento`: construtor + 4 métodos públicos que implementam a interface `RepositorioPontos`. O método auxiliar `lerRegistros()` é `private`.
- `Placar`: construtor + 3 métodos públicos definidos pelo enunciado. Nenhum método extra público foi adicionado.

---

#### Requisito: Outras classes não devem depender de como o armazenamento é feito no arquivo.
**✅ Atendido.** A interface `RepositorioPontos` abstrai completamente o mecanismo de persistência. `Placar` depende apenas da interface, nunca de `Armazenamento` diretamente. Os testes de `Placar` jamais tocam em arquivo — usam exclusivamente o mock em memória.

---

### 1.4 Testes

#### Requisito: Testes da classe `Armazenamento` devem usar arquivos reais.
**✅ Atendido.** `ArmazenamentoTest` usa a anotação `@TempDir` do JUnit 5, que cria um diretório temporário real no sistema de arquivos para cada teste, garantindo isolamento e limpeza automática.

---

#### Requisito: Testes da classe `Placar` devem usar mock object para `Armazenamento`.
**✅ Atendido.** `PlacarTest` usa exclusivamente `RepositorioDePontosMock`, uma implementação em memória de `RepositorioPontos`. O mock também expõe os campos `ultimoUsuarioRegistrado`, `ultimoTipoRegistrado` e `ultimaQuantidadeRegistrada` para verificar que `Placar` delega corretamente ao repositório — característica clássica de um mock object.

---

#### Requisito: Devem ser criados testes de integração incluindo as duas classes.
**✅ Atendido.** A classe `PlacarArmazenamentoIntegracaoTest` instancia `Armazenamento` real (com `@TempDir`) e `Placar` real, exercitando o fluxo completo de ponta a ponta: registro de pontos, consulta por usuário e geração de ranking.

---

### 1.5 Classes Auxiliares

| Classe/Interface | Papel | Avaliação |
|---|---|---|
| `RepositorioPontos` (interface) | Contrato entre `Placar` e a camada de persistência | ✅ Permite substituição por qualquer implementação |
| `RankingItem` (record) | Value object imutável para item de ranking | ✅ Uso adequado de Java record; elimina boilerplate |
| `RepositorioDePontosMock` | Implementação em memória para testes de `Placar` | ✅ Implementa a interface corretamente |

---

## Parte 2 — Ciclos TDD Identificados pelos Commits

O histórico de 23 commits evidencia a aplicação do ciclo **Red → Green → Refactor** ao longo de todo o desenvolvimento.

---

### Ciclo 1 — Bootstrap e primeiros testes de `Armazenamento`

| Commit | Hash | Fase TDD |
|---|---|---|
| `add initial project setup with .gitignore, pom.xml, and ArmazenamentoTest class` | `78ac9e1` | 🔴 RED |
| `add Armazenamento class and RepositorioPontos interface with basic methods` | `1ccce1b` | 🟢 GREEN |
| `add tests for Armazenamento class to validate point retrieval and registration` | `5d2a8cb` | 🔴 RED |
| `implement point registration and retrieval in Armazenamento class` | `f202285` | 🟢 GREEN |

**Descrição:** O projeto começa com a estrutura mínima e os primeiros dois testes do `Armazenamento` — um que verifica retorno zero para usuário sem pontos e outro que registra e recupera pontos. A classe esqueleto é criada para os testes compilarem (RED), depois a implementação real os faz passar (GREEN).

---

### Ciclo 2 — Acumulação de pontos

| Commit | Hash | Fase TDD |
|---|---|---|
| `add test to validate point accumulation for the same user and type` | `7c44703` | 🔴 RED |
| `add test to validate point accumulation for multiple users and calls` | `2e8a6dc` | 🔴 RED |
| `add test to validate point accumulation for distinct users` | `a2cee71` | 🔴 RED |
| `add test to validate retrieval of all users who received points` | `4e7e708` | 🔴 RED |
| `implement user retrieval method in Armazenamento class` | `d51d220` | 🟢 GREEN |
| `refactor` | `5be6a59` | 🔄 REFACTOR |

**Descrição:** Três testes RED são adicionados em sequência, explorando cenários progressivamente mais complexos de acumulação: um usuário, múltiplos usuários em sequência e usuários distintos sem mistura de pontos. Em seguida, o teste de recuperação de todos os usuários é adicionado. Um único commit de implementação satisfaz todos os casos. O commit `refactor` consolida o código — provavelmente extraindo o método `lerRegistros()` como método privado reutilizável.

---

### Ciclo 3 — Recuperação de tipos de ponto

| Commit | Hash | Fase TDD |
|---|---|---|
| `add test to validate retrieval of all registered point types` | `2015ca2` | 🔴 RED |
| `add method to retrieve all registered point types` | `517cf02` | 🟢 GREEN |

**Descrição:** Ciclo curto e preciso. Primeiro o teste `deveRecuperarTodosOsTiposDePontoRegistrados`, depois a implementação de `recuperarTiposDePonto()` mapeando o segundo campo de cada registro.

---

### Ciclo 4 — Validação de entrada

| Commit | Hash | Fase TDD |
|---|---|---|
| `add test to validate that registering points with zero or negative quantity throws an exception` | `b2ebc4f` | 🔴 RED |
| `add validation to ensure positive quantity when registering points` | `1e570eb` | 🟢 GREEN |

**Descrição:** Ciclo clássico de validação. O teste `naoDeveRegistrarQuantidadeMenorOuIgualAZero` usa `assertThrows` para especificar o comportamento esperado antes da guarda ser inserida no método `registrarPontos`.

---

### Ciclo 5 — Classe `Placar` e delegação ao repositório

| Commit | Hash | Fase TDD |
|---|---|---|
| `add Placar class and corresponding test for point registration` | `1c757bb` | 🔴 RED |
| `add RepositorioDePontosMock class to manage point registration and retrieval` | `b1086a5` | 🔴 RED (infraestrutura) |
| `add Placar class constructor and method to register points` | `3c26b13` | 🟢 GREEN |

**Descrição:** Inicia o desenvolvimento da classe `Placar`. O teste `deveRegistrarPontosDelegandoParaORepositorio` verifica que `Placar` delega ao repositório com os valores corretos. A criação do `RepositorioDePontosMock` é necessária para que o teste compile e rode — é a infraestrutura de teste sendo construída antes do código de produção, prática consistente com TDD.

---

### Ciclo 6 — Recuperação de pontos do usuário em `Placar`

| Commit | Hash | Fase TDD |
|---|---|---|
| `add test to retrieve all points for a user in Placar class` | `8dbc447` | 🔴 RED |
| `add method to retrieve points for a user in Placar class` | `dab5395` | 🟢 GREEN |
| `add tests for point retrieval and ranking in Placar class` | `a4dab52` | 🔴 RED |

**Descrição:** O teste `deveRetornarTodosOsPontosDeUmUsuario` força a criação de `recuperarPontosDoUsuario`. O commit `a4dab52` adiciona o teste complementar `naoDeveRetornarTipoDePontoComValorZeroParaUsuario`, assegurando que tipos sem pontos sejam filtrados.

---

### Ciclo 7 — Ranking

| Commit | Hash | Fase TDD |
|---|---|---|
| `add tests for point retrieval and ranking in Placar class` | `a4dab52` | 🔴 RED |
| `add method to retrieve ranking and create RankingItem class` | `be43710` | 🟢 GREEN |
| `add test to ensure ranking does not include users without points of that type` | `b3b88df` | 🔴 RED → 🟢 GREEN |

**Descrição:** O último grande ciclo. O teste `deveRetornarRankingDeUmTipoDePontoEmOrdemDecrescente` especifica a ordenação. Para implementá-lo, o record `RankingItem` e o método `recuperarRanking` são criados. O commit final adiciona o teste `rankingNaoDeveIncluirUsuarioSemPontosDaqueleTipo`, que refina o comportamento do filtro `item.pontos() > 0` — o qual provavelmente já existia na implementação, tornando este um ciclo RED instantaneamente GREEN (característica de boa implementação antecipada).

---

## Resumo

| Aspecto | Resultado |
|---|---|
| Requisitos de implementação | ✅ Todos atendidos |
| Uso correto de Mock Objects | ✅ `RepositorioDePontosMock` isola `Placar` da persistência |
| Divisão de responsabilidades | ✅ `Armazenamento` persiste, `Placar` coordena, `RepositorioPontos` abstrai |
| Testes com arquivo real | ✅ `ArmazenamentoTest` com `@TempDir` |
| Testes de integração | ✅ `PlacarArmazenamentoIntegracaoTest` |
| Ciclos TDD documentados por commits | ✅ 7 ciclos Red→Green→Refactor identificados |
| Organização do código | ✅ Separação clara em pacotes, nomes expressivos, uso de `record`, streams e interface |

