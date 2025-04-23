package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Button;

import application.IA;

public class Main extends Application {

    public void start(Stage TelaPrincipal) {
        // Criando um TextArea para o usuário digitar código
        TextArea codeArea = new TextArea();
        codeArea.setPrefSize(600, 400);

        // Botão para enviar o código para a IA
        Button btnSendCode = new Button("Enviar código");

        TextArea answerArea = new TextArea();
        answerArea.setPrefSize(600, 400);
        answerArea.setEditable(false);
        
        // Criando a página principal
        VBox root = new VBox(codeArea, btnSendCode, answerArea);
        VBox.setMargin(codeArea, new Insets(10, 20, 10, 20));

        // Criando a cena
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // Funcionalidade do botão btnSendCode
        btnSendCode.setOnAction(event -> {
            String codigo = codeArea.getText();

            // Criando o objeto Texto para transformar em linha única
            Texto texto1 = new Texto();
            texto1.setTexto(codigo);  // Converte o código em linha única
            
            IA.respostaIA(texto1.getTexto());
            answerArea.setText(IA.respostaIA(texto1.getTexto()));
        });

        // Configurando o palco (janela)
        TelaPrincipal.setTitle("DeepCode");
        TelaPrincipal.setScene(scene);
        TelaPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
