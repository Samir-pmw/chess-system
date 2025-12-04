# ♞ Sistema de Xadrez em Camadas

Trabalho prático da disciplina de Programação Orientada a Objetos (POO). O repositório reúne todas as entregas solicitadas no enunciado oficial, seguindo uma abordagem de estudante: código comentado, divisões claras em camadas e documentação acessível.

---

## 📜 Enunciado Resumido

**Objetivo geral:** implementar um sistema de xadrez completo, capaz de conduzir partidas entre duas pessoas no console, respeitando as regras oficiais e explorando os conceitos centrais de POO.

### Diretrizes do enunciado

- Representar tabuleiro e peças usando matriz e composição de objetos.
- Criar classes específicas para cada peça (Rei, Rainha, Torre, Bispo, Cavalo e Peão) e para estruturas de suporte (tabuleiro, posição, exceções).
- Manter camadas separadas: `Board Layer` (estrutura) e `Chess Layer` (regras, movimentos, xeque/xeque-mate).
- Implementar movimentos especiais: roque, en passant e promoção.
- Utilizar enumeradores para cores e aplicar encapsulamento, herança e polimorfismo.
- Disponibilizar interface simples via console.
- Controlar o projeto com Git/GitHub, produzindo documentação, diagrama UML e relatório de decisões.

---

## 🎯 Objetivos do Sistema

- Gerenciar rodadas completas entre dois jogadores humanos.
- Validar movimentos, capturas, xeque e xeque-mate.
- Garantir mensagens claras em caso de erros ou jogadas inválidas.
- Facilitar manutenção com uma arquitetura em camadas bem definida.

---

## 🧱 Arquitetura em Camadas

```
Chess-Game-in-Java/
├── src/
│   ├── aplicacao/   -> Interface de texto (Program e UI)
│   ├── tabuleiro/   -> Estruturas genéricas (Tabuleiro, Peca, Posicao, exceções)
│   └── xadrez/      -> Regras do jogo, peças e PartidaDeXadrez
└── README.md        -> Documentação do trabalho
```

- **Board Layer (`tabuleiro`)**: controla tamanho do tabuleiro, alocação de peças, checagem de limites e estados.
- **Chess Layer (`xadrez`)**: conhece as regras, delega ações ao tabuleiro, verifica xeque/xeque-mate e expõe a visão usada pela interface.

---

## ⚙️ Requisitos Técnicos

- Java 17+.
- IDE ou editor com suporte a projetos Java (VS Code + Extension Pack for Java, IntelliJ, Eclipse, etc.).
- Git e GitHub para versionamento.
- Execução no console (sem interface gráfica nem IA adversária).

### Regras contempladas

- Movimentação regular das seis peças oficiais.
- Capturas, bloqueios e validações de origem/destino.
- Xeque, xeque-mate e prevenção de auto-xeque.
- Movimentos especiais: roque pequeno/grande, en passant e promoção automática para dama (com opção de troca).
- Controle de turno e indicação textual do jogador da vez.

### Fluxo de execução

1. Clone ou baixe o repositório.
2. Abra a pasta no VS Code (ou IDE de sua preferência) e configure um projeto Java simples.
3. Compile tudo a partir da raiz executando em um terminal PowerShell:

    ```powershell
    javac -d out (Get-ChildItem -Recurse src/*.java)
    java -cp out aplicacao.Program
    ```

    > Caso use VS Code com o Extension Pack for Java, basta clicar em "Run" sobre `aplicacao/Program.java`.
4. Observe o tabuleiro renderizado e acompanhe a partida pelo console.

---

## 📂 Documentação Entregue

- Código-fonte comentado e dividido por responsabilidade.
- README (este documento) com resumo do enunciado, arquitetura e instruções.
- Diagrama UML (ver pasta de anexos do trabalho ou documento entregue em sala).
- Relatório textual descrevendo decisões e regras implementadas.

---

## 👥 Equipe

- **Samir Batista dos Santos** — <https://github.com/Samir-pmw>
- **Brendo Henrique Soares Oliveira** — <https://github.com/JurRter>

---

## ✅ Status Atual

- Estrutura das camadas implementada e funcionando.
- Peças e movimentos básicos operacionais (renomeados para português para facilitar o entendimento em sala).
- Pronto para expansão com mais validações, interface interativa e registro de jogadas.

> Trabalho acadêmico sem fins comerciais. Licenciado sob MIT — consulte `LICENSE` para detalhes.

