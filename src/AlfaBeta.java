import java.util.*;

public class AlfaBeta {
	private static final double FATOR_DE_ALEATORIEDADE = 0.001;
	private static final int INFINITO_NEGATIVO = Integer.MIN_VALUE;
	private static final int INFINITO_POSITIVO = Integer.MAX_VALUE;
	public static final int PROFUNDIDADE_PADRAO = 4;
	private static final int QUANTIDADE_DE_JOGADAS_CONSIDERADAS = 6;
	
	private Random geradorDeNumerosAleatorios;
	private int profundidade;
	private No raiz;
	
	public AlfaBeta() {
		this(new Tabuleiro(), PROFUNDIDADE_PADRAO);
	}
	
	public AlfaBeta(Tabuleiro tabuleiroInicial) {
		this(tabuleiroInicial, PROFUNDIDADE_PADRAO);
	}
	
	public AlfaBeta(Tabuleiro tabuleiroInicial, int profundidade) {
		geradorDeNumerosAleatorios = new Random();
		this.profundidade = profundidade;
		raiz = new No(tabuleiroInicial);
	}
	
	public boolean fatorDeAleatoriedade() {
		return (geradorDeNumerosAleatorios.nextDouble() < FATOR_DE_ALEATORIEDADE);
	}
	
	public int getProfundidade() {
		return profundidade;
	}
	
	@SuppressWarnings("unchecked")
	private List<Jogada> jogadasPossiveisOrdenadas(Tabuleiro tabuleiro) {
		// É preciso clonar a lista porque ela será modificada
		List<Jogada> jogadasPossiveis = (List<Jogada>) ((ArrayList<Jogada>) tabuleiro.jogadasPossiveis()).clone();
		for (Jogada jogada : jogadasPossiveis) {
			Tabuleiro tabuleiroDeTeste = tabuleiro.jogar(jogada);
			jogada.setNota(Avaliacao.avaliar(tabuleiroDeTeste, 0));
		}
		List<Jogada> melhoresJogadas = new ArrayList<Jogada>();
		int j = QUANTIDADE_DE_JOGADAS_CONSIDERADAS;
		while (j > 0) {
			Jogada melhorJogada = null;
			int melhorNota = INFINITO_NEGATIVO;
			for (Jogada jogada : jogadasPossiveis) {
				if (jogada.getNota() > melhorNota) {
					melhorJogada = jogada;
					melhorNota = jogada.getNota();
				}
			}
			jogadasPossiveis.remove(melhorJogada);
			melhoresJogadas.add(melhorJogada);
			j--;
			if (j > 0) {
				continue;
			}
			if (fatorDeAleatoriedade() && (jogadasPossiveis.size() > 0)) {
				melhoresJogadas.remove(0);
				j++;
			}
		}
		return jogadasPossiveis;
	}
	
	public Jogada melhorJogada() {
		Jogada melhorJogada = melhorJogada(raiz, null, profundidade, INFINITO_NEGATIVO, INFINITO_POSITIVO); 
		return melhorJogada;
	}
	
	private Jogada melhorJogada(No no, Jogada jogadaAnterior, int profundidade, int alfa, int beta) {
		Tabuleiro tabuleiroAtual = no.getTabuleiro();
		
		List<Jogada> jogadasPossiveis = null;
		
		if ((profundidade != 0) && (no.getArestas().isEmpty())) {
			jogadasPossiveis = jogadasPossiveisOrdenadas(tabuleiroAtual);
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
			Jogada possivelMelhorJogada = piorJogada(noFilho, jogadaPossivel, profundidade - 1, alfa, beta);
			if (possivelMelhorJogada.getNota() > melhorNota) {
				melhorNota = possivelMelhorJogada.getNota();
				if (profundidade == this.profundidade) { // Raiz
					melhorJogada = jogadaPossivel;
					jogadaPossivel.setNota(melhorNota);
				} else {
					melhorJogada = possivelMelhorJogada;
				}
			}
			
			if (melhorNota >= beta) {
				return melhorJogada;
			}
			
			if (melhorNota > alfa) {
				alfa = melhorNota;
			}
		}
		
		return melhorJogada;
	}
	
	private Jogada piorJogada(No no, Jogada jogadaAnterior, int profundidade, int alfa, int beta) {
		Tabuleiro tabuleiroAtual = no.getTabuleiro();
		
		List<Jogada> jogadasPossiveis = null;
		
		if ((profundidade != 0) && (no.getArestas().isEmpty())) {
			jogadasPossiveis = jogadasPossiveisOrdenadas(tabuleiroAtual);
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
			Jogada possivelPiorJogada = melhorJogada(noFilho, jogadaPossivel, profundidade - 1, alfa, beta);
			if (possivelPiorJogada.getNota() < piorNota) {
				piorJogada = possivelPiorJogada;
				piorNota = possivelPiorJogada.getNota();
			}

			if (piorNota <= alfa) {
				return piorJogada;
			}
			
			if (piorNota < beta) {
				beta = piorNota;
			}
		}
		
		return piorJogada;
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