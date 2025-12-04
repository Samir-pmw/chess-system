package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

/**
 * Coordena toda a lógica de uma partida de xadrez.
 */
public class PartidaDeXadrez {

    private int turno;
    private Cor jogadorAtual;
    private boolean xeque;
    private boolean xequeMate;
    private PecaDeXadrez vulneravelEnPassant;
    private PecaDeXadrez promovida;

    private final Tabuleiro tabuleiro;
    private final List<Peca> pecasTabuleiro = new ArrayList<>();
    private final List<Peca> pecasCapturadas = new ArrayList<>();

    public PartidaDeXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        jogadorAtual = Cor.BRANCA;
        iniciarPartida();
    }

    public int getTurno() {
        return turno;
    }

    public Cor getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean getXeque() {
        return xeque;
    }

    public boolean getXequeMate() {
        return xequeMate;
    }

    public PecaDeXadrez getVulneravelEnPassant() {
        return vulneravelEnPassant;
    }

    public PecaDeXadrez getPromovida() {
        return promovida;
    }

    public PecaDeXadrez[][] getPecas() {
        PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    public boolean[][] movimentosPossiveis(PosicaoXadrez origemPosicao) {
        Posicao posicao = origemPosicao.toPosicao();
        validarPosicaoDeOrigem(posicao);
        return tabuleiro.peca(posicao).movimentosPossiveis();
    }

    public PecaDeXadrez executarMovimentoXadrez(PosicaoXadrez origemPosicao, PosicaoXadrez destinoPosicao) {
        Posicao origem = origemPosicao.toPosicao();
        Posicao destino = destinoPosicao.toPosicao();
        validarPosicaoDeOrigem(origem);
        validarPosicaoDeDestino(origem, destino);
        Peca pecaCapturada = mover(origem, destino);

        if (testarXeque(jogadorAtual)) {
            desfazerMovimento(origem, destino, pecaCapturada);
            throw new XadrezException("Você não pode se colocar em xeque!");
        }

        PecaDeXadrez peçaMovida = (PecaDeXadrez) tabuleiro.peca(destino);

        promovida = null;
        if (peçaMovida instanceof Peao) {
            if ((peçaMovida.getCor() == Cor.BRANCA && destino.getLinha() == 0)
                || (peçaMovida.getCor() == Cor.PRETA && destino.getLinha() == 7)) {
                promovida = (PecaDeXadrez) tabuleiro.peca(destino);
                promovida = substituirPecaPromovida("D");
            }
        }

        xeque = testarXeque(oponente(jogadorAtual));

        if (testarXequeMate(oponente(jogadorAtual))) {
            xequeMate = true;
        } else {
            proximoTurno();
        }

        if (peçaMovida instanceof Peao
            && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
            vulneravelEnPassant = peçaMovida;
        } else {
            vulneravelEnPassant = null;
        }

        return (PecaDeXadrez) pecaCapturada;
    }

    public PecaDeXadrez substituirPecaPromovida(String tipo) {
        if (promovida == null) {
            throw new IllegalStateException("Não há peça para ser promovida.");
        }
        if (!tipo.equals("D") && !tipo.equals("T") && !tipo.equals("B") && !tipo.equals("C")) {
            return promovida;
        }
        Posicao pos = promovida.getPosicao();
        Peca p = tabuleiro.retirarPeca(pos);
        pecasTabuleiro.remove(p);

        PecaDeXadrez novaPeca = novaPeca(tipo, promovida.getCor());
        tabuleiro.colocarPeca(novaPeca, pos);
        pecasTabuleiro.add(novaPeca);

        return novaPeca;
    }

    private PecaDeXadrez novaPeca(String tipo, Cor cor) {
        return switch (tipo) {
            case "D" -> new Rainha(tabuleiro, cor);
            case "T" -> new Torre(tabuleiro, cor);
            case "B" -> new Bispo(tabuleiro, cor);
            default -> new Cavalo(tabuleiro, cor);
        };
    }

    private Peca mover(Posicao origem, Posicao destino) {
        PecaDeXadrez p = (PecaDeXadrez) tabuleiro.retirarPeca(origem);
        p.incrementarMovimento();
        Peca pecaCapturada = tabuleiro.retirarPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if (pecaCapturada != null) {
            pecasTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.retirarPeca(origemT);
            tabuleiro.colocarPeca(torre, destinoT);
            torre.incrementarMovimento();
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.retirarPeca(origemT);
            tabuleiro.colocarPeca(torre, destinoT);
            torre.incrementarMovimento();
        }

        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
                Posicao posicaoPeao;
                if (p.getCor() == Cor.BRANCA) {
                    posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                } else {
                    posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tabuleiro.retirarPeca(posicaoPeao);
                pecasCapturadas.add(pecaCapturada);
                pecasTabuleiro.remove(pecaCapturada);
            }
        }

        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
        PecaDeXadrez p = (PecaDeXadrez) tabuleiro.retirarPeca(destino);
        p.decrementarMovimento();
        tabuleiro.colocarPeca(p, origem);

        if (pecaCapturada != null) {
            tabuleiro.colocarPeca(pecaCapturada, destino);
            pecasCapturadas.remove(pecaCapturada);
            pecasTabuleiro.add(pecaCapturada);
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.retirarPeca(destinoT);
            tabuleiro.colocarPeca(torre, origemT);
            torre.decrementarMovimento();
        }

        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.retirarPeca(destinoT);
            tabuleiro.colocarPeca(torre, origemT);
            torre.decrementarMovimento();
        }

        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == vulneravelEnPassant) {
                PecaDeXadrez peao = (PecaDeXadrez) tabuleiro.retirarPeca(destino);
                Posicao posicaoPeao;
                if (p.getCor() == Cor.BRANCA) {
                    posicaoPeao = new Posicao(3, destino.getColuna());
                } else {
                    posicaoPeao = new Posicao(4, destino.getColuna());
                }
                tabuleiro.colocarPeca(peao, posicaoPeao);
            }
        }
    }

    private void validarPosicaoDeOrigem(Posicao posicao) {
        if (!tabuleiro.haPeca(posicao)) {
            throw new XadrezException("Não existe peça na posição de origem.");
        }
        PecaDeXadrez peca = (PecaDeXadrez) tabuleiro.peca(posicao);
        if (peca.getCor() != jogadorAtual) {
            throw new XadrezException("A peça escolhida não é sua.");
        }
        if (!peca.existeMovimentoPossivel()) {
            throw new XadrezException("Não há movimentos possíveis para a peça escolhida.");
        }
    }

    private void validarPosicaoDeDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
            throw new XadrezException("A peça escolhida não pode mover-se para a posição de destino.");
        }
    }

    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCA) ? Cor.PRETA : Cor.BRANCA;
    }

    private Cor oponente(Cor cor) {
        return (cor == Cor.BRANCA) ? Cor.PRETA : Cor.BRANCA;
    }

    private PecaDeXadrez rei(Cor cor) {
        List<Peca> pecas = pecasTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
            .collect(Collectors.toList());
        for (Peca p : pecas) {
            if (p instanceof Rei) {
                return (PecaDeXadrez) p;
            }
        }
        throw new IllegalStateException("Não existe o rei da cor " + cor + " no tabuleiro.");
    }

    private boolean testarXeque(Cor cor) {
        Posicao reiPosicao = rei(cor).getPosicao();
        List<Peca> pecaList = pecasTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == oponente(cor))
            .collect(Collectors.toList());
        for (Peca p : pecaList) {
            boolean[][] mat = p.movimentosPossiveis();
            if (mat[reiPosicao.getLinha()][reiPosicao.getColuna()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testarXequeMate(Cor cor) {
        if (!testarXeque(cor)) {
            return false;
        }
        List<Peca> pecas = pecasTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
            .collect(Collectors.toList());
        for (Peca p : pecas) {
            boolean[][] mat = p.movimentosPossiveis();
            for (int i = 0; i < tabuleiro.getLinhas(); i++) {
                for (int j = 0; j < tabuleiro.getColunas(); j++) {
                    if (mat[i][j]) {
                        Posicao origem = ((PecaDeXadrez) p).getPosicao();
                        Posicao destino = new Posicao(i, j);
                        Peca pecaCapturada = mover(origem, destino);
                        boolean testeXeque = testarXeque(cor);
                        desfazerMovimento(origem, destino, pecaCapturada);
                        if (!testeXeque) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
        tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
        pecasTabuleiro.add(peca);
    }

    private void iniciarPartida() {
        // Brancas
        colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCA, this));
        colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCA));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCA));
        for (char coluna = 'a'; coluna <= 'h'; coluna++) {
            colocarNovaPeca(coluna, 2, new Peao(tabuleiro, Cor.BRANCA, this));
        }

        // Pretas
        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETA));
        colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETA));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETA));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETA));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETA, this));
        colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETA));
        colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETA));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETA));
        for (char coluna = 'a'; coluna <= 'h'; coluna++) {
            colocarNovaPeca(coluna, 7, new Peao(tabuleiro, Cor.PRETA, this));
        }
    }
}
