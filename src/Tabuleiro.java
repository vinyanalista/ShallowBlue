import java.util.*;

public class Tabuleiro {
	private static final Peca[][] TABULEIRO_INICIAL = {
		{Peca.TORRE_ADVERSARIA, Peca.CAVALO_ADVERSARIO, Peca.BISPO_ADVERSARIO, Peca.RAINHA_ADVERSARIA, Peca.REI_ADVERSARIO, Peca.BISPO_ADVERSARIO, Peca.CAVALO_ADVERSARIO, Peca.TORRE_ADVERSARIA},
		{Peca.PEAO_ADVERSARIO, Peca.PEAO_ADVERSARIO, Peca.PEAO_ADVERSARIO, Peca.PEAO_ADVERSARIO, Peca.PEAO_ADVERSARIO, Peca.PEAO_ADVERSARIO, Peca.PEAO_ADVERSARIO, Peca.PEAO_ADVERSARIO},
		{null, null, null, null, null, null, null, null},
		{null, null, null, null, null, null, null, null},
		{null, null, null, null, null, null, null, null},
		{null, null, null, null, null, null, null, null},
		{Peca.PEAO_AMIGO, Peca.PEAO_AMIGO, Peca.PEAO_AMIGO, Peca.PEAO_AMIGO, Peca.PEAO_AMIGO, Peca.PEAO_AMIGO, Peca.PEAO_AMIGO, Peca.PEAO_AMIGO},
		{Peca.TORRE_AMIGA, Peca.CAVALO_AMIGO, Peca.BISPO_AMIGO, Peca.RAINHA_AMIGA, Peca.REI_AMIGO, Peca.BISPO_AMIGO, Peca.CAVALO_AMIGO, Peca.TORRE_AMIGA}
	};
	
	private static final Peca[][] clone(Peca[][] tabuleiro) {
		// http://stackoverflow.com/questions/1686425/copy-a-2d-array-in-java
		Peca[][] tabuleiroClone = new Peca[tabuleiro.length][];
		for (int linha = 0; linha < tabuleiro.length; linha++) {
			tabuleiroClone[linha] = tabuleiro[linha].clone();
		}
		return tabuleiroClone;
	}
	
	private CondicoesParaRoque condicoesParaRoqueDoBranco, condicoesParaRoqueDoPreto;
	private List<Jogada> jogadasPossiveis;
	private Cor jogador;
	private int posicaoDoReiAdversario, posicaoDoReiAmigo;
	
	private Peca[][] tabuleiro;
	
	public Tabuleiro() {
		this(clone(TABULEIRO_INICIAL), new CondicoesParaRoque(),
				new CondicoesParaRoque(), null, Cor.BRANCA);
	}
	
	public Tabuleiro(Peca[][] tabuleiro,
			CondicoesParaRoque condicoesParaRoqueDoBranco,
			CondicoesParaRoque condicoesParaRoqueDoPreto,
			List<Jogada> jogadasPossiveis, Cor jogador) {
		this.condicoesParaRoqueDoBranco = condicoesParaRoqueDoBranco;
		this.condicoesParaRoqueDoPreto = condicoesParaRoqueDoPreto;
		this.jogadasPossiveis = jogadasPossiveis;
		this.jogador = jogador;
		this.tabuleiro = tabuleiro;
		calcularPosicoesDosReis();
	}
	
	private void calcularPosicoesDosReis() {
        posicaoDoReiAdversario = -1;
        for (int linha = 0; linha < 8; linha++) {
			for (int coluna = 0; coluna < 8; coluna++) {
				if (Peca.REI_ADVERSARIO.equals(pecaNaCasa(linha, coluna))) {
	                posicaoDoReiAdversario = Casa.inteiro(linha, coluna);
	                break;
	            }
			}
        }
        
        posicaoDoReiAmigo = -1;
        for (int linha = 0; linha < 8; linha++) {
			for (int coluna = 0; coluna < 8; coluna++) {
				if (Peca.REI_AMIGO.equals(pecaNaCasa(linha, coluna))) {
					posicaoDoReiAmigo = Casa.inteiro(linha, coluna);
	                break;
	            }
			}
        }
    }
	
	private boolean casaVazia(Casa casa) {
        return (tabuleiro[casa.getLinha()][casa.getColuna()] == null);
    }
	
	public boolean casaVazia(int casa) {
		return (tabuleiro[Casa.linha(casa)][Casa.coluna(casa)] == null);
	}
	
	public boolean casaVazia(int linha, int coluna) {
		return (tabuleiro[linha][coluna] == null);
	}
	
	@Override
	protected Object clone() {
		Tabuleiro clone = new Tabuleiro(clone(tabuleiro),
				(CondicoesParaRoque) condicoesParaRoqueDoBranco.clone(),
				(CondicoesParaRoque) condicoesParaRoqueDoPreto.clone(), null,
				jogador);
		return clone;
	}
	
	public Tabuleiro desfazer(Jogada jogada) {
		Tabuleiro novoTabuleiro = (Tabuleiro) clone();
		boolean jogadorPreto = Cor.PRETA.equals(novoTabuleiro.jogador);
		if (jogadorPreto) {
			jogada.virar();
		}
		int linha = jogada.getOrigem().getLinha();
		int coluna = jogada.getOrigem().getColuna();
		int novaLinha = jogada.getDestino().getLinha();
		int novaColuna = jogada.getDestino().getColuna();
		Peca pecaMovida;
		Peca pecaCapturada = jogada.getPecaCapturada();
		if (jogada.ocorrePromocao()) {
			pecaMovida = Peca.PEAO_AMIGO;
		} else {
			pecaMovida = novoTabuleiro.pecaNaCasa(novaLinha, novaColuna);
			// Atualiza posição do rei, se a peça movida foi o rei
			if (Peca.REI_AMIGO.equals(pecaMovida)) {
				novoTabuleiro.posicaoDoReiAmigo = Casa.inteiro(linha, coluna);
			}
		}
		novoTabuleiro.tabuleiro[linha][coluna] = pecaMovida;
		novoTabuleiro.tabuleiro[novaLinha][novaColuna] = pecaCapturada;
		if (jogadorPreto) {
			jogada.virar();
		}
		// TODO Desfazer roque
		return novoTabuleiro;
	}
	
	public Cor getJogador() {
		return jogador;
	}
	
	public int getPosicaoDoReiAmigo(){
		return posicaoDoReiAmigo;
	}
	
	public List<Jogada> jogadasPossiveis() {
		if (jogadasPossiveis != null) {
			// Faz cache das jogadas possíveis
			return jogadasPossiveis;
		}
		jogadasPossiveis = new ArrayList<Jogada>();
		for (int linha = 0; linha < 8; linha++) {
			for (int coluna = 0; coluna < 8; coluna++) {
				if (casaVazia(linha, coluna)) {
					continue;
				}
				switch (pecaNaCasa(linha, coluna)) {
				case BISPO_AMIGO:
					jogadasPossiveis.addAll(jogadasPossiveisParaBispo(linha, coluna));
					break;
				case CAVALO_AMIGO:
					jogadasPossiveis.addAll(jogadasPossiveisParaCavalo(linha, coluna));
					break;
				case PEAO_AMIGO:
					jogadasPossiveis.addAll(jogadasPossiveisParaPeao(linha, coluna));
					break;
				case RAINHA_AMIGA:
					jogadasPossiveis.addAll(jogadasPossiveisParaRainha(linha, coluna));
					break;
				case REI_AMIGO:
					jogadasPossiveis.addAll(jogadasPossiveisParaRei(linha, coluna));
					break;
				case TORRE_AMIGA:
					jogadasPossiveis.addAll(jogadasPossiveisParaTorre(linha, coluna));
					break;
				default:
					continue;
				}
			}
		}
		if (Cor.PRETA.equals(jogador)) {
			for (Jogada jogada : jogadasPossiveis) {
				if (jogada instanceof Roque) {
					// Não vira o roque ao adicioná-lo à lista de jogadas possíveis
					continue;
				}
				jogada.virar();
			}
		}
		return jogadasPossiveis;
	}
	
	public List<Jogada> jogadasPossiveisParaBispo(int linha, int coluna) {
		List<Jogada> lista = new ArrayList<Jogada>();
		Peca pecaAntiga;
		int novaLinha, novaColuna, temp=1;
		for (int j=-1;j<=1;j+=2) {
            for (int k=-1;k<=1;k+=2) {
	     		novaLinha = linha + temp*j;
	     		novaColuna = coluna + temp*k;
	     		//movimentos enquanto for casa vazia
	     		while(Casa.ehValida(novaLinha, novaColuna) && casaVazia(novaLinha, novaColuna)){
					pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
					tabuleiro[linha][coluna] = null;
					tabuleiro[novaLinha][novaColuna] = Peca.BISPO_AMIGO;
					if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()){
						Casa origem = Casa.obter(linha, coluna);
						Casa destino = Casa.obter(novaLinha, novaColuna);
						Jogada jogada = new Jogada(origem, destino, pecaAntiga);
						lista.add(jogada);
					}
					//desfaz o movimento 
					tabuleiro[linha][coluna] = Peca.BISPO_AMIGO;
					tabuleiro[novaLinha][novaColuna] = pecaAntiga;
					temp++;
					novaLinha = linha + temp*j;
		     		novaColuna = coluna + temp*k;
				}
	     		//captura
	     		if (Casa.ehValida(novaLinha, novaColuna) && !casaVazia(novaLinha, novaColuna) && pecaNaCasa(novaLinha, novaColuna).ehAdversaria()){
					pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
					Casa origem = Casa.obter(linha, coluna);
					Casa destino = Casa.obter(novaLinha, novaColuna);
					Jogada jogada = new Jogada(origem, destino, pecaAntiga);
					lista.add(jogada);
				}
	     		temp = 1;
	     	}
		}
		return lista;
	}
	
	public List<Jogada> jogadasPossiveisParaCavalo(int linha, int coluna) {
		List<Jogada> lista = new ArrayList<Jogada>();
		Peca pecaAntiga;
		int novaLinha, novaColuna;
		for (int j=-1;j<=1;j+=2) {
	     	for (int k=-1;k<=1;k+=2) {
	     		//movimentos abaixo
	     		novaLinha = linha + j;
	     		novaColuna = coluna + k*2;
	     		if (Casa.ehValida(novaLinha, novaColuna)){
	     			pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
	     			if (casaVazia(novaLinha, novaColuna) || pecaAntiga.ehAdversaria()) {
		     			tabuleiro[linha][coluna] = null;
		     			tabuleiro[novaLinha][novaColuna] = Peca.CAVALO_AMIGO;
		     			if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()){
		     				Casa origem = Casa.obter(linha, coluna);
							Casa destino = Casa.obter(novaLinha, novaColuna);
							Jogada jogada = new Jogada(origem, destino, pecaAntiga);
							lista.add(jogada);
		     			}
		     			//Desfaz o movimento
		     			tabuleiro[linha][coluna] = Peca.CAVALO_AMIGO;
		     			tabuleiro[novaLinha][novaColuna] = pecaAntiga;
		     		}
	     		}
	     		//movimentos acima
	     		novaLinha = linha + j*2;
	     		novaColuna = coluna + k;
	     		if (Casa.ehValida(novaLinha, novaColuna)){
	     			pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
	     			if (casaVazia(novaLinha, novaColuna) || pecaAntiga.ehAdversaria()) {
		     			tabuleiro[linha][coluna] = null;
		     			tabuleiro[novaLinha][novaColuna] = Peca.CAVALO_AMIGO;
		     			if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()){
		     				Casa origem = Casa.obter(linha, coluna);
							Casa destino = Casa.obter(novaLinha, novaColuna);
							Jogada jogada = new Jogada(origem, destino, pecaAntiga);
							lista.add(jogada);
		     			}
		     			//Desfaz o movimento
		     			tabuleiro[linha][coluna] = Peca.CAVALO_AMIGO;
		     			tabuleiro[novaLinha][novaColuna] = pecaAntiga;
		     		}
	     		}
	     	}
	     }
		return lista;
	}
	
	public List<Jogada> jogadasPossiveisParaPeao(int linha, int coluna) {
		List<Jogada> lista = new ArrayList<Jogada>();
		Peca pecaAntiga; 
		int novaLinha = linha - 1;
		int novaColuna;
		// Captura (movimento na diagonal) 
		for (int diagonal = -1; diagonal <= 1; diagonal += 2) {
			// Determina a localização do peão após o possível movimento
			novaColuna = coluna + diagonal;
			if (!Casa.ehValida(novaLinha, novaColuna)) {
				// Não ultrapassa o tabuleiro
				continue;
			}
			pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
			// Captura sem promoção
			if (!casaVazia(novaLinha, novaColuna) && pecaAntiga.ehAdversaria() && (linha > 1)) {
				// Testa o possível movimento
				tabuleiro[linha][coluna] = null;
				tabuleiro[novaLinha][novaColuna] = Peca.PEAO_AMIGO;
				// Se o movimento é válido, adiciona à lista
				if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()) {
					Casa origem = Casa.obter(linha, coluna);
					Casa destino = Casa.obter(novaLinha, novaColuna);
					Jogada jogada = new Jogada(origem, destino, pecaAntiga);
					lista.add(jogada);
				}
				// Desfaz o movimento
				tabuleiro[linha][coluna] = Peca.PEAO_AMIGO;
				tabuleiro[novaLinha][novaColuna] = pecaAntiga;
			}
			// Captura com promoção
			if (!casaVazia(novaLinha, novaColuna) && pecaAntiga.ehAdversaria() && (linha == 1)) {
				Peca[] pecasPromovidasPossiveis = {Peca.RAINHA_AMIGA, Peca.TORRE_AMIGA, Peca.BISPO_AMIGO, Peca.CAVALO_AMIGO};
				// Cadastra um movimento para cada peça que o peão pode se tornar
				for (int peca = 0; peca < 4; peca++) {
					Peca pecaPromovida = pecasPromovidasPossiveis[peca];
					// Testa o possível movimento
					tabuleiro[linha][coluna] = null;
					tabuleiro[novaLinha][novaColuna] = pecaPromovida;
					// Se o movimento é válido, adiciona à lista
					if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()) {
						// A promoção é cadastrada com uma informação adicional: a nova peça
						Casa origem = Casa.obter(linha, coluna);
						Casa destino = Casa.obter(novaLinha, novaColuna);
						Jogada jogada = new Jogada(origem, destino, pecaAntiga, pecaPromovida);
						lista.add(jogada);
					}
					// Desfaz o movimento
					tabuleiro[linha][coluna] = Peca.PEAO_AMIGO;
					tabuleiro[novaLinha][novaColuna] = pecaAntiga;
				}
			}
		}
		// Andar uma casa à frente sem promoção
		novaColuna = coluna;
		pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
		if (casaVazia(novaLinha, novaColuna) && (linha > 1)) {
			// Testa o possível movimento
			tabuleiro[linha][coluna] = null;
			tabuleiro[novaLinha][novaColuna] = Peca.PEAO_AMIGO;
			// Se o movimento é válido, adiciona à lista
			if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()) {
				Casa origem = Casa.obter(linha, coluna);
				Casa destino = Casa.obter(novaLinha, novaColuna);
				Jogada jogada = new Jogada(origem, destino, pecaAntiga);
				lista.add(jogada);
			}
			// Desfaz o movimento
			tabuleiro[linha][coluna] = Peca.PEAO_AMIGO;
			tabuleiro[novaLinha][novaColuna] = pecaAntiga;
		}
		// Andar uma casa à frente com promoção (promoção sem captura)
		if (casaVazia(novaLinha, novaColuna) && (linha == 1)) {
			Peca[] pecasPromovidasPossiveis = {Peca.RAINHA_AMIGA, Peca.TORRE_AMIGA, Peca.BISPO_AMIGO, Peca.CAVALO_AMIGO};
			// Cadastra um movimento para cada peça que o peão pode se tornar
			for (int peca = 0; peca < 4; peca++) {
				Peca pecaPromovida = pecasPromovidasPossiveis[peca];
				// Testa o possível movimento
				tabuleiro[linha][coluna] = null;
				tabuleiro[novaLinha][novaColuna] = pecaPromovida;
				// Se o movimento é válido, adiciona à lista
				if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()) {
					// A promoção é cadastrada com uma informação adicional: a nova peça
					Casa origem = Casa.obter(linha, coluna);
					Casa destino = Casa.obter(novaLinha, novaColuna);
					Jogada jogada = new Jogada(origem, destino, pecaAntiga, pecaPromovida);
					lista.add(jogada);
				}
				// Desfaz o movimento
				tabuleiro[linha][coluna] = Peca.PEAO_AMIGO;
				tabuleiro[novaLinha][novaColuna] = pecaAntiga;
			}
		}
		// Andar duas casas à frente
		if ((linha == 6) && casaVazia(5, coluna) && casaVazia(4, coluna)) {
			novaLinha = 4;
			novaColuna = coluna;
			// Testa o possível movimento
			tabuleiro[linha][coluna] = null;
			tabuleiro[novaLinha][novaColuna] = Peca.PEAO_AMIGO;
			// Se o movimento é válido, adiciona à lista
			if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()) {
				Casa origem = Casa.obter(linha, coluna);
				Casa destino = Casa.obter(novaLinha, novaColuna);
				Jogada jogada = new Jogada(origem, destino);
				lista.add(jogada);
			}
			// Desfaz o movimento
			tabuleiro[linha][coluna] = Peca.PEAO_AMIGO;
			tabuleiro[novaLinha][novaColuna] = null;
		}
		return lista;
	}
	
	public List<Jogada> jogadasPossiveisParaRainha(int linha, int coluna) {
		List<Jogada> lista = new ArrayList<Jogada>();
		Peca pecaAntiga; 
		for (int direcaoLinha = -1; direcaoLinha <= 1; direcaoLinha++) { // Direção do movimento na linha
			for (int direcaoColuna = -1; direcaoColuna <= 1; direcaoColuna++) { // Direção do movimento na coluna
				for (int incremento = 1; incremento < 8; incremento++) {
					// Determina a localização da rainha após o possível movimento
					int novaLinha = linha + incremento * direcaoLinha;
					int novaColuna = coluna + incremento * direcaoColuna;
					if (!Casa.ehValida(novaLinha, novaColuna)) {
						// Não ultrapassa o tabuleiro
						break;
					}
					pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
					if (!casaVazia(novaLinha, novaColuna) && pecaAntiga.ehAmiga()) {
						// Não passa por uma peça amiga
						break;
					}
					// Testa o possível movimento
					tabuleiro[linha][coluna] = null;
					tabuleiro[novaLinha][novaColuna] = Peca.RAINHA_AMIGA;
					// Se o movimento é válido, adiciona à lista
					if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()) {
						Casa origem = Casa.obter(linha, coluna);
						Casa destino = Casa.obter(novaLinha, novaColuna);
						Jogada jogada = new Jogada(origem, destino, pecaAntiga);
						lista.add(jogada);
					}
					// Desfaz o movimento
					tabuleiro[linha][coluna] = Peca.RAINHA_AMIGA;
					tabuleiro[novaLinha][novaColuna] = pecaAntiga;
					if (!casaVazia(novaLinha, novaColuna) && pecaAntiga.ehAdversaria()) {
						// Não passa ao comer uma peça adversária
						break;
					}
				}
			}
		}
		return lista;
	}
	
	public List<Jogada> jogadasPossiveisParaRei(int linha, int coluna) {
		List<Jogada> lista = new ArrayList<Jogada>();
		Peca pecaAntiga; 
		// Verifica as 8 casas para as quais o rei pode mover
		for (int movimento = 0; movimento < 9; movimento++) {
			if (movimento == 4) {
				// Não verifica a casa onde o rei já se encontra
				continue;
			}
			// Determina a localização do rei após o possível movimento
			int novaLinha = linha - 1 + movimento / 3;
			int novaColuna = coluna - 1 + movimento % 3;
			if (!Casa.ehValida(novaLinha, novaColuna)) {
				// Não ultrapassa o tabuleiro
				continue;
			}
			if (casaVazia(novaLinha, novaColuna) || pecaNaCasa(novaLinha, novaColuna).ehAdversaria()) {
				// Testa o possível movimento
				pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
				tabuleiro[linha][coluna] = null;
				tabuleiro[novaLinha][novaColuna] = Peca.REI_AMIGO;
				int posicaoAntigaDoReiAmigo = posicaoDoReiAmigo;
				posicaoDoReiAmigo = Casa.inteiro(novaLinha, novaColuna);
				// Se o movimento é válido, adiciona à lista
				if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()) {
					Casa origem = Casa.obter(linha, coluna);
					Casa destino = Casa.obter(novaLinha, novaColuna);
					Jogada jogada = new Jogada(origem, destino, pecaAntiga);
					lista.add(jogada);
				}
				// Desfaz o movimento
				tabuleiro[linha][coluna] = Peca.REI_AMIGO;
				tabuleiro[novaLinha][novaColuna] = null;
				posicaoDoReiAmigo = posicaoAntigaDoReiAmigo;
			}
		}
		
		// Verifica se o roque é permitido e/ou possível
		CondicoesParaRoque condicoesParaRoque;
		if (Cor.BRANCA.equals(jogador)) {
			condicoesParaRoque = condicoesParaRoqueDoBranco;
		} else {
			condicoesParaRoque = condicoesParaRoqueDoPreto;
		}
		if (condicoesParaRoque.podeFazerRoque()) {
			Casa casaDoRei, casaDaTorre;
			boolean casasVazias;
			int direcaoDoMovimento, linhaDoRei, primeiraColuna, ultimaColuna;
			boolean reiEmAtaque;
			
			// Verifica se o roque pequeno é permitido e/ou possível
			if (condicoesParaRoque.podeFazerRoquePequeno()) {
				Roque roquePequeno;
				if (Cor.BRANCA.equals(jogador)) {
					roquePequeno = Roque.obter(Roque.ROQUE_PEQUENO_DO_BRANCO);
				} else {
					roquePequeno = Roque.obter(Roque.ROQUE_PEQUENO_DO_PRETO);
				}
				casaDoRei = roquePequeno.getOrigemDoRei();
				casaDaTorre = roquePequeno.getOrigemDaTorre();
				direcaoDoMovimento = 1;
				linhaDoRei = casaDoRei.getLinha();
				
				// Verifica se as casas entre o rei e a torre estão vazias
				casasVazias = true;
				primeiraColuna = Math.min(casaDoRei.getColuna(), casaDaTorre.getColuna());
				ultimaColuna = Math.max(casaDoRei.getColuna(), casaDaTorre.getColuna());
				for (int colunaTemporaria = primeiraColuna + 1; colunaTemporaria < ultimaColuna; colunaTemporaria++) {
					Casa casaTemporaria = Casa.obter(linhaDoRei, colunaTemporaria);
					if (Cor.PRETA.equals(jogador)) {
						casaTemporaria = casaTemporaria.casaOposta();
					}
					casasVazias = (casasVazias && casaVazia(casaTemporaria));
				}
				
				reiEmAtaque = false;
				if (casasVazias) {
					// Simula o passeio do rei e verifica se em alguma posição ele é ameaçado
					// Rei anda uma casa
					Casa origemDoRei = casaDoRei;
					Casa destinoDoRei = Casa.obter(linhaDoRei, casaDoRei.getColuna() + direcaoDoMovimento);
					Jogada jogadaDeTeste = new Jogada(origemDoRei, destinoDoRei, null);
					Tabuleiro tabuleiroDeTeste = jogar(jogadaDeTeste);
					reiEmAtaque = (reiEmAtaque || !tabuleiroDeTeste.reiEstaSeguro());
					if (!reiEmAtaque) {
						// Rei anda outra casa e conclui o roque
						tabuleiroDeTeste = (Tabuleiro) clone();
						tabuleiroDeTeste = tabuleiroDeTeste.jogar(roquePequeno);
						reiEmAtaque = (reiEmAtaque || !tabuleiroDeTeste.reiEstaSeguro());
					}
				}
				
				if (casasVazias && !reiEmAtaque) {
					// Se todas as condições foram atendidas, o roque pequeno é permitido e possível
					lista.add(roquePequeno);
				}
			}
			
			// Verifica se o roque grande é permitido e/ou possível
			if (condicoesParaRoque.podeFazerRoqueGrande()) {
				Roque roqueGrande;
				if (Cor.BRANCA.equals(jogador)) {
					roqueGrande = Roque.obter(Roque.ROQUE_GRANDE_DO_BRANCO);
				} else {
					roqueGrande = Roque.obter(Roque.ROQUE_GRANDE_DO_PRETO);
				}
				casaDoRei = roqueGrande.getOrigemDoRei();
				casaDaTorre = roqueGrande.getOrigemDaTorre();
				direcaoDoMovimento = -1;
				linhaDoRei = casaDoRei.getLinha();
				
				// Verifica se as casas entre o rei e a torre estão vazias
				casasVazias = true;
				primeiraColuna = Math.min(casaDoRei.getColuna(), casaDaTorre.getColuna());
				ultimaColuna = Math.max(casaDoRei.getColuna(), casaDaTorre.getColuna());
				for (int colunaTemporaria = primeiraColuna + 1; colunaTemporaria < ultimaColuna; colunaTemporaria++) {
					Casa casaTemporaria = Casa.obter(linhaDoRei, colunaTemporaria);
					if (Cor.PRETA.equals(jogador)) {
						casaTemporaria = casaTemporaria.casaOposta();
					}
					casasVazias = (casasVazias && casaVazia(casaTemporaria));
				}
				
				reiEmAtaque = false;
				if (casasVazias) {
					// Simula o passeio do rei e verifica se em alguma posição ele é ameaçado
					// Rei anda uma casa
					Casa origemDoRei = casaDoRei;
					Casa destinoDoRei = Casa.obter(linhaDoRei, casaDoRei.getColuna() + direcaoDoMovimento);
					Jogada jogadaDeTeste = new Jogada(origemDoRei, destinoDoRei, null);
					Tabuleiro tabuleiroDeTeste = jogar(jogadaDeTeste);
					reiEmAtaque = (reiEmAtaque || !tabuleiroDeTeste.reiEstaSeguro());
					if (!reiEmAtaque) {
						// Rei anda outra casa e conclui o roque
						tabuleiroDeTeste = (Tabuleiro) clone();
						tabuleiroDeTeste = tabuleiroDeTeste.jogar(roqueGrande);
						reiEmAtaque = (reiEmAtaque || !tabuleiroDeTeste.reiEstaSeguro());
					}
				}
				
				if (casasVazias && !reiEmAtaque) {
					// Se todas as condições foram atendidas, o roque pequeno é permitido e possível
					lista.add(roqueGrande);
				}
			}
		}
		return lista;
	}
	
	public List<Jogada> jogadasPossiveisParaTorre(int linha, int coluna) {
		List<Jogada> lista = new ArrayList<Jogada>();
		Peca pecaAntiga;
		int novaLinha, novaColuna;
		int temp=1;
		for (int j=-1;j<=1;j+=2) {
			//movimentos na horizontal
			temp=1;
			novaLinha = linha;
			novaColuna = coluna + temp*j;
			while(Casa.ehValida(novaLinha, novaColuna) && casaVazia(novaLinha, novaColuna)){
				pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
				tabuleiro[linha][coluna] = null;
				tabuleiro[novaLinha][novaColuna] = Peca.TORRE_AMIGA;
				if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()){
					Casa origem = Casa.obter(linha, coluna);
					Casa destino = Casa.obter(novaLinha, novaColuna);
					Jogada jogada = new Jogada(origem, destino, pecaAntiga);
					lista.add(jogada);
				}
				tabuleiro[linha][coluna] = Peca.TORRE_AMIGA;
				tabuleiro[novaLinha][novaColuna] = pecaAntiga;
				temp++;
				novaColuna = coluna + temp*j;
			}
			//captura na horizontal
			if (Casa.ehValida(novaLinha, novaColuna) && !casaVazia(novaLinha, novaColuna) && pecaNaCasa(novaLinha, novaColuna).ehAdversaria()){
				pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
				Casa origem = Casa.obter(linha, coluna);
				Casa destino = Casa.obter(novaLinha, novaColuna);
				Jogada jogada = new Jogada(origem, destino, pecaAntiga);
				lista.add(jogada);
			}
			//movimentos na vertical
			temp = 1;
			novaLinha = linha + temp*j;
			novaColuna = coluna;
			while(Casa.ehValida(novaLinha, novaColuna) && casaVazia(novaLinha, novaColuna)){
				pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
				tabuleiro[linha][coluna] = null;
				tabuleiro[novaLinha][novaColuna] = Peca.TORRE_AMIGA;
				if (!Peca.REI_ADVERSARIO.equals(pecaAntiga) && reiEstaSeguro()){
					Casa origem = Casa.obter(linha, coluna);
					Casa destino = Casa.obter(novaLinha, novaColuna);
					Jogada jogada = new Jogada(origem, destino, pecaAntiga);
					lista.add(jogada);
				}
				tabuleiro[linha][coluna] = Peca.TORRE_AMIGA;
				tabuleiro[novaLinha][novaColuna] = pecaAntiga;
				temp++;
				novaLinha = linha + temp*j;
			}
			temp = 1;
			//captura na vertical
			if (Casa.ehValida(novaLinha, novaColuna) && !casaVazia(novaLinha, novaColuna) && pecaNaCasa(novaLinha, novaColuna).ehAdversaria()){
				pecaAntiga = pecaNaCasa(novaLinha, novaColuna);
				Casa origem = Casa.obter(linha, coluna);
				Casa destino = Casa.obter(novaLinha, novaColuna);
				Jogada jogada = new Jogada(origem, destino, pecaAntiga);
				lista.add(jogada);
			}
		}
		return lista;
	}
	
	public Tabuleiro jogar(Jogada jogada) {
		Tabuleiro novoTabuleiro = (Tabuleiro) clone();
		boolean jogadorPreto = Cor.PRETA.equals(novoTabuleiro.jogador);
		if (jogadorPreto) {
			jogada.virar();
		}
		int linha = jogada.getOrigem().getLinha();
		int coluna = jogada.getOrigem().getColuna();
		int novaLinha = jogada.getDestino().getLinha();
		int novaColuna = jogada.getDestino().getColuna();
		Peca pecaMovida;
		if (jogada.ocorrePromocao()) {
			pecaMovida = jogada.getPecaPromovida();
		} else if (jogada instanceof Roque) {
			pecaMovida = Peca.REI_AMIGO;
			Roque roque = (Roque) jogada;
			// Se o movimento é um roque, move também a torre
			int colunaDaTorre = roque.getOrigemDaTorre().getColuna();
			int novaColunaDaTorre = roque.getDestinoDaTorre().getColuna();
			novoTabuleiro.tabuleiro[linha][colunaDaTorre] = null;
			novoTabuleiro.tabuleiro[linha][novaColunaDaTorre] = Peca.TORRE_AMIGA;
		} else {
			pecaMovida = novoTabuleiro.pecaNaCasa(linha, coluna);
		}
		novoTabuleiro.tabuleiro[linha][coluna] = null;
		novoTabuleiro.tabuleiro[novaLinha][novaColuna] = pecaMovida;
		// Atualiza posição do rei, se a peça movida foi o rei
		if (Peca.REI_AMIGO.equals(pecaMovida)) {
			novoTabuleiro.posicaoDoReiAmigo = Casa.inteiro(novaLinha, novaColuna);
		}
		if (jogadorPreto) {
			jogada.virar();
		}
		// Verifica se o movimento envolveu uma torre e/ou o rei e inviabiliza futuros roques
		CondicoesParaRoque condicoesParaRoque;
		int colunaA = Casa.coluna('A'); // 0, para o branco
		int colunaH = Casa.coluna('H'); // 7, para o branco
		if (jogadorPreto) {
			condicoesParaRoque = novoTabuleiro.condicoesParaRoqueDoPreto;
			colunaA = Casa.colunaOposta(colunaA); // 7, para o preto
			colunaH = Casa.colunaOposta(colunaH); // 0, para o preto
		} else {
			condicoesParaRoque = novoTabuleiro.condicoesParaRoqueDoBranco;
		}
		if (Peca.REI_AMIGO.equals(pecaMovida) && !condicoesParaRoque.jaMoveuRei()) {
			condicoesParaRoque.setJaMoveuRei(true);
			if (jogada instanceof Roque) {
				if (jogadorPreto) {
					if (Roque.obter(Roque.ROQUE_PEQUENO_DO_PRETO).equals(jogada)) {
						condicoesParaRoque.setJaMoveuTorreNaColunaH(true);
					} else if (Roque.obter(Roque.ROQUE_GRANDE_DO_PRETO).equals(jogada)) {
						condicoesParaRoque.setJaMoveuTorreNaColunaA(true);
					}
				} else {
					if (Roque.obter(Roque.ROQUE_PEQUENO_DO_BRANCO).equals(jogada)) {
						condicoesParaRoque.setJaMoveuTorreNaColunaH(true);
					} else if (Roque.obter(Roque.ROQUE_GRANDE_DO_BRANCO).equals(jogada)) {
						condicoesParaRoque.setJaMoveuTorreNaColunaA(true);
					}
				}
			}
		}
		if ((linha == 0) && (coluna == colunaA) && Peca.TORRE_AMIGA.equals(pecaMovida) && !condicoesParaRoque.jaMoveuTorreNaColunaA()) {
			condicoesParaRoque.setJaMoveuTorreNaColunaA(true);
		}
		if ((linha == 0) && (coluna == colunaH) && Peca.TORRE_AMIGA.equals(pecaMovida) && !condicoesParaRoque.jaMoveuTorreNaColunaH()) {
			condicoesParaRoque.setJaMoveuTorreNaColunaH(true);
		}
		return novoTabuleiro;
	}
	
	public Peca pecaNaCasa(Casa casa) {
		return tabuleiro[casa.getLinha()][casa.getColuna()];
	}
	
	public Peca pecaNaCasa(int casa) {
		return tabuleiro[Casa.linha(casa)][Casa.coluna(casa)];
	}
	
	public Peca pecaNaCasa(int linha, int coluna) {
		return tabuleiro[linha][coluna];
	}
	
	public boolean reiEstaSeguro() {
		int temp=1;
		int linhaDoRei;
		int colunaDoRei;
		Peca peca;
		//Verica xeque com bispo ou rainha nas diagonais
		for (int i=-1;i<=1;i+=2) {
			for (int j=-1;j<=1;j+=2) {
				linhaDoRei = posicaoDoReiAmigo/8 + temp*i;
				colunaDoRei = posicaoDoReiAmigo%8 + temp*j;
				
				//calcular fronteira da posição do rei
				while(Casa.ehValida(linhaDoRei, colunaDoRei) && casaVazia(linhaDoRei, colunaDoRei)){
					temp++;
					linhaDoRei = posicaoDoReiAmigo/8 + temp*i;
					colunaDoRei = posicaoDoReiAmigo%8 + temp*j;
				}
				temp=1;
				if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
					peca = pecaNaCasa(linhaDoRei, colunaDoRei);
					if (Peca.BISPO_ADVERSARIO.equals(peca) || Peca.RAINHA_ADVERSARIA.equals(peca))
						return false;
				}
			}
		}
		//Verifica xeque com torre ou rainha na vertical e horizontal
		for (int i=-1;i<=1;i+=2) {
			//xeque na horizontal
			linhaDoRei = posicaoDoReiAmigo/8;
			colunaDoRei = posicaoDoReiAmigo%8+temp*i;
			while(Casa.ehValida(linhaDoRei,colunaDoRei) && casaVazia(linhaDoRei,colunaDoRei)){
				temp++;
				colunaDoRei = posicaoDoReiAmigo%8+temp*i;
			}
			temp=1;
			if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
				peca = pecaNaCasa(linhaDoRei, colunaDoRei);
				if (Peca.TORRE_ADVERSARIA.equals(peca) || Peca.RAINHA_ADVERSARIA.equals(peca))
					return false;
			}
            
            //xeque na vertical
			linhaDoRei = posicaoDoReiAmigo/8 + temp*i;
			colunaDoRei = posicaoDoReiAmigo%8;
			while(Casa.ehValida(linhaDoRei,colunaDoRei) && casaVazia(linhaDoRei,colunaDoRei)){
				temp++;
				linhaDoRei = posicaoDoReiAmigo/8 + temp*i;
			}
			temp=1;
			if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
				peca = pecaNaCasa(linhaDoRei, colunaDoRei);
				if (Peca.TORRE_ADVERSARIA.equals(peca) || Peca.RAINHA_ADVERSARIA.equals(peca))
					return false;
			}
		}
		//Verifica xeque com cavalo
		for (int i=-1;i<=1;i+=2) {
			for (int j=-1;j<=1;j+=2) {
				linhaDoRei = posicaoDoReiAmigo/8 + i;
				colunaDoRei = posicaoDoReiAmigo%8 + j*2;
				if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
					peca = pecaNaCasa(linhaDoRei, colunaDoRei);
					if (Peca.CAVALO_ADVERSARIO.equals(peca))
						return false;
				}
				linhaDoRei = posicaoDoReiAmigo/8 + i*2;
				colunaDoRei = posicaoDoReiAmigo%8 + j;
				if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
					peca = pecaNaCasa(linhaDoRei, colunaDoRei);
					if (Peca.CAVALO_ADVERSARIO.equals(peca))
						return false;
				}
			}
		}
		//Verifica xeque com peão
		if (posicaoDoReiAmigo >= 16){
			linhaDoRei = posicaoDoReiAmigo/8 - 1;
			colunaDoRei = posicaoDoReiAmigo%8 - 1;
			if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
				peca = pecaNaCasa(linhaDoRei, colunaDoRei);
				if (Peca.PEAO_ADVERSARIO.equals(peca))
					return false;
			}
			linhaDoRei = posicaoDoReiAmigo/8 + 1;
			colunaDoRei = posicaoDoReiAmigo%8 + 1;
			if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
				peca = pecaNaCasa(linhaDoRei, colunaDoRei);
				if (Peca.PEAO_ADVERSARIO.equals(peca))
					return false;
			}
		}
		//Verifica se rei adversário impede movimento do rei amigo
		for (int i=-1;i<=1;i+=2) {
			for (int j=-1;j<=1;j+=2) {
				if (i != 0 || j != 0){
					linhaDoRei = posicaoDoReiAmigo/8 +i;
					colunaDoRei = posicaoDoReiAmigo%8 +j;
					if (Casa.ehValida(linhaDoRei, colunaDoRei) && !casaVazia(linhaDoRei, colunaDoRei)){
						peca = pecaNaCasa(linhaDoRei, colunaDoRei);
						if (Peca.PEAO_ADVERSARIO.equals(peca))
							return false;
					}
				}
			}
		}
		return true;
	}
	
	public void setPosicaoDoReiAmigo(int casa){
		posicaoDoReiAmigo = casa;
	}
	
	@Override
	public String toString() {
		boolean jogadorPreto = Cor.PRETA.equals(jogador);
		if (jogadorPreto) {
			virar();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		for (int coluna = 0; coluna < 8; coluna++){
			sb.append(" " + Casa.colunaComoCaractere(coluna) + " ");
		}
		sb.append("\n");
		for (int linha = 0; linha < 8; linha++) {
			sb.append(Casa.linhaComoCaractere(linha));
			for (int coluna = 0; coluna < 8; coluna++) {
				if (casaVazia(linha, coluna)) {
					sb.append(" - ");
				} else {
					Peca peca = tabuleiro[linha][coluna];
					sb.append(" " + peca.toString() + " ");
				}
			}
			if (linha < 7) {
				sb.append("\n");
			}
		}
		if (jogadorPreto) {
			virar();
		}
		return sb.toString();
	}
	
	public void virar() {
		// Itera apenas por metade do tabuleiro
		for (int linha = 0; linha < 4; linha++) {
			for (int coluna = 0; coluna < 8; coluna++) {
				Peca peca = tabuleiro[linha][coluna];
				
				int linhaOposta = Casa.linhaOposta(linha);
				int colunaOposta = Casa.colunaOposta(coluna);
				Peca outraPeca = tabuleiro[linhaOposta][colunaOposta];
				
				tabuleiro[linha][coluna] = Peca.pecaOposta(outraPeca);
				tabuleiro[linhaOposta][colunaOposta] = Peca.pecaOposta(peca);
			}
		}
		int posicaoDoReiAmigo = this.posicaoDoReiAmigo;
		this.posicaoDoReiAmigo = Casa.casaOposta(posicaoDoReiAdversario);
		posicaoDoReiAdversario = Casa.casaOposta(posicaoDoReiAmigo);
		jogador = jogador.corOposta();
	}
}