public class Jogada {
	protected Casa destino;
	protected int nota;
	protected Casa origem;
	protected Peca pecaCapturada;
	protected Peca pecaPromovida;

	public Jogada(Casa origem, Casa destino) {
		this(origem, destino, null, null);
	}
	
	public Jogada(Casa origem, Casa destino, Peca pecaCapturada) {
		this(origem, destino, pecaCapturada, null);
	}
	
	public Jogada(Casa origem, Casa destino, Peca pecaCapturada, Peca pecaPromovida) {
		this.origem = origem;
		this.destino = destino;
		this.pecaCapturada = pecaCapturada;
		this.pecaPromovida = pecaPromovida;
	}
	
	@Override
	public boolean equals(Object outroObjeto) {
		if ((outroObjeto == null) || !(outroObjeto instanceof Jogada)) {
			return false;
		}
		Jogada outraJogada = (Jogada) outroObjeto;
		boolean igual = destino.equals(outraJogada.destino);
		igual = igual && origem.equals(outraJogada.origem);
		if (pecaCapturada == null) {
			igual = igual && (outraJogada.pecaCapturada == null);
		} else {
			igual = igual && pecaCapturada.equals(outraJogada.pecaCapturada);
		}
		if (pecaPromovida == null) {
			igual = igual && (outraJogada.pecaPromovida == null);
		} else {
			igual = igual && pecaPromovida.equals(outraJogada.pecaPromovida);
		}
		return igual;
	}
	
	public Casa getDestino() {
		return destino;
	}
	
	public int getNota() {
		return nota;
	}
	
	public Casa getOrigem() {
		return origem;
	}
	
	public Peca getPecaCapturada() {
		return pecaCapturada;
	}
	
	public Peca getPecaPromovida() {
		return pecaPromovida;
	}
	
	public boolean ocorrePromocao() {
		return (pecaPromovida != null);
	}
	
	public void setNota(int nota) {
		this.nota = nota;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (pecaPromovida == null) {
			sb.append(origem.toString());
			sb.append(destino.toString());
			if (pecaCapturada == null) {
				sb.append(" ");
			} else {
				sb.append(pecaCapturada.toString());
			}
		} else {
			sb.append(origem.getColunaComoCaractere());
			sb.append(destino.getColunaComoCaractere());
			if (pecaCapturada == null) {
				sb.append(" ");
			} else {
				sb.append(pecaCapturada.toString());
			}
			sb.append(pecaPromovida.toString());
			sb.append("P");
		}
		return sb.toString();
	}
	
	public void virar() {
		destino = destino.casaOposta();
		origem = origem.casaOposta();
		pecaCapturada = Peca.pecaOposta(pecaCapturada);
		pecaPromovida = Peca.pecaOposta(pecaPromovida);
	}
	
}