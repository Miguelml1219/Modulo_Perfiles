package Example_Screen.Connection;

import  Example_Screen.Model.Aprendiz;

import java.sql.*;
import java.time.LocalDate;

public class AprendizDAO {
    private Connection connection;

    public AprendizDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/progreso", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Aprendiz obtenerAprendiz() {
        String query = "SELECT * FROM progreso_aprendiz LIMIT 1";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                LocalDate inicio = rs.getDate("fecha_inicio").toLocalDate();
                LocalDate fin = rs.getDate("fecha_fin").toLocalDate();
                return new Aprendiz(nombre, inicio, fin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
