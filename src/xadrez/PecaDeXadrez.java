package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

/**
 * Adiciona informações específicas do xadrez a uma peça genérica.
 */
public abstract class PecaDeXadrez extends Peca {

    private final Cor cor;
    private int contagemMovimentos;

    protected PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public int getContagemMovimentos() {
        return contagemMovimentos;
    }

    public void incrementarMovimento() {
        contagemMovimentos++;
    }

    public void decrementarMovimento() {
        contagemMovimentos--;
    }

    public PosicaoXadrez getPosicaoXadrez() {
        return PosicaoXadrez.fromPosicao(posicao);
    }

    protected boolean existePecaAdversaria(Posicao posicao) {
        PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }
}
