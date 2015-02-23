import org.json.JSONObject;

import br.com.cliente.TXadrezClient;

public class EscutaDoServidor implements Runnable {
	private static final int TEMPO_DE_ESPERA = 100;
	
	private AlfaBeta alfaBeta;
	private boolean primeiraJogada;
	private Tabuleiro tabuleiro;
	
	private final TXadrezClient cliente;
	private final String id;
	private final InterfaceServidor interfaceGrafica;
	private boolean interromper;

	public EscutaDoServidor(TXadrezClient cliente, String id, InterfaceServidor interfaceGrafica) {
		alfaBeta = new AlfaBeta();
		primeiraJogada = true;
		tabuleiro = alfaBeta.tabuleiroAtual();
		
		this.cliente = cliente;
		this.id = id;
		this.interfaceGrafica = interfaceGrafica;
		interromper = false;
	}
	
	private synchronized Jogada converterParaEngine(JSONObject jogadaNoFormatoServidor) {
		JSONObject casaNoFormatoServidor = jogadaNoFormatoServidor.getJSONObject("posicao_atual");
		int linha = Casa.linha(casaNoFormatoServidor.getString("x").charAt(0));
		int coluna = Casa.coluna(casaNoFormatoServidor.getString("y").charAt(0));
		Casa origem = Casa.obter(linha, coluna);
		casaNoFormatoServidor = jogadaNoFormatoServidor.getJSONObject("nova_posicao");
		int novaLinha = Casa.linha(casaNoFormatoServidor.getString("x").charAt(0));
		int novaColuna = Casa.coluna(casaNoFormatoServidor.getString("y").charAt(0));
		Casa destino = Casa.obter(novaLinha, novaColuna);
		// TODO Verificar: às vezes retorna peça capturada != null sem ter havido captura
		Peca pecaCapturada = tabuleiro.pecaNaCasa(novaLinha, novaColuna);
		Jogada jogada;
		if (casaNoFormatoServidor.has("peca_promocao")) {
			String pecaPromovidaNoFormatoServidor = jogadaNoFormatoServidor.getString("peca_promocao");
			Peca pecaPromovida;
			switch (Character.toUpperCase(pecaPromovidaNoFormatoServidor.charAt(0))) {
			case 'B':
				pecaPromovida = Peca.BISPO_ADVERSARIO;
				break;
			case 'C':
				pecaPromovida = Peca.CAVALO_ADVERSARIO;
				break;
			case 'D':
				pecaPromovida = Peca.RAINHA_ADVERSARIA;
				break;
			case 'T':
				pecaPromovida = Peca.TORRE_ADVERSARIA;
				break;
			default:
				pecaPromovida = null;
				break;
			}
			jogada = new Jogada(origem, destino, pecaCapturada, pecaPromovida);
		} else {
			jogada = new Jogada(origem, destino, pecaCapturada);
		}
		// TODO Verificar roque
		return jogada;
	}
	
	private synchronized JSONObject converterParaServidor(Casa casa) {
		JSONObject casaNoFormatoServidor = new JSONObject();
		casaNoFormatoServidor.put("x", String.valueOf(casa
				.getLinhaComoCaractere()));
		casaNoFormatoServidor.put("y", String.valueOf(Character
				.toLowerCase(casa.getColunaComoCaractere())));
		return casaNoFormatoServidor;
	}
	
	private synchronized JSONObject converterParaServidor(Jogada jogada) {
		JSONObject jogadaNoFormatoServidor = new JSONObject();
		jogadaNoFormatoServidor.put("id_jogador", id);
		jogadaNoFormatoServidor.put("posicao_atual",
				converterParaServidor(jogada.getOrigem()));
		jogadaNoFormatoServidor.put("nova_posicao",
				converterParaServidor(jogada.getDestino()));
		if (jogada.ocorrePromocao()) {
			jogadaNoFormatoServidor.put("peca_promocao",
					converterParaServidor(jogada.getPecaPromovida()));
		}
		return jogadaNoFormatoServidor;
	}
	
	private synchronized String converterParaServidor(Peca peca) {
		switch (peca.getTipo()) {
		case BISPO:
			return "B";
		case CAVALO:
			return "C";
		case RAINHA:
			return "D";
		case TORRE:
			return "T";
		default:
			return null;
		}
	}
	
	public synchronized void interromper() {
		interromper = true;
	}
	
	private synchronized void jogadaDoAdversario(JSONObject jogadaDoAdversario) {
		Jogada jogada = converterParaEngine(jogadaDoAdversario);
		// TODO Verificar: às vezes retorna peça capturada != null sem ter havido captura
		//interfaceGrafica.log("Jogada do adversario: " + jogada);
		interfaceGrafica.log("Jogada do adversario: " + jogada.toString().substring(0, 5));
		if (alfaBeta.seguirJogada(jogada)) {
			tabuleiro = alfaBeta.tabuleiroAtual();
		} else {
			tabuleiro = tabuleiro.jogar(jogada);
			tabuleiro.virar();
			alfaBeta = new AlfaBeta(tabuleiro);
		}
		interfaceGrafica.desenharTabuleiro(tabuleiro, jogada);
	}
	
	private synchronized boolean jogar() {
		if (primeiraJogada) {
			interfaceGrafica.log("Jogando com a cor " + tabuleiro.getJogador());
			primeiraJogada = false;
		}
		Jogada melhorJogada = alfaBeta.melhorJogada();
		
		if (alfaBeta.seguirJogada(melhorJogada)) {
			tabuleiro = alfaBeta.tabuleiroAtual();
		} else {
			tabuleiro = tabuleiro.jogar(melhorJogada);
			tabuleiro.virar();
			alfaBeta = new AlfaBeta(tabuleiro);
		}
		String requisicaoJogar = converterParaServidor(melhorJogada).toString();
		
		// Verifica fim de jogo imediatamente antes de jogar (pode ser que o tempo tenha esgotado)
		Mensagem situacao = processarRespostaDoServidor(cliente.SituacaoAtual(id));
		if ((situacao != null) && situacao.fimDeJogo()) {
			interfaceGrafica.fimDeJogo(situacao.getMensagem());
			return false;
		}
		
		// TODO Verificar: às vezes retorna peça capturada != null sem ter havido captura
		//interfaceGrafica.log("Jogada da engine: " + melhorJogada);
		interfaceGrafica.log("Jogada da engine: " + melhorJogada.toString().substring(0, 5));
		interfaceGrafica.desenharTabuleiro(tabuleiro, melhorJogada);
		Mensagem retornoJogar = processarRespostaDoServidor(cliente.Jogar(requisicaoJogar));
		if (retornoJogar == null) {
			interfaceGrafica.desconectar();
			return false;
		}
		if (retornoJogar.erro()) {
			interfaceGrafica.tratarErro(retornoJogar);
		} else if (retornoJogar.fimDeJogo()) {
			interfaceGrafica.fimDeJogo(retornoJogar.getMensagem());
		} else {
			String codigo = retornoJogar.getCodigo();
			if (codigo.equals("104") || codigo.equals("105") || codigo.equals("107") || codigo.equals("108")) {
				// Jogada aceita, é a vez do adversário
				return true;
			} else {
				String mensagem = retornoJogar.getMensagem();
				interfaceGrafica.log(codigo + " - " + mensagem);
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		interfaceGrafica.desenharTabuleiro(tabuleiro);
		String ultimoCodigo = null;
		while (true) {
			Mensagem situacao = processarRespostaDoServidor(cliente.SituacaoAtual(id));
			if (situacao == null) {
				interfaceGrafica.desconectar();
				break;
			}
			if (situacao.erro()) {
				interfaceGrafica.tratarErro(situacao);
			} else if (situacao.fimDeJogo()) {
				interfaceGrafica.fimDeJogo(situacao.getMensagem());
			} else {
				if (situacao.getCodigo().equals("103") || situacao.getCodigo().equals("106")) {
					// É a vez da engine
					if (situacao.getUltimaJogada() != null) {
						jogadaDoAdversario(situacao.getUltimaJogada());
					}
					jogar();
				} else {
					if ((ultimoCodigo == null) || !ultimoCodigo.equals(situacao.getCodigo())) {
						interfaceGrafica.log(situacao.getCodigo() + " - " + situacao.getMensagem());
					}
				}
			}
			if (interromper) {
				break;
			}
			ultimoCodigo = situacao.getCodigo();
			try {
				Thread.sleep(TEMPO_DE_ESPERA);
			} catch (InterruptedException excecao) {
			}
		}
	}
	
	private Mensagem processarRespostaDoServidor(String respostaDoServidor) {
		String codigo, mensagem;
		JSONObject resposta, ultimaJogada = null;
		try {
			if ((respostaDoServidor == null) || respostaDoServidor.isEmpty()) {
				return null;
			}
			resposta = new JSONObject(respostaDoServidor);
			resposta = new JSONObject(resposta.getJSONArray("result").getString(0));
			JSONObject status = resposta.getJSONObject("mensagem");
			codigo = status.getString("codigo");
			mensagem = status.getString("mensagem");
			if (resposta.has("tabuleiro")) {
				JSONObject tabuleiro = resposta.getJSONObject("tabuleiro");	
				if (tabuleiro.has("ultima_jogada")) {	
					ultimaJogada = tabuleiro.getJSONObject("ultima_jogada");
				}
			}
		} catch (Exception excecao1) {
			try {
				resposta = new JSONObject(excecao1.getMessage());
				resposta = new JSONObject(resposta.getString("error"));
				codigo = resposta.getString("codigo");
				mensagem = resposta.getString("mensagem");
			} catch (Exception excecao2) {
				return null;
			}
		}
		return new Mensagem(codigo, mensagem, ultimaJogada);
	}
	
}