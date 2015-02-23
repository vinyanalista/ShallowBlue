public enum TipoDePeca {
	BISPO {
		@Override
		public String toString() {
			return "B"; // Bishop
		}
	},
	CAVALO {
		@Override
		public String toString() {
			return "K"; // Knight
		}
	},
	PEAO {
		@Override
		public String toString() {
			return "P"; // Pawn
		}
	},
	RAINHA {
		@Override
		public String toString() {
			return "Q"; // Queen
		}
	},
	REI {
		@Override
		public String toString() {
			return "A"; // King
		}
	},
	TORRE {
		@Override
		public String toString() {
			return "R"; // Rook
		}
	};
}