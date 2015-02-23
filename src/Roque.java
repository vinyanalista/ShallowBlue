public class Roque extends Jogada {
	public static final int ROQUE_PEQUENO_DO_BRANCO = 0;
	public static final int ROQUE_PEQUENO_DO_PRETO = 1;
	public static final int ROQUE_GRANDE_DO_BRANCO = 2;
	public static final int ROQUE_GRANDE_DO_PRETO = 3;
	
	private static final Roque[] ROQUES;
	
	static {
		ROQUES = new Roque[4];
		
		int linhaDoRei, colunaDoRei, colunaDaTorre, novaColunaDoRei, novaColunaDaTorre;
		Casa origemDoRei, destinoDoRei, origemDaTorre, destinoDaTorre;
		
		colunaDoRei = 4; // E
		
		// Roque pequeno (mais comum)
		
		colunaDaTorre = 7; // H
		novaColunaDoRei = 6; // G
		novaColunaDaTorre = 5; // F
		
		// Roque pequeno do branco (E1G1)
		// Rei na casa E1, torre na casa H1, rei vai para a casa G1 e torre vai para a casa F1
		
		linhaDoRei = 7; // 1 para o branco
		origemDoRei = Casa.obter(linhaDoRei, colunaDoRei);
		destinoDoRei = Casa.obter(linhaDoRei, novaColunaDoRei);
		origemDaTorre = Casa.obter(linhaDoRei, colunaDaTorre);
		destinoDaTorre = Casa.obter(linhaDoRei, novaColunaDaTorre);
		ROQUES[ROQUE_PEQUENO_DO_BRANCO] = new Roque(origemDoRei, destinoDoRei, origemDaTorre, destinoDaTorre);
		
		// Roque pequeno do preto (E8G8)
		// Rei na casa E8, torre na casa H8, rei vai para a casa G8 e torre vai para a casa F8
		
		linhaDoRei = 0; // 8 para o preto
		origemDoRei = Casa.obter(linhaDoRei, colunaDoRei);
		destinoDoRei = Casa.obter(linhaDoRei, novaColunaDoRei);
		origemDaTorre = Casa.obter(linhaDoRei, colunaDaTorre);
		destinoDaTorre = Casa.obter(linhaDoRei, novaColunaDaTorre);
		ROQUES[ROQUE_PEQUENO_DO_PRETO] = new Roque(origemDoRei, destinoDoRei, origemDaTorre, destinoDaTorre);
		
		// Roque grande
		
		colunaDaTorre = 0; // A
		novaColunaDoRei = 2; // C
		novaColunaDaTorre = 3; // D
		
		// Roque grande do branco (E1C1)
		// Torre na casa A1, rei na casa E1, rei vai para a casa C1 e torre vai para a casa D1
		
		linhaDoRei = 7; // 1 para o branco
		origemDoRei = Casa.obter(linhaDoRei, colunaDoRei);
		destinoDoRei = Casa.obter(linhaDoRei, novaColunaDoRei);
		origemDaTorre = Casa.obter(linhaDoRei, colunaDaTorre);
		destinoDaTorre = Casa.obter(linhaDoRei, novaColunaDaTorre);
		ROQUES[ROQUE_GRANDE_DO_BRANCO] = new Roque(origemDoRei, destinoDoRei, origemDaTorre, destinoDaTorre);
		
		// Roque grande do preto (E8C8)
		// Torre na casa A8, rei na casa E8, rei vai para a casa C8 e torre vai para a casa D8
		
		linhaDoRei = 0; // 8 para o preto
		origemDoRei = Casa.obter(linhaDoRei, colunaDoRei);
		destinoDoRei = Casa.obter(linhaDoRei, novaColunaDoRei);
		origemDaTorre = Casa.obter(linhaDoRei, colunaDaTorre);
		destinoDaTorre = Casa.obter(linhaDoRei, novaColunaDaTorre);
		ROQUES[ROQUE_GRANDE_DO_PRETO] = new Roque(origemDoRei, destinoDoRei, origemDaTorre, destinoDaTorre);
		
		// Fonte: http://pt.wikipedia.org/wiki/Roque_%28xadrez%29
	}
	
	public static Roque obter(int roque) {
		return (Roque) ROQUES[roque].clone();
	}
	
	private Casa destinoDaTorre;
	private Casa origemDaTorre;

	private Roque(Casa origemDoRei, Casa destinoDoRei, Casa origemDaTorre, Casa destinoDaTorre) {
		super(origemDoRei, destinoDoRei, null);
		this.origemDaTorre = origemDaTorre;
		this.destinoDaTorre = destinoDaTorre;
	}
	
	@Override
	protected Object clone() {
		return new Roque(origem, destino, origemDaTorre, destinoDaTorre);
	}
	
	@Override
	public boolean equals(Object outroObjeto) {
		if (!(outroObjeto instanceof Roque)) {
			return false;
		}
		Roque outroRoque = (Roque) outroObjeto;
		boolean igual = destino.equals(outroRoque.destino);
		igual = igual && destinoDaTorre.equals(outroRoque.destinoDaTorre);
		igual = igual && origem.equals(outroRoque.origem);
		igual = igual && origemDaTorre.equals(outroRoque.origemDaTorre);
		return igual;
	}
	
	public Casa getDestinoDaTorre() {
		return destinoDaTorre;
	}
	
	public Casa getDestinoDoRei() {
		return destino;
	}
	
	public Casa getOrigemDaTorre() {
		return origemDaTorre;
	}
	
	public Casa getOrigemDoRei() {
		return origem;
	}
	
	@Override
	public void virar() {
		// Tomar cuidado ao virar o roque, pois os roques do branco e do preto não são simétricos
		super.virar();
		destinoDaTorre = destinoDaTorre.casaOposta();
		origemDaTorre = origemDaTorre.casaOposta();
	}
	
}