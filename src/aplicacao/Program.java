package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezException;

/**
 * Ponto de entrada do jogo. Executa o loop principal da partida.
 */
public class Program {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PartidaDeXadrez partida = new PartidaDeXadrez();
        List<PecaDeXadrez> capturadas = new ArrayList<>();

        while (!partida.getXequeMate()) {
            try {
                UI.limparTela();
                UI.imprimirPartida(partida, capturadas);
                System.out.println();
                System.out.print("Origem: ");
                PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);

                boolean[][] movimentosPossiveis = partida.movimentosPossiveis(origem);
                UI.imprimirTabuleiro(partida.getPecas(), movimentosPossiveis);

                System.out.println();
                System.out.print("Destino: ");
                PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);

                PecaDeXadrez pecaCapturada = partida.executarMovimentoXadrez(origem, destino);

                if (pecaCapturada != null) {
                    capturadas.add(pecaCapturada);
                }

                if (partida.getPromovida() != null) {
                    System.out.print("Informe para qual peça promover (D/T/B/C): ");
                    String tipo = sc.nextLine().toUpperCase();
                    partida.substituirPecaPromovida(tipo);
                }

            } catch (XadrezException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }

        UI.limparTela();
        UI.imprimirPartida(partida, capturadas);
        sc.close();
    }
}
