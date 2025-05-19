package Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class AprendizGUI {

    private JComboBox<String> estado;
    private JButton confirmarButton;
    private JComboBox<String> aprendiz;    // ID_usuarios
    private JComboBox<String> ficha;       // ID_Fichas
    private JComboBox<String> modalidad;   // ID_modalidad
    private JComboBox<String> evaluador;   // ID_instructor
    private JComboBox<String> empresa;     // ID_empresas
    private JButton cancelar;
    private ArrayList<Usuarios_getset> listaAprendices;
    private ArrayList<Usuarios_getset> listaInstructores;
    private ArrayList<Integer> listaIDEmpresas = new ArrayList<>(); // ID reales de empresas

    private JPanel main;

    public JPanel getMainPanel() {
        return main;
    }

    public AprendizGUI() {
        UsuariosDAO usuariosDAO = new UsuariosDAO();

        // Lista de aprendices
        listaAprendices = usuariosDAO.listarUsuariosPorRol("aprendiz");
        DefaultComboBoxModel<String> modeloAprendiz = new DefaultComboBoxModel<>();
        for (Usuarios_getset u : listaAprendices) {
            modeloAprendiz.addElement(u.getNombres() + " " + u.getApellidos());
        }
        aprendiz.setModel(modeloAprendiz);

        // Lista de instructores
        listaInstructores = usuariosDAO.listarUsuariosPorRol("evaluador");
        DefaultComboBoxModel<String> modeloEvaluador = new DefaultComboBoxModel<>();
        for (Usuarios_getset u : listaInstructores) {
            modeloEvaluador.addElement(u.getNombres() + " " + u.getApellidos());
        }
        evaluador.setModel(modeloEvaluador);

        // Cargar empresas, fichas y modalidades
        cargarEmpresas();
        cargarFichas();
        cargarModalidad();

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedIndexAprendiz = aprendiz.getSelectedIndex();
                    if (selectedIndexAprendiz == -1 || listaAprendices.isEmpty()) {
                        JOptionPane.showMessageDialog(main, "Por favor, seleccione un aprendiz.");
                        return;
                    }
                    int idUsuario = listaAprendices.get(selectedIndexAprendiz).getID_usuarios();

                    int selectedIndexEvaluador = evaluador.getSelectedIndex();
                    if (selectedIndexEvaluador == -1 || listaInstructores.isEmpty()) {
                        JOptionPane.showMessageDialog(main, "Por favor, seleccione un evaluador.");
                        return;
                    }
                    int idInstructor = listaInstructores.get(selectedIndexEvaluador).getID_usuarios();

                    int idFicha = ficha.getSelectedIndex() + 1;
                    int idEmpresa = listaIDEmpresas.get(empresa.getSelectedIndex()); // Obtener el ID real
                    int idModalidad = modalidad.getSelectedIndex() + 1;
                    String estadoSeleccionado = (String) estado.getSelectedItem();

                    Aprendiz_getset aprend = new Aprendiz_getset(
                            0,
                            idUsuario,
                            idFicha,
                            idEmpresa,
                            idInstructor,
                            idModalidad,
                            estadoSeleccionado
                    );

                    AprendizDAO dao = new AprendizDAO();
                    if (dao.crearAprendiz(aprend)) {
                        JOptionPane.showMessageDialog(main, "Aprendiz agregado correctamente");
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(main, "Error al agregar aprendiz");
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(main, "Error en los datos ingresados");
                    ex.printStackTrace();
                }
            }
        });

        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }

    private void limpiarCampos() {
        if (aprendiz.getItemCount() > 0) aprendiz.setSelectedIndex(0);
        if (empresa.getItemCount() > 0) empresa.setSelectedIndex(0);
        if (evaluador.getItemCount() > 0) evaluador.setSelectedIndex(0);
        if (modalidad.getItemCount() > 0) modalidad.setSelectedIndex(0);
        if (ficha.getItemCount() > 0) ficha.setSelectedIndex(0);
        if (estado.getItemCount() > 0) estado.setSelectedIndex(0);
    }

    private void cargarEmpresas() {
        String url = "jdbc:mysql://localhost:3306/saep";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ID_empresas, nombre_empresa FROM empresas")) {

            DefaultComboBoxModel<String> modeloEmpresa = new DefaultComboBoxModel<>();
            listaIDEmpresas.clear();

            while (rs.next()) {
                listaIDEmpresas.add(rs.getInt("ID_empresas"));
                modeloEmpresa.addElement(rs.getString("nombre_empresa"));
            }

            empresa.setModel(modeloEmpresa);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(main, "Error al cargar empresas");
            ex.printStackTrace();
        }
    }

    private void cargarFichas() {
        String url = "jdbc:mysql://localhost:3306/saep";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT codigo FROM fichas")) {

            DefaultComboBoxModel<String> modeloFichas = new DefaultComboBoxModel<>();
            while (rs.next()) {
                modeloFichas.addElement(rs.getString("codigo"));
            }

            ficha.setModel(modeloFichas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(main, "Error al cargar fichas");
            ex.printStackTrace();
        }
    }

    private void cargarModalidad() {
        String url = "jdbc:mysql://localhost:3306/saep";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT modalidad FROM modalidad")) {

            DefaultComboBoxModel<String> modeloModalidad = new DefaultComboBoxModel<>();
            while (rs.next()) {
                modeloModalidad.addElement(rs.getString("modalidad"));
            }

            modalidad.setModel(modeloModalidad);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(main, "Error al cargar modalidad");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Crear Aprendiz");
        frame.setContentPane(new AprendizGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
