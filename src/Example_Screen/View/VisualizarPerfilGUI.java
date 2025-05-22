package Example_Screen.View;

import Example_Screen.Connection.DBConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class VisualizarPerfilGUI {
    public JPanel panel1;
    private JLabel estado;
    private JLabel conta;
    private JLabel direc;
    private JLabel email;
    private JLabel num_doc;
    private JLabel nombre;
    private JLabel rol;
    private JLabel tipo_doc;
    private JLabel apellido;
    private JLabel email_insti;
    private int userID; // Para almacenar el ID del usuario actual


    public VisualizarPerfilGUI() {


        cargarDatosUsuario();

    }

    public void cargarDatosUsuario()
    {
        String usuarioEmail = obtenerUsuarioActual();
        if (usuarioEmail == null || usuarioEmail.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se pudo obtener la información del usuario actual.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            //String sql = "SELECT * FROM usuarios WHERE email = ?";
            // Cambiar esta consulta:
            String sql = "SELECT u.*, r.rol FROM usuarios u " +
                    "INNER JOIN rol r ON u.ID_rol = r.ID_rol " +
                    "WHERE u.email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuarioEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Almacenar ID para futuras actualizaciones
                userID = rs.getInt("ID_usuarios");

                // Cargar datos a los campos
                nombre.setText(rs.getString("nombres"));
                apellido.setText(rs.getString("apellidos"));
                num_doc.setText(rs.getString("numero"));
                email.setText(rs.getString("email"));
                email_insti.setText(rs.getString("email_insti"));
                direc.setText(rs.getString("direccion"));
                conta.setText(rs.getString("contacto1"));
                tipo_doc.setText(rs.getString("tipo_dc"));
                rol.setText(rs.getString("rol"));
                estado.setText(rs.getString("estado"));


            } else {
                JOptionPane.showMessageDialog(null, "No se encontró información del usuario en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del usuario: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Agregar este método a la clase VisualizarPerfilGUI
    public void cargarDatosUsuarioEspecifico(String numeroDoc, String tipoDoc) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT u.*, r.rol FROM usuarios u " +
                    "INNER JOIN rol r ON u.ID_rol = r.ID_rol " +
                    "WHERE u.numero = ? AND u.tipo_dc = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, numeroDoc);
            stmt.setString(2, tipoDoc);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Almacenar ID para futuras actualizaciones
                userID = rs.getInt("ID_usuarios");

                // Cargar datos a los campos
                nombre.setText(rs.getString("nombres"));
                apellido.setText(rs.getString("apellidos"));
                num_doc.setText(rs.getString("numero"));
                email.setText(rs.getString("email"));
                email_insti.setText(rs.getString("email_insti"));
                direc.setText(rs.getString("direccion"));
                conta.setText(rs.getString("contacto1"));
                tipo_doc.setText(rs.getString("tipo_dc"));
                rol.setText(rs.getString("rol"));
                estado.setText(rs.getString("estado"));

            } else {
                JOptionPane.showMessageDialog(null,
                        "No se encontró información del usuario con documento: " + numeroDoc,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar los datos del usuario: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String obtenerUsuarioActual() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
            return props.getProperty("usuario", "");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
