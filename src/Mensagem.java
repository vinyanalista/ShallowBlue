import org.json.JSONObject;

public class Mensagem {
	private String codigo;
	private String mensagem;
	private JSONObject ultimaJogada;

	public Mensagem(String codigo, String mensagem, JSONObject ultimaJogada) {
		this.codigo = codigo;
		this.mensagem = mensagem;
		this.ultimaJogada = ultimaJogada;
	}
	
	public boolean erro() {
		return (codigo.charAt(0) == '3');
	}
	
	public boolean fimDeJogo() {
		return (codigo.charAt(0) == '2');
	}

	public String getCodigo() {
		return codigo;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	public JSONObject getUltimaJogada() {
		return ultimaJogada;
	}
	
}