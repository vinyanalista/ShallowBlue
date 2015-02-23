import java.awt.*;

import javax.swing.*;

public class TelaTabuleiro extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int BORDA = 0;
	private static final int LADO_DA_CASA = 48;
	private static final Color COR_1 = new Color(255, 225, 180);
	private static final Color COR_2 = new Color(195, 125, 60);
	private static final Color COR_COORDENADA = Color.BLACK;
	private static final Color COR_JOGADA_DESTINO = new Color(20, 70, 150);
	private static final Color COR_JOGADA_ORIGEM = new Color(110, 170, 220);

	private Image pecas;
	private Tabuleiro tabuleiro;
	private Jogada ultimaJogada;
	
	public void desenhar(Tabuleiro tabuleiro) {
		desenhar(tabuleiro, null);
	}
	
	public void desenhar(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		this.tabuleiro = tabuleiro;
		this.ultimaJogada = ultimaJogada;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		int xTabuleiro, yTabuleiro;
		// Desenha o tabuleiro
		// http://www.javacoffeebreak.com/books/extracts/javanotesv3/c3/ex-3-5-answer.html
		for (int linha = 0; linha < 8; linha++) {
			yTabuleiro = linha * LADO_DA_CASA;
			for (int coluna = 0; coluna < 8; coluna++) {
				xTabuleiro = coluna * LADO_DA_CASA;
				if ((linha % 2) == (coluna % 2))
					g.setColor(COR_1);
				else
					g.setColor(COR_2);
				g.fillRect(xTabuleiro, yTabuleiro, LADO_DA_CASA, LADO_DA_CASA);
			}
		}
		// Pinta as casas envolvidas na última jogada
		if (ultimaJogada != null) {
			// Origem
			xTabuleiro = ultimaJogada.getOrigem().getColuna() * LADO_DA_CASA;
			yTabuleiro = ultimaJogada.getOrigem().getLinha() * LADO_DA_CASA;
			g.setColor(COR_JOGADA_ORIGEM);
			g.fillRect(xTabuleiro, yTabuleiro, LADO_DA_CASA, LADO_DA_CASA);
			// Destino
			xTabuleiro = ultimaJogada.getDestino().getColuna() * LADO_DA_CASA;
			yTabuleiro = ultimaJogada.getDestino().getLinha() * LADO_DA_CASA;
			g.setColor(COR_JOGADA_DESTINO);
			g.fillRect(xTabuleiro, yTabuleiro, LADO_DA_CASA, LADO_DA_CASA);
		}
		// Pinta as indicações de linhas e colunas
		g.setColor(COR_COORDENADA);
		xTabuleiro = 0;
		for (int linha = 0; linha < 8; linha++) {
			yTabuleiro = linha * LADO_DA_CASA + 10;
			g.drawString(String.valueOf(Casa.linhaComoCaractere(linha)), xTabuleiro, yTabuleiro);
		}
		yTabuleiro = 8 * LADO_DA_CASA - 3;
		for (int coluna = 0; coluna < 8; coluna++) {
			xTabuleiro = (coluna + 1) * LADO_DA_CASA - 10;
			g.drawString(String.valueOf(Casa.colunaComoCaractere(coluna)), xTabuleiro, yTabuleiro);
		}
		// Desenha as peças
		if (tabuleiro == null) {
			return;
		}
		boolean jogadorPreto = Cor.PRETA.equals(tabuleiro.getJogador());
		if (jogadorPreto) {
			tabuleiro.virar();
		}
		int xPecas = 0, yPecas = 0;
		for (int linha = 0; linha < 8; linha++) {
			yTabuleiro = linha * LADO_DA_CASA;
			for (int coluna = 0; coluna < 8; coluna++) {
				xTabuleiro = coluna * LADO_DA_CASA;
				if (tabuleiro.casaVazia(linha, coluna)) {
					continue;
				}
				switch (tabuleiro.pecaNaCasa(linha, coluna)) {
				case BISPO_ADVERSARIO:
					xPecas = 192;
					yPecas = 64;
					break;
				case BISPO_AMIGO:
					xPecas = 192;
					yPecas = 0;
					break;
				case CAVALO_ADVERSARIO:
					xPecas = 256;
					yPecas = 64;
					break;
				case CAVALO_AMIGO:
					xPecas = 256;
					yPecas = 0;
					break;
				case PEAO_ADVERSARIO:
					xPecas = 320;
					yPecas = 64;
					break;
				case PEAO_AMIGO:
					xPecas = 320;
					yPecas = 0;
					break;
				case RAINHA_ADVERSARIA:
					xPecas = 64;
					yPecas = 64;
					break;
				case RAINHA_AMIGA:
					xPecas = 64;
					yPecas = 0;
					break;
				case REI_ADVERSARIO:
					xPecas = 0;
					yPecas = 64;
					break;
				case REI_AMIGO:
					xPecas = 0;
					yPecas = 0;
					break;
				case TORRE_ADVERSARIA:
					xPecas = 128;
					yPecas = 64;
					break;
				case TORRE_AMIGA:
					xPecas = 128;
					yPecas = 0;
					break;
				default:
					break;
				}
				g.drawImage(pecas, xTabuleiro, yTabuleiro, xTabuleiro
						+ LADO_DA_CASA, yTabuleiro + LADO_DA_CASA, xPecas,
						yPecas, xPecas + 64, yPecas + 64, this);
			}
		}
		if (jogadorPreto) {
			tabuleiro.virar();
		}
	}
	
	public TelaTabuleiro() {
		super();
		tabuleiro = null;
		pecas = new ImageIcon(TelaTabuleiro.class.getClassLoader().getResource("pecas.png")).getImage();
		setMaximumSize(new Dimension(LADO_DA_CASA * 8 + BORDA, LADO_DA_CASA * 8 + BORDA));
		setMinimumSize(new Dimension(LADO_DA_CASA * 8 + BORDA, LADO_DA_CASA * 8 + BORDA));
		setPreferredSize(new Dimension(LADO_DA_CASA * 8 + BORDA, LADO_DA_CASA * 8 + BORDA));
		desenhar(tabuleiro);
	}

}