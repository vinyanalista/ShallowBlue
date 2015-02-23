public enum Peca {
	BISPO_AMIGO(TipoDePeca.BISPO, Lado.AMIGO) {
		@Override
		public char comoCaractere() {
			return 'B';
		}

		@Override
		public Peca pecaOposta() {
			return BISPO_ADVERSARIO;
		}
	},
	BISPO_ADVERSARIO(TipoDePeca.BISPO, Lado.ADVERSARIO) {
		@Override
		public char comoCaractere() {
			return 'b';
		}

		@Override
		public Peca pecaOposta() {
			return BISPO_AMIGO;
		}
	},
	CAVALO_AMIGO(TipoDePeca.CAVALO, Lado.AMIGO) {
		@Override
		public char comoCaractere() {
			return 'K';
		}

		@Override
		public Peca pecaOposta() {
			return CAVALO_ADVERSARIO;
		}
	},
	CAVALO_ADVERSARIO(TipoDePeca.CAVALO, Lado.ADVERSARIO) {
		@Override
		public char comoCaractere() {
			return 'k';
		}

		@Override
		public Peca pecaOposta() {
			return CAVALO_AMIGO;
		}
	},
	PEAO_AMIGO(TipoDePeca.PEAO, Lado.AMIGO) {
		@Override
		public char comoCaractere() {
			return 'P';
		}

		@Override
		public Peca pecaOposta() {
			return PEAO_ADVERSARIO;
		}
	},
	PEAO_ADVERSARIO(TipoDePeca.PEAO, Lado.ADVERSARIO) {
		@Override
		public char comoCaractere() {
			return 'p';
		}

		@Override
		public Peca pecaOposta() {
			return PEAO_AMIGO;
		}
	},
	RAINHA_AMIGA(TipoDePeca.RAINHA, Lado.AMIGO) {
		@Override
		public char comoCaractere() {
			return 'Q';
		}

		@Override
		public Peca pecaOposta() {
			return RAINHA_ADVERSARIA;
		}
	},
	RAINHA_ADVERSARIA(TipoDePeca.RAINHA, Lado.ADVERSARIO) {
		@Override
		public char comoCaractere() {
			return 'q';
		}

		@Override
		public Peca pecaOposta() {
			return RAINHA_AMIGA;
		}
	},
	REI_AMIGO(TipoDePeca.REI, Lado.AMIGO) {
		@Override
		public char comoCaractere() {
			return 'A';
		}

		@Override
		public Peca pecaOposta() {
			return REI_ADVERSARIO;
		}
	},
	REI_ADVERSARIO(TipoDePeca.REI, Lado.ADVERSARIO) {
		@Override
		public char comoCaractere() {
			return 'a';
		}

		@Override
		public Peca pecaOposta() {
			return REI_AMIGO;
		}
	},
	TORRE_AMIGA(TipoDePeca.TORRE, Lado.AMIGO) {
		@Override
		public char comoCaractere() {
			return 'R';
		}

		@Override
		public Peca pecaOposta() {
			return TORRE_ADVERSARIA;
		}
	},
	TORRE_ADVERSARIA(TipoDePeca.TORRE, Lado.ADVERSARIO) {
		@Override
		public char comoCaractere() {
			return 'r';
		}

		@Override
		public Peca pecaOposta() {
			return TORRE_AMIGA;
		}
	};
	
	public static Peca obter(char peca) {
		for (Peca pecaPossivel : Peca.values()) {
			if (pecaPossivel.comoCaractere() == peca) {
				return pecaPossivel;
			}
		}
		return null;
	}
	
	public static Peca pecaOposta(Peca peca) {
		if (peca == null) {
			return null;
		} else {
			return peca.pecaOposta();
		}
	}
	
	private final Lado lado;
	private final TipoDePeca tipo;

	private Peca(TipoDePeca tipo, Lado lado) {
		this.lado = lado;
		this.tipo = tipo;
	}
	
	public abstract char comoCaractere();
	
	public boolean ehAdversaria() {
		return Lado.ADVERSARIO.equals(lado);
	}
	
	public boolean ehAmiga() {
		return Lado.AMIGO.equals(lado);
	}
	
	public Lado getLado() {
		return lado;
	}
	
	public TipoDePeca getTipo() {
		return tipo;
	}
	
	public abstract Peca pecaOposta();
	
	@Override
	public String toString() {
		return "" + comoCaractere();
	}

}