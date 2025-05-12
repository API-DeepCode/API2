package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Main extends Application {
   private VBox bancoDeDadosSidebar;
   private boolean sidebarVisivel = false;
   private double larguraPadrao = 800;
   private double alturaPadrao = 600;
   private String dataFormatada = "";
   private void toggleSidebar(BorderPane layout) {
       if (sidebarVisivel) {
           layout.setLeft(null);
           layout.setPrefWidth(larguraPadrao);
       } else {
           layout.setLeft(bancoDeDadosSidebar);
           layout.setPrefWidth(larguraPadrao);
       }
       sidebarVisivel = !sidebarVisivel;
   }
   private VBox criarSidebar() {
       VBox sidebar = new VBox(10);
       sidebar.setPadding(new Insets(15));
       sidebar.setStyle("-fx-background-color: #2d2d2d; -fx-pref-width: 250px;");
       sidebar.getChildren().add(new Label("Respostas salvas (simulado):"));
       // Simula arquivos salvos
       for (int i = 1; i <= 5; i++) {
           sidebar.getChildren().add(new Label("resposta_" + i + ".txt"));
       }
       return sidebar;
   }
   @Override
   public void start(Stage primaryStage) {
       bancoDeDadosSidebar = criarSidebar();
       // ========== Tela 1: Enviar Código ==========
       TextArea codeArea = new TextArea();
       codeArea.setPromptText("Digite seu código Java aqui...");
       VBox.setVgrow(codeArea, Priority.ALWAYS);
       codeArea.setMaxHeight(Double.MAX_VALUE);
       codeArea.setMaxWidth(Double.MAX_VALUE);
       Button btnSendCode = new Button("Enviar código");
       Button btnMenu1 = new Button("☰");
       btnMenu1.setStyle("-fx-font-size: 20px; -fx-background-color: transparent; -fx-text-fill: white;");
       HBox topo1 = new HBox(btnMenu1);
       topo1.setAlignment(Pos.TOP_LEFT);
       VBox center1 = new VBox(10, new Label("Código Java:"), codeArea, btnSendCode);
       center1.setPadding(new Insets(20));
       BorderPane layout1 = new BorderPane();
       layout1.setTop(topo1);
       layout1.setCenter(center1);
       Scene scene1 = new Scene(layout1, larguraPadrao, alturaPadrao);
       scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
       // ========== Tela 2: Resposta da IA ==========
       TextArea answerArea = new TextArea();
       answerArea.setEditable(false);
       answerArea.setEditable(false);
       answerArea.setWrapText(true);
       VBox.setVgrow(answerArea, Priority.ALWAYS);
       answerArea.setMaxHeight(Double.MAX_VALUE);
       answerArea.setMaxWidth(Double.MAX_VALUE);
       Button btnSalvar = new Button("Salvar resposta");
       Button btnVoltar = new Button("Voltar");
       Button btnMenu2 = new Button("☰");
       btnMenu2.setStyle("-fx-font-size: 20px; -fx-background-color: transparent; -fx-text-fill: white;");
       Label dataHoraLabel = new Label();
       
       Button btnNovaResposta = new Button("Gerar nova resposta"); //Criação do botão para enviar uma nova resposta.

       btnNovaResposta.setOnAction(e -> {
           String codigo = codeArea.getText().trim(); // pega o código novamente
           Texto texto = new Texto();
           texto.setTexto(codigo);
           String novaResposta = IA.respostaIA(texto.getTexto()); // chama a IA de novo

           answerArea.setText(novaResposta); // mostra nova resposta
           dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
           dataHoraLabel.setText("Data: " + dataFormatada);
       });

       
       dataHoraLabel.setStyle("-fx-text-fill: #999999; -fx-font-size: 12px;");
       VBox center2 = new VBox(10, new Label("Resposta da IA:"), dataHoraLabel, answerArea, btnNovaResposta, btnSalvar, btnVoltar);
       center2.setPadding(new Insets(20));
       BorderPane layout2 = new BorderPane();
       layout2.setTop(new HBox(btnMenu2));
       layout2.setCenter(center2);
       Scene scene2 = new Scene(layout2, larguraPadrao, alturaPadrao);
       scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
       // ========== Ações ==========
       btnSendCode.setOnAction(e -> {
           String codigo = codeArea.getText().trim();
           Texto texto = new Texto();
           texto.setTexto(codigo);
           String resposta = IA.respostaIA(texto.getTexto());
           answerArea.setText(resposta);
           dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
           dataHoraLabel.setText("Data: " + dataFormatada);
           layout2.setLeft(null);
           sidebarVisivel = false;
           primaryStage.setScene(scene2);
       });
       btnVoltar.setOnAction(e -> {
           layout1.setLeft(null);
           sidebarVisivel = false;
           primaryStage.setScene(scene1);
       });
      btnSalvar.setOnAction(e -> {

           Stage salvarStage = new Stage();
           salvarStage.initModality(Modality.APPLICATION_MODAL);
           salvarStage.setTitle("Salvar resposta da IA");

           Label lblNome = new Label("Nome do arquivo:");
           TextField txtNome = new TextField();
           txtNome.setPromptText("ex: correcao_codigo");

           Button btnConfirmar = new Button("Confirmar");
           
           Button btnExcluir = new Button("Excluir resposta");

           btnExcluir.setOnAction(evt -> {
               String titulo = txtNome.getText().trim();

               if (!titulo.isEmpty()) {
                   HistoricoDAO dao = new HistoricoDAO();
                   dao.excluirHistoricoPorTitulo(titulo);
               } else {
                   System.out.println("Digite o título da resposta a ser excluída.");
               }
           });


           btnConfirmar.setOnAction(evt -> {
               String titulo = txtNome.getText().trim();
               String pergunta = codeArea.getText(); 
               String respostaIA = answerArea.getText();

               if (!titulo.isEmpty() && !respostaIA.isEmpty()) {

                   HistoricoDAO historicoDAO = new HistoricoDAO();
                   historicoDAO.salvarHistorico(titulo, pergunta, respostaIA); 

                   System.out.println("Resposta salva no banco de dados com o título: " + titulo);

                   
                   salvarStage.close();
               } else {
                   System.out.println("Título ou resposta não podem estar vazios.");
               }
           });
           
           
           VBox salvarLayout = new VBox(10, lblNome, txtNome, btnConfirmar, btnExcluir);
           salvarLayout.setPadding(new Insets(20));
           salvarLayout.setAlignment(Pos.CENTER);
           Scene salvarScene = new Scene(salvarLayout, 300, 150);
           salvarScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
           salvarStage.setScene(salvarScene);
           salvarStage.showAndWait();
       });
      
       btnMenu1.setOnAction(e -> toggleSidebar(layout1));
       btnMenu2.setOnAction(e -> toggleSidebar(layout2));
       // ========== Inicia a Aplicação ==========
       primaryStage.setTitle("DeepCode");
       primaryStage.setScene(scene1);
       primaryStage.show();
   }
   public static void main(String[] args) {
       launch(args);
   }
}
