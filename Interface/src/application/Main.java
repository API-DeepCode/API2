package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Main extends Application {

    public void start(Stage primaryStage) {
        // ======= Tela 1: Envio de Código =======
        TextArea codeArea = new TextArea();
        codeArea.setPrefSize(600, 400);

        Button btnSendCode = new Button("Enviar código");

        VBox tela1 = new VBox(15, new Label("Digite seu código abaixo:"), codeArea, btnSendCode);
        tela1.setPadding(new Insets(20));

        Scene scene1 = new Scene(tela1, 800, 600);
        scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // ======= Tela 2: Resposta da IA =======
        TextArea answerArea = new TextArea();
        answerArea.setPrefSize(600, 400);
        answerArea.setEditable(false);

        Button salvarButton = new Button("Salvar resposta");
        Button voltarButton = new Button("Voltar");

        VBox tela2 = new VBox(15,
            new Label("Resposta da IA:"),
            answerArea,
            salvarButton,
            voltarButton
        );
        tela2.setPadding(new Insets(20));

        Scene scene2 = new Scene(tela2, 800, 600);
        scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // ======= Ações dos botões =======
        btnSendCode.setOnAction(event -> {
            String codigo = codeArea.getText();

            Texto texto1 = new Texto();
            texto1.setTexto(codigo);

            String resposta = IA.respostaIA(texto1.getTexto());
            answerArea.setText(resposta);

            primaryStage.setScene(scene2);
        });

        voltarButton.setOnAction(e -> primaryStage.setScene(scene1));

        salvarButton.setOnAction(e -> {
            // Futuramente: abrir tela com nome e data para salvar no banco
            System.out.println("Botão de salvar clicado!");
        });

        // ======= Configuração da Janela =======
        primaryStage.setTitle("DeepCode");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
