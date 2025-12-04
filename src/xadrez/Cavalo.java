package xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class Cavalo extends PecaDeXadrez {

    public Cavalo(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return "C";
    }

    private boolean podeMover(Posicao posicao) {
        PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        int[][] saltos = {
            {-2, -1}, {-2, 1},
            {-1, -2}, {-1, 2},
            {1, -2}, {1, 2},
            {2, -1}, {2, 1}
        };

        Posicao p = new Posicao(0, 0);
        for (int[] salto : saltos) {
            p.definirValores(posicao.getLinha() + salto[0], posicao.getColuna() + salto[1]);
            if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
        }

        return mat;
    }
}
