package ia;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONObject;

public class IA {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    public static String consultarOllama(String prompt) {
        try {
            // Criando a conexão com o servidor local do Ollama
            URL url = new URL(OLLAMA_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Criando o JSON com o modelo e o prompt
            JSONObject json = new JSONObject();
            json.put("model", "meu_qwen"); // Nome do modelo que criamos. OBS: o modelo será diferente para cada usuário, segundo o LLM que escolher. Sugerimos que assista ao tutorial disponível no GitHub.
            json.put("prompt", prompt);
            json.put("stream", false);

            // Enviando a requisição
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Lendo a resposta
            Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Extraindo a resposta JSON
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("response");
        }
        catch (Exception e) {
            return "Erro ao conectar ao Ollama: " + e.getMessage();
        }
    }

    public static String respostaIA(String codigo) {
    	String resposta = consultarOllama(codigo);
    	return resposta;
    }
}
