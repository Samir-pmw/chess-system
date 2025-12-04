package xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class Rei extends PecaDeXadrez {

    private final PartidaDeXadrez partida;

    public Rei(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partida) {
        super(tabuleiro, cor);
        this.partida = partida;
    }

    @Override
    public String toString() {
        return "R";
    }

    private boolean podeMover(Posicao posicao) {
        PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }

    private boolean testeTorreParaRoque(Posicao posicao) {
        PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
        return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContagemMovimentos() == 0;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        int[][] direcoes = {
            {-1, 0}, // acima
            {1, 0},  // abaixo
            {0, -1}, // esquerda
            {0, 1},  // direita
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
        };

        for (int[] dir : direcoes) {
            p.definirValores(posicao.getLinha() + dir[0], posicao.getColuna() + dir[1]);
            if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
        }

        if (getContagemMovimentos() == 0 && !partida.getXeque()) {
            Posicao posicaoTorreDireita = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if (getTabuleiro().posicaoExiste(posicaoTorreDireita) && testeTorreParaRoque(posicaoTorreDireita)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }

            Posicao posicaoTorreEsquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if (getTabuleiro().posicaoExiste(posicaoTorreEsquerda) && testeTorreParaRoque(posicaoTorreEsquerda)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }
            }
        }

        return mat;
    }
}
