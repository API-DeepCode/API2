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

    public String buscarConteudoPorNome(String nome) {
        String conteudo = null;
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT respostaIA FROM historico WHERE titulo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                conteudo = rs.getString("respostaIA");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conteudo;
    }

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
    
    public List<String> listarTitulosSalvos() {
        List<String> titulos = new ArrayList<>();
        String sql = "SELECT titulo FROM historico ORDER BY id DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                if (titulo != null && !titulo.trim().isEmpty()) {
                    titulos.add(titulo);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar títulos do histórico: " + e.getMessage());
        }
        return titulos;
    }
    public String buscarPerguntaPorTitulo(String titulo) {
        String pergunta = null;
        String sql = "SELECT pergunta FROM historico WHERE titulo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                pergunta = rs.getString("pergunta");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar pergunta: " + e.getMessage());
        }
        return pergunta;
    }
    
    public String buscarRespostaPorTitulo(String titulo) {
        String resposta = null;
        String sql = "SELECT respostaIA FROM historico WHERE titulo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resposta = rs.getString("respostaIA");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar resposta: " + e.getMessage());
        }
        return resposta;
    }
    
    public String[] buscarPerguntaRespostaPorNome(String nome) {
        String[] conteudo = new String[2];
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT pergunta, respostaIA FROM historico WHERE titulo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                conteudo[0] = rs.getString("pergunta");
                conteudo[1] = rs.getString("respostaIA");
                return conteudo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
  

    
}
