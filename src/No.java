import java.util.*;

public class No {
	private final List<Aresta> arestas;
	private No pai;
	private final Tabuleiro tabuleiro;
	
	public No(Tabuleiro tabuleiro) {
		arestas = new ArrayList<Aresta>();
		pai = null;
		this.tabuleiro = tabuleiro;
	}
	
	public List<Aresta> getArestas() {
		return arestas;
	}
	
	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}
	
	public No getPai() {
		return pai;
	}
	
	public void setPai(No pai) {
		this.pai = pai;
	}

}