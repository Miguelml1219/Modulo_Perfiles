package Usuarios;

import Example_Screen.Connection.DBConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class UsuariosDAO {
    private static DBConnection dbConnection = new DBConnection();

    // Agregar usuario (asume que el ID es autoincremental en la base de datos)
    public boolean agregarUsuario(Usuarios_getset usuario) {
        String query = "INSERT INTO usuarios (ID_rol, tipo_dc, numero, nombres, apellidos, email, email_insti, direccion, contacto1, contacto2, clave, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, usuario.getID_rol());
            pst.setString(2, usuario.getTipo_dc());
            pst.setString(3, usuario.getDocumento());
            pst.setString(4, usuario.getNombres());
            pst.setString(5, usuario.getApellidos());
            pst.setString(6, usuario.getEmail());
            pst.setString(7, usuario.getEmail_insti());
            pst.setString(8, usuario.getDireccion());
            pst.setString(9, usuario.getContacto1());
            pst.setString(10, usuario.getContacto2());
            pst.setString(11, usuario.getClave());
            pst.setString(12, usuario.getEstado());

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar usuario por ID
    public boolean actualizarUsuario(Usuarios_getset usuario) {
        String query = "UPDATE usuarios SET ID_rol = ?, tipo_dc = ?, numero = ?, nombres = ?, apellidos = ?, email = ?, email_insti = ?, direccion = ?, contacto1 = ?, contacto2 = ?, clave = ?, estado = ? WHERE ID_usuarios = ?";
        try (Connection con = dbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, usuario.getID_rol());
            pst.setString(2, usuario.getTipo_dc());
            pst.setString(3, usuario.getDocumento());
            pst.setString(4, usuario.getNombres());
            pst.setString(5, usuario.getApellidos());
            pst.setString(6, usuario.getEmail());
            pst.setString(7, usuario.getEmail_insti());
            pst.setString(8, usuario.getDireccion());
            pst.setString(9, usuario.getContacto1());
            pst.setString(10, usuario.getContacto2());
            pst.setString(11, usuario.getClave());
            pst.setString(12, usuario.getEstado());
            pst.setInt(13, usuario.getID_usuarios());

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar usuario por ID
    public boolean eliminarUsuario(int id_usuario) {
        String query = "DELETE FROM usuarios WHERE ID_usuarios = ?";
        try (Connection con = dbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id_usuario);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Buscar un usuario por ID
    public Usuarios_getset buscarUsuario(int id_usuario) {
        String query = "SELECT * FROM usuarios WHERE ID_usuarios = ?";
        try (Connection con = dbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id_usuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Usuarios_getset(
                            rs.getInt("ID_usuarios"),
                            rs.getInt("ID_rol"),
                            rs.getString("tipo_dc"),
                            rs.getString("numero"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("email_insti"),
                            rs.getString("direccion"),
                            rs.getString("contacto1"),
                            rs.getString("contacto2"),
                            rs.getString("clave"),
                            rs.getString("estado")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Listar todos los usuarios
    public ArrayList<Usuarios_getset> listarUsuariosPorRol(String rol) {
        ArrayList<Usuarios_getset> lista = new ArrayList<>();
        String query = "SELECT * FROM usuarios WHERE ID_rol = (SELECT ID_rol FROM rol WHERE rol =?)";
        try (Connection con = dbConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, rol);
            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    Usuarios_getset usuario = new Usuarios_getset(
                            rs.getInt("ID_usuarios"),
                            rs.getInt("ID_rol"),
                            rs.getString("tipo_dc"),
                            rs.getString("numero"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("email_insti"),
                            rs.getString("direccion"),
                            rs.getString("contacto1"),
                            rs.getString("contacto2"),
                            rs.getString("clave"),
                            rs.getString("estado")
                    );
                    lista.add(usuario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Agregar este método a tu clase UsuariosDAO
    public Usuarios_getset obtenerUsuarioPorDocumento(String tipoDoc, String numeroDoc) {
        Usuarios_getset usuario = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            String query = "SELECT * FROM usuarios WHERE tipo_dc = ? AND numero = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, tipoDoc);
            stmt.setString(2, numeroDoc);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Usando el constructor completo con todos los campos
                usuario = new Usuarios_getset(
                        rs.getInt("id_usuarios"),           // ID_usuarios
                        rs.getInt("id_rol"),                // ID_rol
                        rs.getString("tipo_dc"),            // tipo_dc
                        rs.getString("numero"),             // documento
                        rs.getString("nombres"),            // nombres
                        rs.getString("apellidos"),          // apellidos
                        rs.getString("email"),              // email (personal)
                        rs.getString("email_insti"),        // email_insti (institucional)
                        rs.getString("direccion"),          // direccion
                        rs.getString("contacto1"),          // contacto1
                        rs.getString("contacto2"),          // contacto2
                        rs.getString("clave"),              // clave
                        rs.getString("estado")              // estado
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al obtener usuario: " + e.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return usuario;
    }
}
