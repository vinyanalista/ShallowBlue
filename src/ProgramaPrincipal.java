import br.com.vinyanalista.jchessengine.*;

public class ProgramaPrincipal {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("-xboard")) {
			InterfaceXboard engine = new InterfaceXboard();
			XBoard.setEngine(engine);
			engine.setDebug(true);

			engine.debug("Engine iniciada");

			try {
				engine.debug("Escutando comandos do XBoard/WinBoard");
				XBoard.listen();
			} catch (Exception excecao) {
				engine.debug("Erro inesperado: " + excecao.getMessage());
				excecao.printStackTrace();
			}
		} else {
			new InterfaceServidor();
		}
	}
}