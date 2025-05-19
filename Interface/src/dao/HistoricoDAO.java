package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;




import factory.ConnectionFactory;

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
    
    //
    // //
    //
    public List<String> listarNomesRespostas() {
        List<String> nomes = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT titulo FROM historico";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                nomes.add(rs.getString("titulo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomes;
    }

    public String buscarConteudoPorNome(String nome) {
        String conteudo = null;
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT texto FROM historico WHERE titulo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                conteudo = rs.getString("texto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conteudo;
    }

    //
    // //
    //
    
    
    public void excluirHistoricoPorTitulo(String titulo) {
        String sql = "DELETE FROM historico WHERE titulo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Histórico com título '" + titulo + "' excluído com sucesso.");
            } else {
                System.out.println("Nenhum histórico encontrado com esse título.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao excluir histórico: " + e.getMessage());
        }
    }
    
    
}