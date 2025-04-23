package application;

import application.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class HistoricoDAO {

    public void salvarHistorico(String titulo, String pergunta, String respostaIA) {
        String sql = "INSERT INTO historico (titulo, pergunta, respostaIA, dataPergunta) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.setString(2, pergunta);
            stmt.setString(3, respostaIA);
            stmt.setDate(4, Date.valueOf(LocalDate.now()));

            stmt.executeUpdate();
            System.out.println("Histórico salvo com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }
}