package Example_Screen.Model;

import Example_Screen.Connection.DBConnection;
import java.sql.*;
import java.time.LocalDate;

public class AprendizDAO {
    public Aprendiz obtenerAprendiz(int idUsuario) {
        String query = "SELECT f.fecha_fin_lec, f.fecha_final, u.nombres " +
                "FROM fichas f " +
                "JOIN aprendices a ON f.ID_Fichas = a.ID_Fichas " +
                "JOIN usuarios u ON a.ID_usuarios = u.ID_usuarios " +
                "WHERE u.ID_usuarios = " + idUsuario;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                LocalDate inicio = rs.getDate("fecha_fin_lec").toLocalDate();
                LocalDate fin = rs.getDate("fecha_final").toLocalDate();
                String nombre = rs.getString("nombres");
                return new Aprendiz(nombre, inicio, fin);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos del aprendiz: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}