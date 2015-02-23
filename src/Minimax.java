import java.util.List;

public class Minimax {
	private static final int INFINITO_NEGATIVO = Integer.MIN_VALUE;
	private static final int INFINITO_POSITIVO = Integer.MAX_VALUE;
	
	private int profundidade;
	private No raiz;
	
	public Minimax(No raiz, int profundidade) {
		this.profundidade = profundidade;
		this.raiz = raiz;
	}
	
	public Minimax(Tabuleiro tabuleiroInicial, int profundidade) {
		this(new No(tabuleiroInicial), profundidade);
	}
	
	public int getProfundidade() {
		return profundidade;
	}
	
	public Jogada melhorJogada() {
		Jogada melhorJogada = melhorJogada(raiz, null, profundidade); 
		return melhorJogada;
	}
	
	private Jogada piorJogada(No no, Jogada jogadaAnterior, int profundidade) {
		Tabuleiro tabuleiroAtual = no.getTabuleiro();
		
		List<Jogada> jogadasPossiveis = null;
		
		if ((profundidade != 0) && (no.getArestas().isEmpty())) {
			jogadasPossiveis = tabuleiroAtual.jogadasPossiveis();
			for (Jogada jogadaPossivel : jogadasPossiveis) {
				Tabuleiro tabuleiroPossivel = tabuleiroAtual.jogar(jogadaPossivel);
				tabuleiroPossivel.virar();
				No noFilho = new No(tabuleiroPossivel);
				no.getArestas().add(new Aresta(no, noFilho, jogadaPossivel));
			}
		}
		
		// Nó terminal
		if ((profundidade == 0) || (no.getArestas().isEmpty() && jogadasPossiveis.isEmpty())) {
			int profundidadeAtual = this.profundidade - profundidade;
			int nota = Avaliacao.avaliar(tabuleiroAtual, profundidadeAtual) * (-1);
			jogadaAnterior.setNota(nota);
			// Retorna a jogada que originou o tabuleiro e a nota deste
			return jogadaAnterior;
		}
		
		Jogada piorJogada = null;
		int piorNota = INFINITO_POSITIVO;
		
		for (Aresta aresta : no.getArestas()) {
			Jogada jogadaPossivel = aresta.getJogada();
			No noFilho = aresta.getFilho();
			Jogada possivelPiorJogada = melhorJogada(noFilho, jogadaPossivel, profundidade - 1);
			if (possivelPiorJogada.getNota() < piorNota) {
				piorJogada = possivelPiorJogada;
				piorNota = possivelPiorJogada.getNota();
			}
		}
		
		return piorJogada;
	}
	
	private Jogada melhorJogada(No no, Jogada jogadaAnterior, int profundidade) {
		Tabuleiro tabuleiroAtual = no.getTabuleiro();
		
		List<Jogada> jogadasPossiveis = null;
		
		if ((profundidade != 0) && (no.getArestas().isEmpty())) {
			jogadasPossiveis = tabuleiroAtual.jogadasPossiveis();
			for (Jogada jogadaPossivel : jogadasPossiveis) {
				Tabuleiro tabuleiroPossivel = tabuleiroAtual.jogar(jogadaPossivel);
				tabuleiroPossivel.virar();
				No noFilho = new No(tabuleiroPossivel);
				no.getArestas().add(new Aresta(no, noFilho, jogadaPossivel));
			}
		}
		
		// Nó terminal
		if ((profundidade == 0) || (no.getArestas().isEmpty() && jogadasPossiveis.isEmpty())) {
			int profundidadeAtual = this.profundidade - profundidade;
			int nota = Avaliacao.avaliar(tabuleiroAtual, profundidadeAtual);
			jogadaAnterior.setNota(nota);
			// Retorna a jogada que originou o tabuleiro e a nota deste
			return jogadaAnterior;
		}
		
		Jogada melhorJogada = null;
		int melhorNota = INFINITO_NEGATIVO;
		
		for (Aresta aresta : no.getArestas()) {
			Jogada jogadaPossivel = aresta.getJogada();
			No noFilho = aresta.getFilho();
			Jogada possivelMelhorJogada = piorJogada(noFilho, jogadaPossivel, profundidade - 1);
			if (possivelMelhorJogada.getNota() > melhorNota) {
				melhorNota = possivelMelhorJogada.getNota();
				if (profundidade == this.profundidade) { // Raiz
					melhorJogada = jogadaPossivel;
					jogadaPossivel.setNota(melhorNota);
				} else {
					melhorJogada = possivelMelhorJogada;
				}
			}
		}
		
		return melhorJogada;
	}
	
	public boolean seguirJogada(Jogada jogada) {
		for (Aresta aresta : raiz.getArestas()) {
			if (aresta.getJogada().equals(jogada)) {
				raiz = aresta.getFilho();
				raiz.setPai(null);
				return true;
			}
		}
		return false;
	}
	
	public void setProfundidade(int profundidade) {
		this.profundidade = profundidade;
	}
	
	public Tabuleiro tabuleiroAtual() {
		return raiz.getTabuleiro();
	}
	
}