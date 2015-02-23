import br.com.vinyanalista.jchessengine.*;

public class InterfaceXboard implements JChessEngine {
	private static final int UM_SEGUNDO = 100;
	private static final int UM_MINUTO = 60 * UM_SEGUNDO;
	private static final int VINTE_SEGUNDOS = 1 * UM_SEGUNDO;
	
	private static String tempoComoString(long tempoEmCentissegundos) {
		StringBuilder sb = new StringBuilder();
		long minutos = tempoEmCentissegundos / UM_MINUTO;
		long segundos = (tempoEmCentissegundos % UM_MINUTO) / UM_SEGUNDO;
		if (minutos > 1) {
			sb.append(minutos).append(" minutos");
		} else if (minutos == 1) {
			sb.append(minutos).append(" minuto");
		}
		if ((minutos > 0) && (segundos > 0)) {
			sb.append(" e ");
		}
		if (segundos > 1) {
			sb.append(segundos).append(" segundos");
		} else if (segundos == 1) {
			sb.append(segundos).append(" segundo");
		}
		return sb.toString();
	}
	
	private AlfaBeta alfaBeta;
	private boolean primeiraJogada;
	private int profundidade;
	private Tabuleiro tabuleiro;
	
	private boolean debugMode;
	private boolean forceMode;
	
	public InterfaceXboard() {
		alfaBeta = new AlfaBeta();
		debugMode = true;
		forceMode = false;
		primeiraJogada = true;
		profundidade = alfaBeta.getProfundidade();
		tabuleiro = alfaBeta.tabuleiroAtual();
	}

	@Override
	public void black() {
	}

	@Override
	public void computer() {
		debug("O adversario e um computador");
	}
	
	public Jogada converterParaEngine(String jogadaNoFormatoXboard) {
		debug("converterParaEngine(" + jogadaNoFormatoXboard + ")");
		int coluna = Casa.coluna(jogadaNoFormatoXboard.charAt(0));
		char linhaNoFormatoXboard = jogadaNoFormatoXboard.charAt(1);
		int linha = Casa.linha(linhaNoFormatoXboard);
		Casa origem = Casa.obter(linha, coluna);
		int novaColuna = Casa.coluna(jogadaNoFormatoXboard.charAt(2));
		int novaLinha = Casa.linha(jogadaNoFormatoXboard.charAt(3));
		Casa destino = Casa.obter(novaLinha, novaColuna);
		Peca pecaCapturada = tabuleiro.pecaNaCasa(novaLinha, novaColuna);
		Jogada jogada;
		if (jogadaNoFormatoXboard.length() == 4) {
			jogada = new Jogada(origem, destino, pecaCapturada);
		} else {
			// Promoção do peão (exemplo: e7e8q)
			char pecaPromovidaComoCaractere = jogadaNoFormatoXboard.charAt(4);
			if (Character.getNumericValue(linhaNoFormatoXboard) == 8) {
				pecaPromovidaComoCaractere = Character.toUpperCase(pecaPromovidaComoCaractere);
			} else {
				pecaPromovidaComoCaractere = Character.toLowerCase(pecaPromovidaComoCaractere);
			}
			Peca pecaPromovida = Peca.obter(pecaPromovidaComoCaractere);
			jogada = new Jogada(origem, destino, pecaCapturada, pecaPromovida);
		}
		// TODO Verificar roque
		return jogada;
	}
	
	public String converterParaXboard(Jogada jogada) {
		debug("converterParaXboard(" + jogada.toString() + ")");
		StringBuilder sb = new StringBuilder();
		sb.append(jogada.getOrigem().toString().toLowerCase());
		sb.append(jogada.getDestino().toString().toLowerCase());
		if (jogada.ocorrePromocao()) {
			// Promoção do peão (exemplo: e7e8q)
			Peca pecaPromovida = jogada.getPecaPromovida();
			char pecaPromovidaComoCaractere = pecaPromovida.comoCaractere();
			sb.append(Character.toLowerCase(pecaPromovidaComoCaractere));
		}
		return sb.toString();
	}

	@Override
	public void debug(String message) {
		if (debugMode) {
			XBoard.send("# " + message);
		}
	}

	@Override
	public void force() {
		forceMode = true;
		debug("A engine esta inativa");
	}

	@Override
	public void go() {
		forceMode = false;
		jogar();
	}
	
	private void jogar() {
		if (primeiraJogada) {
			debug("A engine esta jogando com a cor " + tabuleiro.getJogador());
			primeiraJogada = false;
		}
		Jogada melhorJogada = alfaBeta.melhorJogada();
		debug("Jogada da engine: " + melhorJogada);
		if (alfaBeta.seguirJogada(melhorJogada)) {
			tabuleiro = alfaBeta.tabuleiroAtual();
		} else {
			tabuleiro = tabuleiro.jogar(melhorJogada);
			tabuleiro.virar();
			alfaBeta = new AlfaBeta(tabuleiro, profundidade);
		}
		XBoard.move(converterParaXboard(melhorJogada));
	}

	@Override
	public void moveNow() {
	}

	@Override
	public void newGame() {
		forceMode = false;
		if (alfaBeta != null) {
			alfaBeta = new AlfaBeta();
			primeiraJogada = true;
			profundidade = alfaBeta.getProfundidade();
			tabuleiro = alfaBeta.tabuleiroAtual();
		}
		debug("Uma nova partida comecou");
	}

	@Override
	public void opponentMove(String opponentMove) {
		Jogada jogadaDoAdversario = converterParaEngine(opponentMove);
		debug("Jogada do adversario: " + jogadaDoAdversario);
		if (alfaBeta.seguirJogada(jogadaDoAdversario)) {
			tabuleiro = alfaBeta.tabuleiroAtual();
		} else {
			// Jogada inválida (?)
        	// XBoard.illegalMove();
			// return;
			tabuleiro = tabuleiro.jogar(jogadaDoAdversario);
			tabuleiro.virar();
			alfaBeta = new AlfaBeta(tabuleiro, profundidade);
		}
		if (!forceMode) {
			jogar();
		}
	}

	@Override
	public void performanceTest(int depth) {
	}

	@Override
	public void quit() {
	}

	@Override
	public void random() {
	}

	@Override
	public void remove() {
	}

	@Override
	public void resign() {
	}

	@Override
	public void setBoard(String fen) {
	}

	@Override
	public void setDebug(boolean debug) {
		debugMode = debug;
	}

	@Override
	public void setOpponentTime(long time) {
		debug("Tempo do adversario: " + tempoComoString(time));
	}

	@Override
	public void setPondering(boolean pondering) {
	}

	@Override
	public void setProtocolVersion(int protocolVersion) {
		XBoard.feature("myname=\"ShallowBlue\" setboard=1 analyze=0 variants=\"normal\" colors=0 debug=1 done=1");
	}

	@Override
	public void setSearchDepth(int depth) {
		profundidade = depth;
		alfaBeta.setProfundidade(profundidade);
		debug("Profundidade de busca alterada para " + profundidade);
	}

	@Override
	public void setShowThinking(boolean showThinking) {
	}

	@Override
	public void setTime(long time) {
		debug("Nosso tempo: " + tempoComoString(time));
		if (time < VINTE_SEGUNDOS) {
			debug("Corre!");
			profundidade = 2;
			alfaBeta.setProfundidade(profundidade);
		}
	}

	@Override
	public void setTimeControls(int time) {
	}

	@Override
	public void setTimeControls(int movesPerSession, long baseTime, long increment) {
	}

	@Override
	public void undo() {
	}

	@Override
	public void unrecognizedCommand(String input) {
	}

	@Override
	public void white() {
	}

}