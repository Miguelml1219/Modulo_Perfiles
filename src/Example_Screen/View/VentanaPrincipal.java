package Example_Screen.View;


import Example_Screen.Connection.AprendizDAO;
import Example_Screen.Model.Aprendiz;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    public VentanaPrincipal() {
        setTitle("Progreso del Aprendiz");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        AprendizDAO dao = new AprendizDAO();
        Aprendiz aprendiz = dao.obtenerAprendiz();
        int progreso = aprendiz != null ? aprendiz.calcularProgreso() : 0;

        JLabel bienvenida = new JLabel("Bienvenid@ " + (aprendiz != null ? aprendiz.getNombre() : ""), SwingConstants.CENTER);
        bienvenida.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel fechas = new JLabel(
                "<html>Fecha Inicio: " + aprendiz.getFechaInicio() + "<br/>Fecha Final: " + aprendiz.getFechaFin() + "</html>",
                SwingConstants.CENTER);

        GraficoCircular grafico = new GraficoCircular(progreso);

        setLayout(new BorderLayout());
        add(bienvenida, BorderLayout.NORTH);
        add(grafico, BorderLayout.CENTER);
        add(fechas, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
