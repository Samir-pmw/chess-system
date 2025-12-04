package tabuleiro;

/**
 * Controla o grid de peças e valida posições do tabuleiro.
 */
public class Tabuleiro {

    private final int linhas;
    private final int colunas;
    private final Peca[][] pecas;

    public Tabuleiro(int linhas, int colunas) {
        if (linhas < 1 || colunas < 1) {
            throw new TabuleiroException("Erro ao criar tabuleiro: deve haver pelo menos 1 linha e 1 coluna.");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        this.pecas = new Peca[linhas][colunas];
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public Peca peca(int linha, int coluna) {
        if (!posicaoExiste(linha, coluna)) {
            throw new TabuleiroException("Posição fora dos limites do tabuleiro.");
        }
        return pecas[linha][coluna];
    }

    public Peca peca(Posicao posicao) {
        return peca(posicao.getLinha(), posicao.getColuna());
    }

    public void colocarPeca(Peca peca, Posicao posicao) {
        if (haPeca(posicao)) {
            throw new TabuleiroException("Já existe uma peça na posição " + posicao + ".");
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;
        peca.posicao = posicao;
    }

    public Peca retirarPeca(Posicao posicao) {
        if (!posicaoExiste(posicao)) {
            throw new TabuleiroException("Posição inexistente no tabuleiro.");
        }
        if (peca(posicao) == null) {
            return null;
        }
        Peca aux = pecas[posicao.getLinha()][posicao.getColuna()];
        pecas[posicao.getLinha()][posicao.getColuna()] = null;
        aux.posicao = null;
        return aux;
    }

    public boolean posicaoExiste(Posicao posicao) {
        return posicaoExiste(posicao.getLinha(), posicao.getColuna());
    }

    private boolean posicaoExiste(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    public boolean haPeca(Posicao posicao) {
        if (!posicaoExiste(posicao)) {
            throw new TabuleiroException("Posição fora dos limites do tabuleiro.");
        }
        return peca(posicao) != null;
    }
}
