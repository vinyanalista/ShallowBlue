import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

import org.json.*;

import br.com.cliente.TXadrezClient;

public class InterfaceServidor extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final String ENDERECO_PADRAO_LOCAL = "localhost";
	private static final String ENDERECO_PADRAO_ONLINE = "xadrez.tigersoft.com.br";
	private static final String JOGADOR_PADRAO = "ShallowBlue";
	private static final String PORTA_PADRAO_LOCAL = "8080";
	private static final String PORTA_PADRAO_ONLINE = "8113";
	
	private EscutaDoServidor escuta;
	private JTextArea textAreaLog;
	private TelaTabuleiro telaTabuleiro;
	
	private TXadrezClient cliente;
	private String endereco;
	private String id;
	private int porta;
	
	private void conectar() {
		String[] botoes = new String[] {ENDERECO_PADRAO_LOCAL, ENDERECO_PADRAO_ONLINE, "Outro", "Cancelar"};
		int botaoClicado = JOptionPane.showOptionDialog(this,
				"Informe o endereço do servidor", "Conectar",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, botoes, botoes[0]);
		switch (botaoClicado) {
		case 2:
			endereco = JOptionPane
					.showInputDialog("Informe o endereço do servidor:");
			if (endereco == null) {
				return;
			}
			break;
		case 3:
			return;
		default:
			endereco = botoes[botaoClicado];
			break;
		}
		
		botoes = new String[] {PORTA_PADRAO_LOCAL, PORTA_PADRAO_ONLINE, "Outra", "Cancelar"};
		botaoClicado = JOptionPane.showOptionDialog(this,
				"Informe a porta do servidor", "Conectar",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, botoes, botoes[0]);
		String porta;
		switch (botaoClicado) {
		case 2:
			porta = JOptionPane
					.showInputDialog("Informe a porta do servidor:");
			if (porta == null) {
				return;
			}
			break;
		case 3:
			return;
		default:
			porta = botoes[botaoClicado];
			break;
		}
		this.porta = Integer.valueOf(porta);
		
		log("Tentando conectar ao servidor...");
		log("Endereço: " + endereco);
		log("Porta: " + porta);
		cliente = new TXadrezClient(endereco, this.porta);
		
		String jogador = JOptionPane.showInputDialog(this,
				"Informe um nome para o jogador:", JOGADOR_PADRAO
						+ (1 + new Random().nextInt(100)));
		if (jogador == null) {
			desconectar();
			return;
		}
		
		log("Nome do jogador: " + jogador);
		log("Solicitando ID para o jogador...");
		
		// http://www.devmedia.com.br/trabalhando-com-json-em-java-o-pacote-org-json/25480
		JSONObject resposta;
		try {
			String solicitarId = cliente.SolicitarIdJogador(jogador, true);
			if (solicitarId.isEmpty()) {
				tratarErro("Não foi possível conectar ao servidor");
				return;
			}
			resposta = new JSONObject(solicitarId);
			resposta = new JSONObject(resposta.getJSONArray("result").getString(0));
		} catch (Exception excecao1) {
			try {
				resposta = new JSONObject(excecao1.getMessage());
				resposta = new JSONObject(resposta.getString("error"));
				String codigo = resposta.getString("codigo");
				String mensagem = resposta.getString("mensagem");
				tratarErro(codigo, mensagem);
			} catch (Exception excecao2) {
				tratarErro(excecao2.getMessage());
			}
			return;
		}
		id = resposta.getString("id_jogador");
		log("Conectado!");
		log("ID: " + id);
		
		escutar();
	}
	
	synchronized void desconectar() {
		interromperEscuta();
		log("Desconectado");
	}
	
	synchronized void desenharTabuleiro(Tabuleiro tabuleiro) {
		telaTabuleiro.desenhar(tabuleiro);
	}
	
	synchronized void desenharTabuleiro(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		telaTabuleiro.desenhar(tabuleiro, ultimaJogada);
	}
	
	private void escutar() {
		escuta = new EscutaDoServidor(cliente, id, this);
		new Thread(escuta).start();
	}
	
	private void fechar() {
		interromperEscuta();
		setVisible(false);
		System.exit(0);
	}
	
	synchronized void fimDeJogo(String mensagem) {
		log(mensagem);
		desconectar();
		JOptionPane.showMessageDialog(this, mensagem);
	}
	
	private void interromperEscuta() {
		if (escuta != null) {
			escuta.interromper();
			escuta = null;
		}
	}
	
	synchronized void log(String mensagem) {
		textAreaLog.append(mensagem + "\n");
		// http://stackoverflow.com/questions/6243086/java-swing-how-to-scroll-down-a-jtextarea
		textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
	}
	
	private void sobre() {
		new TelaCarregando(60000);
	}
	
	synchronized void tratarErro(String codigo, String mensagem) {
		log("Erro informado pelo servidor");
		log("Código " + codigo);
		log(mensagem);
		desconectar();
	}
	
	synchronized void tratarErro(String erro) {
		log("Erro");
		log(erro);
		desconectar();
	}
	
	synchronized void tratarErro(Mensagem mensagem) {
		tratarErro(mensagem.getCodigo(), mensagem.getMensagem());
	}
	
	public InterfaceServidor() {
		super("ShallowBlue");
		
		new TelaCarregando(2000);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evento) {
				fechar();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout(5, 5));
		
		JPanel painelNorte = new JPanel(new FlowLayout());
		
		JButton botaoConectar = new JButton("Conectar");
		botaoConectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				conectar();
			}
		});
		painelNorte.add(botaoConectar);
		
		JButton botaoDesconectar = new JButton("Desconectar");
		botaoDesconectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				desconectar();
			}
		});
		painelNorte.add(botaoDesconectar);
		
		JButton botaoSobre = new JButton("Sobre");
		botaoSobre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sobre();
			}
		});
		painelNorte.add(botaoSobre);
		
		JButton botaoFechar = new JButton("Fechar");
		botaoFechar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});
		painelNorte.add(botaoFechar);
		
		getContentPane().add(painelNorte, BorderLayout.NORTH);
		
		JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		telaTabuleiro = new TelaTabuleiro();
		
		painelSul.add(telaTabuleiro);
		
		textAreaLog = new JTextArea(24, 30);
		textAreaLog.setEditable(false);
		
		JScrollPane barrasDeRolagem = new JScrollPane(textAreaLog);
		painelSul.add(barrasDeRolagem);
		
		getContentPane().add(painelSul, BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		log("ShallowBlue iniciado!");
	}

}