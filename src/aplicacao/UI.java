package aplicacao;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoXadrez;

/**
 * Responsável pela interação simples via console.
 */
public class UI {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_PRETO = "\u001B[30m";
    private static final String ANSI_VERMELHO = "\u001B[31m";
    private static final String ANSI_VERDE = "\u001B[32m";
    private static final String ANSI_AMARELO = "\u001B[33m";
    private static final String ANSI_AZUL = "\u001B[34m";
    private static final String ANSI_BRANCO = "\u001B[37m";
    private static final String BG_CLARO = "\u001B[47m";
    private static final String BG_ESCURO = "\u001B[100m";
    private static final String BG_DESTAQUE = "\u001B[42m";

    private UI() {
    }

    // Limpa o console em Windows e terminais compatíveis com ANSI.
    public static void limparTela() {
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static PosicaoXadrez lerPosicaoXadrez(Scanner sc) {
        try {
            String s = sc.nextLine();
            char coluna = s.charAt(0);
            int linha = Integer.parseInt(s.substring(1));
            return new PosicaoXadrez(coluna, linha);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Erro ao ler posição de xadrez. Valores válidos vão de a1 até h8.");
        }
    }

    public static void imprimirPartida(PartidaDeXadrez partida, List<PecaDeXadrez> capturadas) {
        imprimirTabuleiro(partida.getPecas());
        System.out.println();
        imprimirPecasCapturadas(capturadas);
        System.out.println("\nTurno: " + partida.getTurno());
        if (!partida.getXequeMate()) {
            System.out.println("Aguardando jogador: " + partida.getJogadorAtual());
            if (partida.getXeque()) {
                System.out.println(ANSI_VERMELHO + "XEQUE!" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_VERMELHO + "XEQUE-MATE!" + ANSI_RESET);
            System.out.println("Vencedor: " + partida.getJogadorAtual());
        }
    }

    public static void imprimirTabuleiro(PecaDeXadrez[][] pecas) {
        imprimirTabuleiroFormatado(pecas, null);
    }

    public static void imprimirTabuleiro(PecaDeXadrez[][] pecas, boolean[][] movimentosPossiveis) {
        imprimirTabuleiroFormatado(pecas, movimentosPossiveis);
    }

    // Monta uma moldura colorida ao redor do tabuleiro para facilitar a leitura.
    private static void imprimirTabuleiroFormatado(PecaDeXadrez[][] pecas, boolean[][] movimentosPossiveis) {
        imprimirCabecalhoColunas();
        imprimirBordaHorizontal();
        for (int i = 0; i < pecas.length; i++) {
            imprimirLinha(pecas, i, movimentosPossiveis);
        }
        imprimirCabecalhoColunas();
    }

    private static void imprimirLinha(PecaDeXadrez[][] pecas, int linha, boolean[][] movimentosPossiveis) {
        System.out.print(" " + (8 - linha) + " |");
        for (int coluna = 0; coluna < pecas[linha].length; coluna++) {
            boolean destaque = movimentosPossiveis != null && movimentosPossiveis[linha][coluna];
            imprimirCelula(pecas[linha][coluna], destaque, linha, coluna);
        }
        System.out.println(" " + (8 - linha));
        imprimirBordaHorizontal();
    }

    private static void imprimirCelula(PecaDeXadrez peca, boolean destaque, int linha, int coluna) {
        boolean casaEscura = (linha + coluna) % 2 != 0;
        String fundo = destaque ? BG_DESTAQUE : (casaEscura ? BG_ESCURO : BG_CLARO);
        String cor = ANSI_PRETO;
        String simbolo = ".";
        if (peca != null) {
            cor = (peca.getCor() == Cor.BRANCA) ? ANSI_BRANCO : ANSI_AMARELO;
            simbolo = peca.toString();
        }
        String conteudo = String.format(" %-2s", simbolo);
        System.out.print(fundo + cor + conteudo + ANSI_RESET + "|");
    }

    private static void imprimirCabecalhoColunas() {
        System.out.print("    ");
        for (char c = 'a'; c <= 'h'; c++) {
            System.out.print(" " + ANSI_AZUL + c + ANSI_RESET + "  ");
        }
        System.out.println();
    }

    private static void imprimirBordaHorizontal() {
        System.out.print("   +");
        for (int i = 0; i < 8; i++) {
            System.out.print("---+");
        }
        System.out.println();
    }

    private static void imprimirPecasCapturadas(List<PecaDeXadrez> capturadas) {
        List<PecaDeXadrez> brancas = capturadas.stream().filter(x -> x.getCor() == Cor.BRANCA).toList();
        List<PecaDeXadrez> pretas = capturadas.stream().filter(x -> x.getCor() == Cor.PRETA).toList();
        System.out.println("Peças capturadas:");
        System.out.print("Brancas: " + ANSI_BRANCO);
        System.out.print(brancas);
        System.out.println(ANSI_RESET);
        System.out.print("Pretas: " + ANSI_AMARELO);
        System.out.print(pretas);
        System.out.println(ANSI_RESET);
    }
}
