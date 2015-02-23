public class Casa {
	private static final Casa[][] TABULEIRO;
	
	static {
		TABULEIRO = new Casa[8][8];
		for (int linha = 0; linha < 8; linha++) {
			for (int coluna = 0; coluna < 8; coluna++) {
				StringBuilder representacao = new StringBuilder();
				representacao.append(colunaComoCaractere(coluna));
				representacao.append(8 - linha);
				Casa casa = new Casa(linha, coluna, representacao.toString());
				TABULEIRO[linha][coluna] = casa;
			}
		}
	}
	
	public static int casaOposta(int casa) {
		return 63 - casa;
	}
	
	public static int coluna(int casa) {
		return casa % 8;
	}
	
	public static int coluna(char letra) {
		switch (Character.toUpperCase(letra)) {
		case 'A':
			return 0;
		case 'B':
			return 1;
		case 'C':
			return 2;
		case 'D':
			return 3;
		case 'E':
			return 4;
		case 'F':
			return 5;
		case 'G':
			return 6;
		case 'H':
			return 7;
		default:
			return -1;
		}
	}
	
	public static char colunaComoCaractere(int coluna) {
		switch (coluna) {
		case 0:
			return 'A';
		case 1:
			return 'B';
		case 2:
			return 'C';
		case 3:
			return 'D';
		case 4:
			return 'E';
		case 5:
			return 'F';
		case 6:
			return 'G';
		case 7:
			return 'H';
		default:
			return 'X';
		}
	}
	
	public static int colunaOposta(int coluna) {
		return 7 - coluna;
	}
	
	public static int inteiro(int linha, int coluna) {
		return linha * 8 + coluna;
	}
	
	public static int linha(int casa) {
		return casa / 8;
	}
	
	public static int linha(char letra) {
		return 8 - Character.getNumericValue(letra);
	}
	
	public static char linhaComoCaractere(int linha) {
		// http://stackoverflow.com/questions/833709/converting-int-to-char-in-java
		linha = 8 - linha;
		return (char) ('0' + linha);
	}
	
	public static int linhaOposta(int linha) {
		return 7 - linha;
	}
	
	public static Casa obter(int inteiro) {
		return TABULEIRO[linha(inteiro)][coluna(inteiro)];
	}
	
	public static Casa obter(int linha, int coluna) {
		return TABULEIRO[linha][coluna];
	}
	
	public static boolean ehValida(int linha, int coluna) {
		return ((linha > -1) && (linha < 8) && (coluna > -1) && (coluna < 8));
	}
	
	private final int inteiro;
	private final String representacao;

	private Casa(int linha, int coluna, String representacao) {
		inteiro = inteiro(linha, coluna); 
		this.representacao = representacao;
	}
	
	public Casa casaOposta() {
		return obter(linhaOposta(linha(inteiro)), colunaOposta(coluna(inteiro)));
	}
	
	@Override
	public boolean equals(Object outroObjeto) {
		if ((outroObjeto == null) || !(outroObjeto instanceof Casa)) {
			return false;
		}
		Casa outraCasa = (Casa) outroObjeto;
		return (inteiro == outraCasa.inteiro);
	}
	
	public int getColuna() {
		return coluna(inteiro);
	}
	
	public char getColunaComoCaractere() {
		return representacao.charAt(0);
	}
	
	public int getLinha() {
		return linha(inteiro);
	}
	
	public char getLinhaComoCaractere() {
		return representacao.charAt(1);
	}
	
	public int inteiro() {
		return inteiro;
	}
	
	@Override
	public String toString() {
		return representacao;
	}
	
}