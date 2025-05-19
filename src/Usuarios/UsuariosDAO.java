package Usuarios;

import java.sql.*;
import java.util.ArrayList;

public class UsuariosDAO {
    private static ConexionBD conexion = new ConexionBD();

    // Agregar usuario (asume que el ID es autoincremental en la base de datos)
    public boolean agregarUsuario(Usuarios_getset usuario) {
        String query = "INSERT INTO usuarios (ID_rol, tipo_dc, numero, nombres, apellidos, email, direccion, contacto1, contacto2, clave, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, usuario.getID_rol());
            pst.setString(2, usuario.getTipo_dc());
            pst.setString(3, usuario.getDocumento());
            pst.setString(4, usuario.getNombres());
            pst.setString(5, usuario.getApellidos());
            pst.setString(6, usuario.getEmail());
            pst.setString(7, usuario.getDireccion());
            pst.setString(8, usuario.getContacto1());
            pst.setString(9, usuario.getContacto2());
            pst.setString(10, usuario.getClave());
            pst.setString(11, usuario.getEstado());

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar usuario por ID
    public boolean actualizarUsuario(Usuarios_getset usuario) {
        String query = "UPDATE usuarios SET ID_rol = ?, tipo_dc = ?, numero = ?, nombres = ?, apellidos = ?, email = ?, direccion = ?, contacto1 = ?, contacto2 = ?, clave = ?, estado = ? WHERE ID_usuarios = ?";
        try (Connection con = conexion.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, usuario.getID_rol());
            pst.setString(2, usuario.getTipo_dc());
            pst.setString(3, usuario.getDocumento());
            pst.setString(4, usuario.getNombres());
            pst.setString(5, usuario.getApellidos());
            pst.setString(6, usuario.getEmail());
            pst.setString(7, usuario.getDireccion());
            pst.setString(8, usuario.getContacto1());
            pst.setString(9, usuario.getContacto2());
            pst.setString(10, usuario.getClave());
            pst.setString(11, usuario.getEstado());
            pst.setInt(12, usuario.getID_usuarios());

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
        try (Connection con = conexion.getConnection();
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
        try (Connection con = conexion.getConnection();
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
        try (Connection con = conexion.getConnection();
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
}
