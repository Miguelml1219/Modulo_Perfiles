package Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearFichasGUI {
    private JPanel pnlCrearFichas;
    private JComboBox estado;
    private JButton confirmarButton;
    private JButton cancelar;
    private JComboBox idsede;
    private JTextField codigo;
    private JTextField fechainicio;
    private JTextField fechafinlectiva;
    private JTextField fechafin;
    private JComboBox idprograma;
    private JComboBox modalidad;
    private JComboBox jornada;
    private JComboBox nivelformacion;
    private java.util.List<String> listaIDSede = new java.util.ArrayList<>();
    private java.util.List<String> listaIDPrograma = new java.util.ArrayList<>();

    public JPanel getPanel(){return pnlCrearFichas;}


    public CrearFichasGUI() {

        cargarSedes();
        cargarPrograma();

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                    String inicioTexto = fechainicio.getText().replace("/", "-");
                    String finLectivaTexto = fechafinlectiva.getText().replace("/", "-");
                    String finTexto = fechafin.getText().replace("/", "-");

                    Date fechaInicio = formatoFecha.parse(inicioTexto);
                    Date fechaFinLectiva = formatoFecha.parse(finLectivaTexto);
                    Date fechaFinal = formatoFecha.parse(finTexto);

                    Fichas_setget fichas = new Fichas_setget(
                            listaIDPrograma.get(idprograma.getSelectedIndex()),
                            listaIDSede.get(idsede.getSelectedIndex()),
                            codigo.getText(),
                            modalidad.getSelectedItem().toString(),
                            jornada.getSelectedItem().toString(),
                            nivelformacion.getSelectedItem().toString(),
                            fechaInicio,
                            fechaFinLectiva,
                            fechaFinal,
                            estado.getSelectedItem().toString()
                    );

                    String url = "jdbc:mysql://localhost:3306/saep";
                    String user = "root";
                    String password = "";

                    try (Connection conn = DriverManager.getConnection(url, user, password)) {
                        FichasDAO dao = new FichasDAO(conn);
                        boolean exito = dao.insertarFicha(fichas);

                        if (exito) {
                            JOptionPane.showMessageDialog(null, "Ficha creada correctamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al guardar la ficha en la base de datos.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error de conexión: " + ex.getMessage());
                        ex.printStackTrace();
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al crear ficha: " + ex.getMessage());
                }
            }
        });


        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Opcional: limpiar campos o cerrar ventana
            }
        });
    }
    private void cargarSedes() {
        String url = "jdbc:mysql://localhost:3306/saep";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ID_sede, nombre_sede FROM sede")) {

            DefaultComboBoxModel<String> modeloSede = new DefaultComboBoxModel<>();
            listaIDSede.clear();

            while (rs.next()) {
                listaIDSede.add(rs.getString("ID_sede"));
                modeloSede.addElement(rs.getString("nombre_sede"));
            }

            idsede.setModel(modeloSede);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCrearFichas, "Error al cargar sedes");
            ex.printStackTrace();
        }
    }

    private void cargarPrograma() {
        String url = "jdbc:mysql://localhost:3306/saep";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ID_programas, nombre_programa FROM programas")) {

            DefaultComboBoxModel<String> modeloPrograma = new DefaultComboBoxModel<>();
            listaIDPrograma.clear();

            while (rs.next()) {
                listaIDPrograma.add(rs.getString("ID_programas"));
                modeloPrograma.addElement(rs.getString("nombre_programa"));
            }

            idprograma.setModel(modeloPrograma);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCrearFichas, "Error al cargar programas");
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Creación de Fichas");
        frame.setContentPane(new CrearFichasGUI().pnlCrearFichas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrar ventana
        frame.setVisible(true);
    }
}
