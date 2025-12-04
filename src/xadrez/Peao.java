package xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class Peao extends PecaDeXadrez {

    private final PartidaDeXadrez partida;

    public Peao(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partida) {
        super(tabuleiro, cor);
        this.partida = partida;
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        if (getCor() == Cor.BRANCA) {
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haPeca(p)
                && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().haPeca(p2)
                && getContagemMovimentos() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            if (posicao.getLinha() == 3) {
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(esquerda) && getTabuleiro().haPeca(esquerda)
                    && getTabuleiro().peca(esquerda) == partida.getVulneravelEnPassant()) {
                    mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
                }
                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(direita) && getTabuleiro().haPeca(direita)
                    && getTabuleiro().peca(direita) == partida.getVulneravelEnPassant()) {
                    mat[direita.getLinha() - 1][direita.getColuna()] = true;
                }
            }
        } else {
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().haPeca(p)
                && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().haPeca(p2)
                && getContagemMovimentos() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            if (posicao.getLinha() == 4) {
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(esquerda) && getTabuleiro().haPeca(esquerda)
                    && getTabuleiro().peca(esquerda) == partida.getVulneravelEnPassant()) {
                    mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
                }
                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(direita) && getTabuleiro().haPeca(direita)
                    && getTabuleiro().peca(direita) == partida.getVulneravelEnPassant()) {
                    mat[direita.getLinha() + 1][direita.getColuna()] = true;
                }
            }
        }

        return mat;
    }
}
