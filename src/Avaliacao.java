import java.util.List;

public class Avaliacao {
	public static int avaliar(Tabuleiro tabuleiro, int profundidade) {
		int nota = 0;
		// Aumenta a nota para condições favoráveis ao jogador
		int notaDasPecas = avaliarPecas(tabuleiro);
		nota += notaDasPecas;
		nota += avaliarPosicoes(tabuleiro, notaDasPecas);
		nota += avaliarAmeaca(tabuleiro);
		nota += avaliarMobilidade(tabuleiro, profundidade);
		
		tabuleiro.virar();
		
		// Reduz a nota para condições favoráveis ao adversário
		notaDasPecas = avaliarPecas(tabuleiro);
		nota -= notaDasPecas;
		nota -= avaliarPosicoes(tabuleiro, notaDasPecas);
		nota -= avaliarAmeaca(tabuleiro);
		nota -= avaliarMobilidade(tabuleiro, profundidade);
		tabuleiro.virar();
		
		// A precisão da avaliação aumenta com a profundidade
		return -(nota + (50 * profundidade));
	}

	public static int avaliarAmeaca(Tabuleiro tabuleiro) {
		int nota = 0;
		int posicaoOriginalDoRei = tabuleiro.getPosicaoDoReiAmigo();
		for (int casa = 0; casa < 64; casa++){
			if (tabuleiro.casaVazia(casa)){
				continue;
			}
			switch (tabuleiro.pecaNaCasa(casa)) {
			case PEAO_AMIGO:
				tabuleiro.setPosicaoDoReiAmigo(casa);
				if (!tabuleiro.reiEstaSeguro())
					nota -= 64;
				break;
			case CAVALO_AMIGO:
				tabuleiro.setPosicaoDoReiAmigo(casa);
				if (!tabuleiro.reiEstaSeguro())
					nota -= 300;
				break;
			case BISPO_AMIGO:
				tabuleiro.setPosicaoDoReiAmigo(casa);
				if (!tabuleiro.reiEstaSeguro())
					nota -= 300;
				break;
			case TORRE_AMIGA:
				tabuleiro.setPosicaoDoReiAmigo(casa);
				if (!tabuleiro.reiEstaSeguro())
					nota -= 500;
				break;
			case RAINHA_AMIGA:
				tabuleiro.setPosicaoDoReiAmigo(casa);
				if (!tabuleiro.reiEstaSeguro())
					nota -= 900;
				break;		
			default:
				break;
			}
		}
		//rei retorna para posicao original 
		tabuleiro.setPosicaoDoReiAmigo(posicaoOriginalDoRei);
		//penaliza nota caso esteja em xeque
		if (!tabuleiro.reiEstaSeguro())
			nota -= 400;
		
		return nota/2;
	}

	public static int avaliarMobilidade(Tabuleiro tabuleiro, int profundidade) {
		int nota = 0;
		List<Jogada> jogadasPossiveis = tabuleiro.jogadasPossiveis();
		int quantidadeDeJogadasDisponiveis = jogadasPossiveis.size();
        nota += (quantidadeDeJogadasDisponiveis * 5); // 5 pontos por jogada disponível
        if (quantidadeDeJogadasDisponiveis == 0) {
            if (!tabuleiro.reiEstaSeguro()) {
            	// Xeque-mate - o rei está sob ataque e não há como escapar
                nota += (-200000 * profundidade);
            } else {
            	// Empate - não há jogadas disponíveis, apesar de o rei não estar em xeque
                nota += (-150000 * profundidade);
            }
        }
        return nota;
	}
	
	public static int avaliarPecas(Tabuleiro tabuleiro) {
		int nota = 0, quantidadeDeBispos = 0;
		for (int linha = 0; linha < 8; linha++) {
			for (int coluna = 0; coluna < 8; coluna++) {
	        	if (tabuleiro.casaVazia(linha, coluna)) {
	        		continue;
	        	}
	            switch (tabuleiro.pecaNaCasa(linha, coluna)) {
	            case BISPO_AMIGO:
	            	quantidadeDeBispos++;
	            	break;
	            case CAVALO_AMIGO:
	            	nota += 300;
	            	break;
	            case PEAO_AMIGO:
	            	nota += 100;
	            	break;
	            case RAINHA_AMIGA:
	            	nota += 900;
	            	break;
	            case TORRE_AMIGA:
	            	nota += 500;
	            	break;
	            default:
	            	break;
	            }
			}
        }
        // Penaliza a existência de menos de 2 bispos
        if (quantidadeDeBispos >= 2) {
        	nota += (300 * quantidadeDeBispos);
        } else if (quantidadeDeBispos==1) {
        	nota += 250;
        }
        return nota;
	}

	public static int avaliarPosicoes(Tabuleiro tabuleiro, int notaDasPecas) {
		int nota = 0;
        for (int linha = 0; linha < 8; linha++) {
        	for (int coluna = 0; coluna < 8; coluna++) {
	        	if (tabuleiro.casaVazia(linha, coluna)) {
	        		continue;
	        	}
	            switch (tabuleiro.pecaNaCasa(linha, coluna)) {
	            case BISPO_AMIGO:
	            	nota += ValorDaPosicao.BISPO[linha][coluna];
	            	break;
	            case CAVALO_AMIGO:
	            	nota += ValorDaPosicao.CAVALO[linha][coluna];
	            	break;
	            case PEAO_AMIGO:
	            	nota += ValorDaPosicao.PEAO[linha][coluna];
	            	break;
	            case RAINHA_AMIGA:
	            	nota += ValorDaPosicao.RAINHA[linha][coluna];
	            	break;
	            case REI_AMIGO:
	            	if (notaDasPecas >= 1750) {
	            		nota += ValorDaPosicao.REI_NO_INICIO_DO_JOGO[linha][coluna];
	            		nota += (tabuleiro.jogadasPossiveisParaRei(linha, coluna).size() * 10);
	            	} else {
	            		nota += ValorDaPosicao.REI_NO_FIM_DO_JOGO[linha][coluna];
	            		nota += (tabuleiro.jogadasPossiveisParaRei(linha, coluna).size() * 30);
	            	}
	            	nota += ValorDaPosicao.RAINHA[linha][coluna];
	            	break;
	            case TORRE_AMIGA:
	            	nota += ValorDaPosicao.TORRE[linha][coluna];
	            	break;
	            default:
	            	break;
	            }
        	}
        }
		return nota;
	}
}