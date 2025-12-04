package xadrez;

import tabuleiro.TabuleiroException;

/**
 * Exceção principal da camada de regras do xadrez.
 */
public class XadrezException extends TabuleiroException {

    public XadrezException(String mensagem) {
        super(mensagem);
    }
}
