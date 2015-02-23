public class Aresta {
	private final No filho;
	private final Jogada jogada;
	private final No pai;

	public Aresta(No pai, No filho, Jogada jogada) {
		this.pai = pai;
		this.filho = filho;
		this.jogada = jogada;
	}
	
	public No getFilho() {
		return filho;
	}
	
	public Jogada getJogada() {
		return jogada;
	}
	
	public No getPai() {
		return pai;
	}

}