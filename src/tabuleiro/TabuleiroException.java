package tabuleiro;

/**
 * Exceção específica para erros relacionados ao tabuleiro.
 */
public class TabuleiroException extends RuntimeException {

    public TabuleiroException(String mensagem) {
        super(mensagem);
    }
}
