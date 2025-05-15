package gui;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Texto;
import dao.HistoricoDAO;
import ia.IA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    private VBox bancoDeDadosSidebar;
    private boolean sidebarVisivel = false;
    private double larguraPadrao = 850;
    private double alturaPadrao = 700;
    private String dataFormatada = "";

    private Label statusLabel = new Label("🔍 Status: Aguardando código...");

    private void toggleSidebar(BorderPane layout) {
        TranslateTransition trans = new TranslateTransition(Duration.millis(300), bancoDeDadosSidebar);
        if (sidebarVisivel) {
            trans.setFromX(0);
            trans.setToX(-250);
            trans.setOnFinished(e -> layout.setLeft(null));
        } else {
            layout.setLeft(bancoDeDadosSidebar);
            trans.setFromX(-250);
            trans.setToX(0);
        }
        trans.play();
        sidebarVisivel = !sidebarVisivel;
    }

    private VBox criarSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.getStyleClass().add("sidebar");

        Label titulo = new Label("Respostas salvas:");
        titulo.getStyleClass().add("label");

        TextField filtroField = new TextField();
        filtroField.setPromptText("Filtrar por nome...");
        filtroField.setMaxWidth(Double.MAX_VALUE);

        VBox listaArquivosBox = new VBox(5);

        List<String> nomesArquivos = new ArrayList<>(Arrays.asList(
            "resposta_1.txt", "resposta_2.txt", "resposta_3.txt", "resposta_4.txt", "teste"
        ));

        List<Label> labelsArquivos = new ArrayList<>();
        for (String nome : nomesArquivos) {
            Label label = new Label(nome);
            label.getStyleClass().add("label");
            labelsArquivos.add(label);
        }

        listaArquivosBox.getChildren().addAll(labelsArquivos);

        filtroField.textProperty().addListener((obs, oldVal, newVal) -> {
            listaArquivosBox.getChildren().clear();
            if (newVal == null || newVal.isEmpty()) {
                listaArquivosBox.getChildren().addAll(labelsArquivos);
            } else {
                for (Label label : labelsArquivos) {
                    if (label.getText().toLowerCase().contains(newVal.toLowerCase())) {
                        listaArquivosBox.getChildren().add(label);
                    }
                }
            }
        });

        sidebar.getChildren().addAll(titulo, filtroField, listaArquivosBox);
        return sidebar;
    }

    @Override
    public void start(Stage primaryStage) {
        bancoDeDadosSidebar = criarSidebar();

        // Título fixo
        Label tituloTopo = new Label("💡 DeepCode – Melhor grupo da Fatec");
        tituloTopo.getStyleClass().add("titulo-topo");
        HBox headerBox = new HBox(tituloTopo);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10));

        // Tela 1
        TextArea codeArea = new TextArea();
        codeArea.setPromptText("Digite seu código aqui...");
        VBox.setVgrow(codeArea, Priority.ALWAYS);

        Button btnSendCode = new Button("🚀 Enviar código");
        Button btnMenu1 = new Button("☰ Menu");
        Button btnClearCode = new Button("🧹 Limpar código");
        btnClearCode.setOnAction(e -> codeArea.clear());

        VBox center1 = new VBox(10, new Label("📝 Escreva seu código para ser corrigido e melhorado:"), codeArea, btnSendCode, btnClearCode);
        center1.setPadding(new Insets(20));

        BorderPane layout1 = new BorderPane();
        layout1.setTop(new VBox(headerBox, new HBox(btnMenu1)));
        layout1.setCenter(center1);
        layout1.setBottom(statusLabel);

        Scene scene1 = new Scene(layout1, larguraPadrao, alturaPadrao);
        scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // Tela 2
        TextArea answerArea = new TextArea();
        answerArea.setEditable(false);
        answerArea.setWrapText(true);
        VBox.setVgrow(answerArea, Priority.ALWAYS);

        Label dataHoraLabel = new Label();
        Button btnNovaResposta = new Button("🔄 Nova resposta");
        Button btnSalvar = new Button("💾 Salvar resposta");
        Button btnVoltar = new Button("🔙 Voltar");
        Button btnMenu2 = new Button("☰ Menu");

        btnNovaResposta.setOnAction(e -> {
            String codigo = codeArea.getText().trim();
            Texto texto = new Texto();
            texto.setTexto(codigo);
            String novaResposta = IA.respostaIA(texto.getTexto());
            answerArea.setText(novaResposta);
            dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            dataHoraLabel.setText("📅 Data: " + dataFormatada);
            statusLabel.setText("✅ Resposta atualizada com sucesso!");
        });


        VBox center2 = new VBox(10, new Label("🤖 Resposta da IA:"), dataHoraLabel, answerArea, btnNovaResposta, btnSalvar, btnVoltar);
        center2.setPadding(new Insets(20));

        BorderPane layout2 = new BorderPane();
        layout2.setTop(new VBox(headerBox, new HBox(btnMenu2)));
        layout2.setCenter(center2);
        layout2.setBottom(statusLabel);

        Scene scene2 = new Scene(layout2, larguraPadrao, alturaPadrao);
        scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // Ações principais
        btnSendCode.setOnAction(e -> {
            String codigo = codeArea.getText().trim();
            Texto texto = new Texto();
            texto.setTexto(codigo);
            String resposta = IA.respostaIA(texto.getTexto());
            answerArea.setText(resposta);
            dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            dataHoraLabel.setText("📅 Data: " + dataFormatada);
            layout2.setLeft(null);
            sidebarVisivel = false;
            statusLabel.setText("✅ Código enviado para análise!");
            primaryStage.setScene(scene2);
        });

        btnVoltar.setOnAction(e -> {
            layout1.setLeft(null);
            sidebarVisivel = false;
            statusLabel.setText("🔙 Voltou para a tela de código.");
            primaryStage.setScene(scene1);
        });

        btnSalvar.setOnAction(e -> {
            Stage salvarStage = new Stage();
            salvarStage.initModality(Modality.APPLICATION_MODAL);
            salvarStage.setTitle("Salvar resposta da IA");

            Label lblNome = new Label("Nome do arquivo:");
            TextField txtNome = new TextField();
            txtNome.setPromptText("ex: correcao_codigo");

            Button btnConfirmar = new Button("✅ Confirmar");
            Button btnExcluir = new Button("🗑 Excluir");

            btnExcluir.setOnAction(evt -> {
                String titulo = txtNome.getText().trim();
                if (!titulo.isEmpty()) {
                    HistoricoDAO dao = new HistoricoDAO();
                    dao.excluirHistoricoPorTitulo(titulo);
                    statusLabel.setText("🗑️ Resposta excluída do histórico.");
                }
            });

            btnConfirmar.setOnAction(evt -> {
                String titulo = txtNome.getText().trim();
                String pergunta = codeArea.getText();
                String respostaIA = answerArea.getText();
                if (!titulo.isEmpty() && !respostaIA.isEmpty()) {
                    HistoricoDAO historicoDAO = new HistoricoDAO();
                    historicoDAO.salvarHistorico(titulo, pergunta, respostaIA);
                    salvarStage.close();
                    statusLabel.setText("💾 Resposta salva como: " + titulo);
                }
            });

            VBox salvarLayout = new VBox(10, lblNome, txtNome, btnConfirmar, btnExcluir);
            salvarLayout.setPadding(new Insets(20));
            salvarLayout.setAlignment(Pos.CENTER);
            Scene salvarScene = new Scene(salvarLayout, 300, 200);
            salvarScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            salvarStage.setScene(salvarScene);
            salvarStage.showAndWait();
        });

        btnMenu1.setOnAction(e -> toggleSidebar(layout1));
        btnMenu2.setOnAction(e -> toggleSidebar(layout2));

        primaryStage.setTitle("DeepCode");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

