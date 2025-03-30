package application;

public class Texto {
    private String[] textoCompleto;
    static String linhaUnica = "";

    // Cria uma Array e uma única linha com o código completo
    public void setTexto(String texto) {
        textoCompleto = texto.split("\n");  // Divide o código por linha

        // Limpa a variável linhaUnica antes de criar a nova linha única
        linhaUnica = ""; 

        for (String linha : textoCompleto) {
            linha = linha.trim();  // Remove espaços extras no início e fim da linha
            if (!linha.isEmpty()) {  // Ignora linhas vazias
                linhaUnica += linha + " ";  // Adiciona a linha ao final da linhaUnica com um espaço
            }
        }
    }

    // Retorna o código formatado em uma única linha
    public String getTexto() {
        return linhaUnica.trim();  // Retorna a linha única sem o espaço final extra
    }
}
