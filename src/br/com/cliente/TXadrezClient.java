package br.com.cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

/**
 * Classe Proxy Webservice - Antes de usar é necessário ler a documentação
 * completa passada no forum, as explicações dos métodos estão resumidas.
 *
 */
public class TXadrezClient {

    private String FIpServidor;
    private int FPorta;

    public TXadrezClient(String pIpServidor, int pPorta) {
        this.setIpServidor(pIpServidor);
        this.setPorta(pPorta);
    }

    public String getIpServidor() {
        return FIpServidor;
    }

    public void setIpServidor(String pIpServidor) {
        FIpServidor = pIpServidor;
    }

    public int getPorta() {
        return FPorta;
    }

    public void setPorta(int pPorta) {
        FPorta = pPorta;
    }

    /**
     * Realiza jogada no tabuleiro
     *
     * @param pJogada - String no formato JSON com os seguintes pares:
     * "id_jogador":"valor_id", "posicao_atual":"{"x":"valor_x",
     * "y":"valor_y"}", "nova_posicao":"{"x":"valor_x", "y":"valor_y"}",
     * "peca_promocao":"D"

     * @return String no formato JSON com a situacao atual da partida, caso a
     * Jogada for válida, senao retorna um JSON com erro (ver tabela de erros na
     * documentação).
     */
    public String Jogar(String pJogada) {
        return WebServiceRequest(URI() + "Jogar/" + pJogada);
    }

    /**
     * Reinicia a partida
     *
     * @return Boolean
     *
     */
    public Boolean ReiniciarJogo() {
        WebServiceRequest(URI() + "ReiniciarJogo/");
        return true;
    }

    /**
     * Função para solicitar a situacao atual da partida.
     *
     * @param pIdJogador - String com o numero Id do jogador
     * @return String no formato JSON com a situacao do Jogo:
     * {"result":["{\"mensagem\":{\"codigo\":\"102\",\"mensagem\":\"Esperando o
     * Jogador 2\"}}"]} ou desse tipo com a situação completa - Mensagem, Vez,
     * Tabuleiro (Turno, Posicoes), Peca Capturadas:
     * {"result":["{\"mensagem\":{\"codigo\":\"106\",\"mensagem\":\"Esperando Sua Jogada!\"},
	 * \"vez\":\"2319\",
	 * \"tabuleiro\":{\"turno\":\"0\",\"posicoes\":[{\"peca\":{\"nome\":\"T\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"1\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"C\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"2\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"B\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"3\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"D\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"4\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"R\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"5\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"B\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"6\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"C\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"7\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"T\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"1\",\"y\":\"8\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"1\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"2\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"3\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"4\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"5\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"6\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"7\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"branca\",\"posicao_atual\":{\"x\":\"2\",\"y\":\"8\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"1\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"2\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"3\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"4\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"5\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"6\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"7\"}},{\"ponto\":{\"x\":\"3\",\"y\":\"8\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"1\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"2\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"3\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"4\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"5\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"6\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"7\"}},{\"ponto\":{\"x\":\"4\",\"y\":\"8\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"1\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"2\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"3\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"4\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"5\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"6\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"7\"}},{\"ponto\":{\"x\":\"5\",\"y\":\"8\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"1\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"2\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"3\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"4\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"5\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"6\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"7\"}},{\"ponto\":{\"x\":\"6\",\"y\":\"8\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"1\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"2\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"3\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"4\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"5\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"6\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"7\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"P\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"7\",\"y\":\"8\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"T\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"1\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"C\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"2\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"B\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"3\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"D\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"4\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"R\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"5\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"B\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"6\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"C\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"7\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}},{\"peca\":{\"nome\":\"T\",\"cor\":\"preta\",\"posicao_atual\":{\"x\":\"8\",\"y\":\"8\"},\"capturada\":\"false\",\"qtd_movimentos\":\"0\"}}],\"pecas_capturadas\":[]}}"]}.
     *
     */
    public String SituacaoAtual(String pIdJogador) {
        return WebServiceRequest(URI() + "SituacaoAtual/" + pIdJogador);
    }

    /**
     * Funcao para solicitar um numero id para o jogador.
     * Caso seja solicitado um id para o Jogador e a sala ja estiver cheia Entao
     * será retornada um JSON semelhante a este:
     * {"error":"{\"codigo\":\"308\",\"mensagem\":\"Sala cheia! Não Há slots
     * disponiveis"}"}.
     *
     * @param pNomeJogador - String com o nome do jogador. Ex.: Pedro
     * @return String no formato JSON do com o numero Id do Jogador na partida
     * (Jogador 1 ou 2)
     * Ex.: {"result":["{\"id_jogador\":\"0000\",\"numero_jogador\":\"1\"}"]}.
     *
     */
    public String SolicitarIdJogador(String pNomeJogador, boolean usarLetras) {
        return WebServiceRequest(URI() + "SolicitarIdJogador/" + pNomeJogador + (usarLetras ? "/True" : ""));
    }

    /**
     * Monta a URI de chamada
     * 
     * @return String
     */
    private String URI() {
        return "http://" + getIpServidor() + ":" + getPorta() + "/datasnap/rest/TXadrez/";
    }

    /**
     * Conecta com o servidor
     *
     * @param URI - String com a URI de chamada
     * @return String - Contem a resposta do servidor
     */
    private String WebServiceRequest(String URI) {
        try {
            URL uri = new URL(URI);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.setRequestProperty("Request-Method", "GET");
            BufferedReader br;
            String retorno = "";
            String output;

            if (connection.getResponseCode() != 200) {
                br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
                while ((output = br.readLine()) != null) {
                    retorno = retorno + output;
                }
                throw new RuntimeException(retorno);
            } else {
                br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            }

            output = "";
            retorno = "";
            while ((output = br.readLine()) != null) {
                retorno = retorno + output;
            }

            connection.disconnect();

            JSONObject lJSON = new JSONObject(retorno);

            if (lJSON.get("result") != null) {
                return lJSON.toString();
            } else {
                return "";
            }
        } catch (java.net.ConnectException e) {
            e.getMessage();
        } catch (java.lang.RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (MalformedURLException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }
}
