package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

import javafx.geometry.Pos;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    private Label dataHoraLabel = new Label(); // Label que mostra a data/hora atual
    private String dataFormatada = "";


    public void start(Stage primaryStage) {
        // ======= Tela 1: Envio de Código =======
        TextArea codeArea = new TextArea();
        codeArea.setPrefSize(600, 400);
        codeArea.setPromptText("Digite seu código Java aqui...");

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

        Label tituloResposta = new Label("Resposta da IA:");
        dataHoraLabel.setStyle("-fx-text-fill: #999999; -fx-font-size: 12px;");

        VBox tela2 = new VBox(15,
            tituloResposta,
            dataHoraLabel,

            answerArea,
            salvarButton,
            voltarButton
        );
        tela2.setPadding(new Insets(20));


        Scene scene2 = new Scene(tela2, 800, 600);
        scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // ======= Ações dos botões =======
        btnSendCode.setOnAction(event -> {
            String codigo = codeArea.getText().trim();

            if (codigo.isEmpty()) {
                answerArea.setText("Por favor, digite algum código.");
            } else {
                Texto texto1 = new Texto();
                texto1.setTexto(codigo);

                String resposta = IA.respostaIA(texto1.getTexto());
                answerArea.setText(resposta);

                // Atualiza data/hora
                dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                dataHoraLabel.setText("Data: " + dataFormatada);
            }


            primaryStage.setScene(scene2);
        });

        voltarButton.setOnAction(e -> primaryStage.setScene(scene1));

        salvarButton.setOnAction(e -> {

            Stage salvarStage = new Stage();
            salvarStage.initModality(Modality.APPLICATION_MODAL);
            salvarStage.setTitle("Salvar resposta da IA");

            Label lblNome = new Label("Nome do arquivo:");
            TextField txtNome = new TextField();
            txtNome.setPromptText("ex: correcao_codigo");

            Button btnConfirmar = new Button("Confirmar");

            btnConfirmar.setOnAction(evt -> {
                String nome = txtNome.getText().trim();
                if (!nome.isEmpty()) {
                    try (FileWriter writer = new FileWriter(nome + ".txt")) {
                        writer.write("Data: " + dataFormatada + "\n\n");
                        writer.write("Resposta da IA:\n");
                        writer.write(answerArea.getText());
                        writer.write("\n\n---\nSalvo em: " + dataFormatada);
                        System.out.println("Resposta salva como " + nome + ".txt");
                        salvarStage.close();
                    } catch (IOException ex) {
                        System.err.println("Erro ao salvar: " + ex.getMessage());
                    }
                }
            });


            VBox layoutSalvar = new VBox(10, lblNome, txtNome, btnConfirmar);
            layoutSalvar.setPadding(new Insets(20));
            layoutSalvar.setAlignment(Pos.CENTER);

            Scene salvarScene = new Scene(layoutSalvar, 300, 150);
            salvarScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            salvarStage.setScene(salvarScene);
            salvarStage.showAndWait();

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
