public enum Cor {
	PRETA {
		@Override
		public Cor corOposta() {
			return BRANCA;
		}
	},
	BRANCA {
		@Override
		public Cor corOposta() {
			return PRETA;
		}
	};
	
	public boolean adversario(Cor corDoJogador) {
		return !this.equals(corDoJogador);
	}
	
	public abstract Cor corOposta();
}