public class CondicoesParaRoque {
	// Para se poder efetuar um roque, as seguintes condições são necessárias:
	// - Rei nunca foi movido
	// - Torre a usar no roque nunca foi movida
	// - Rei não está em xeque
	// - Nenhuma das casas pelas quais o rei irá passar ou ficar sob ataque.
	// - Casas entre o rei e a torre estão desocupadas
	// Fonte: http://pt.wikipedia.org/wiki/Roque_%28xadrez%29
	
	private boolean jaMoveuRei;
	private boolean jaMoveuTorreNaColunaA;
	private boolean jaMoveuTorreNaColunaH;
	
	public CondicoesParaRoque() {
		this(false, false, false);
	}
	
	public CondicoesParaRoque(boolean jaMoveuRei,
			boolean jaMoveuTorreNaColunaA, boolean jaMoveuTorreNaColunaH) {
		this.jaMoveuRei = jaMoveuRei;
		this.jaMoveuTorreNaColunaA = jaMoveuTorreNaColunaA;
		this.jaMoveuTorreNaColunaH = jaMoveuTorreNaColunaH;
	}
	
	@Override
	protected Object clone() {
		CondicoesParaRoque clone = new CondicoesParaRoque(this.jaMoveuRei,
				this.jaMoveuTorreNaColunaA, this.jaMoveuTorreNaColunaH);
		return clone;
	}

	public boolean jaMoveuRei() {
		return jaMoveuRei;
	}
	
	public boolean jaMoveuTorreNaColunaA() {
		return jaMoveuTorreNaColunaA;
	}

	public boolean jaMoveuTorreNaColunaH() {
		return jaMoveuTorreNaColunaH;
	}
	
	public boolean podeFazerRoque() {
		return (!jaMoveuRei && (!jaMoveuTorreNaColunaA | !jaMoveuTorreNaColunaH));
	}
	
	public boolean podeFazerRoqueGrande() {
		return (!jaMoveuRei && !jaMoveuTorreNaColunaA);
	}
	
	public boolean podeFazerRoquePequeno() {
		return (!jaMoveuRei && !jaMoveuTorreNaColunaH);
	}
	
	public void setJaMoveuRei(boolean jaMoveuRei) {
		this.jaMoveuRei = jaMoveuRei;
	}
	
	public void setJaMoveuTorreNaColunaA(boolean jaMoveuTorreNaColunaA) {
		this.jaMoveuTorreNaColunaA = jaMoveuTorreNaColunaA;
	}
	
	public void setJaMoveuTorreNaColunaH(boolean jaMoveuTorreNaColunaH) {
		this.jaMoveuTorreNaColunaH = jaMoveuTorreNaColunaH;
	}
	
}