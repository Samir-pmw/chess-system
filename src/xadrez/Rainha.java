package xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class Rainha extends PecaDeXadrez {

    public Rainha(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return "D";
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        int[][] direcoes = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] direcao : direcoes) {
            p.definirValores(posicao.getLinha() + direcao[0], posicao.getColuna() + direcao[1]);
            while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
                p.definirValores(p.getLinha() + direcao[0], p.getColuna() + direcao[1]);
            }
            if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
        }

        return mat;
    }
}
