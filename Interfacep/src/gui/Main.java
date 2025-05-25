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
import javafx.scene.Node;
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

        // Botão de atualizar (NOVO)
        Button btnAtualizar = new Button("🔄 Atualizar lista");
        btnAtualizar.getStyleClass().add("button");
        btnAtualizar.setMaxWidth(Double.MAX_VALUE);

        TextField filtroField = new TextField();
        filtroField.setPromptText("Filtrar por nome...");
        filtroField.setMaxWidth(Double.MAX_VALUE);

        VBox listaArquivosBox = new VBox(5);

        // Método para carregar os botões dinamicamente (NOVO)
        Runnable carregarBotoes = () -> {
            listaArquivosBox.getChildren().clear();
            try {
                HistoricoDAO dao = new HistoricoDAO();
                List<String> titulos = dao.listarTitulosSalvos();
                for (String nome : titulos) {
                    Button btn = new Button(nome);
                    btn.setMaxWidth(Double.MAX_VALUE);
                    btn.getStyleClass().add("botao-arquivo");
                    btn.setOnAction(event -> BuscandoHistorico(nome));
                    listaArquivosBox.getChildren().add(btn);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar títulos: " + e.getMessage());
            }
        };

        // Carrega os botões pela primeira vez
        carregarBotoes.run();

        // Atualiza a lista quando o botão é clicado (NOVO)
        btnAtualizar.setOnAction(e -> {
            carregarBotoes.run();
            statusLabel.setText("🔄 Lista de respostas atualizada!");
        });

        // Filtro (mantido)
        filtroField.textProperty().addListener((obs, oldVal, newVal) -> {
            listaArquivosBox.getChildren().clear();
            if (newVal.isEmpty()) {
                carregarBotoes.run();
            } else {
                for (Node node : listaArquivosBox.getChildren()) {
                    if (node instanceof Button) {
                        Button btn = (Button) node;
                        if (btn.getText().toLowerCase().contains(newVal.toLowerCase())) {
                            listaArquivosBox.getChildren().add(btn);
                        }
                    }
                }
            }
        });

        sidebar.getChildren().addAll(titulo, btnAtualizar, filtroField, listaArquivosBox);
        return sidebar;
    }


 
    private void atualizarListaArquivos(VBox listaContainer) {
        listaContainer.getChildren().clear();
        List<Button> botoesArquivos = new ArrayList<>();
        
        try {
            HistoricoDAO dao = new HistoricoDAO();
            List<String> titulos = dao.listarTitulosSalvos();
            
            for (String nome : titulos) {
                Button btn = new Button(nome);
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.getStyleClass().add("botao-arquivo");
                btn.setOnAction(event -> BuscandoHistorico(nome));
                botoesArquivos.add(btn);
            }
            
            listaContainer.getChildren().addAll(botoesArquivos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar lista: " + e.getMessage());
            
            // Adiciona uma mensagem de erro na interface
            Label erroLabel = new Label("Erro ao carregar dados");
            erroLabel.getStyleClass().add("erro-label");
            listaContainer.getChildren().add(erroLabel);
        }
    }


    
    private void showRespostaDialog(String titulo, String pergunta, String resposta) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(titulo);

        TextArea areaPergunta = new TextArea(pergunta);
        areaPergunta.setEditable(false);
        areaPergunta.setWrapText(true);
        
        TextArea areaResposta = new TextArea(resposta);
        areaResposta.setEditable(false);
        areaResposta.setWrapText(true);
        
        VBox layout = new VBox(10, 
            new Label("Pergunta:"), areaPergunta,
            new Label("Resposta:"), areaResposta,
            new Button("Fechar") {{
                setOnAction(e -> dialog.close());
            }}
        );
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }
 

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    
    private void BuscandoHistorico(String titulo) {
        HistoricoDAO dao = new HistoricoDAO();
        String pergunta = dao.buscarPerguntaPorTitulo(titulo);
        String resposta = dao.buscarRespostaPorTitulo(titulo);

        if (pergunta == null) {
            pergunta = "❌ Pergunta não encontrada.";
        }
        if (resposta == null) {
            resposta = "❌ Resposta não encontrada.";
        }
        Stage janela = new Stage();
        janela.initModality(Modality.APPLICATION_MODAL);
        janela.setTitle("📄 Histórico: " + titulo);

        Label labelPergunta = new Label("📋 Pergunta:");
        TextArea textAreaPergunta = new TextArea(pergunta);
        textAreaPergunta.setEditable(false);
        textAreaPergunta.setWrapText(true);

        Label labelResposta = new Label("🤖 Resposta da IA:");
        TextArea textAreaResposta = new TextArea(resposta);
        textAreaResposta.setEditable(false);
        textAreaResposta.setWrapText(true);

        VBox layout = new VBox(10, labelPergunta, textAreaPergunta, labelResposta, textAreaResposta);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        janela.setScene(scene);
        janela.showAndWait();
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
                    bancoDeDadosSidebar = criarSidebar(); // Atualiza sidebar após exclusão (NOVO)
                    if (sidebarVisivel) layout2.setLeft(bancoDeDadosSidebar);
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
                    bancoDeDadosSidebar = criarSidebar(); // Atualiza sidebar após salvar (NOVO)
                    if (sidebarVisivel) layout2.setLeft(bancoDeDadosSidebar);
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