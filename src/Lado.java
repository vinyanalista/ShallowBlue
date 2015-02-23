public enum Lado {
	ADVERSARIO {
		@Override
		public Lado ladoOposto() {
			return AMIGO;
		}
	},
	AMIGO {
		@Override
		public Lado ladoOposto() {
			return ADVERSARIO;
		}
	};
	
	public boolean adversario(Lado jogador) {
		return !this.equals(jogador);
	}
	
	public abstract Lado ladoOposto();
}