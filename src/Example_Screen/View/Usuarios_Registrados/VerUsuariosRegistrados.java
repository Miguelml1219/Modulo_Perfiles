package Example_Screen.View.Usuarios_Registrados;

import Example_Screen.Connection.DBConnection;
import Example_Screen.View.Login.LoginGUI;
import Example_Screen.View.VisualizarPerfilGUI;
import Usuarios.EditarUsuario;
import Usuarios.UsuariosDAO;
import Usuarios.Usuarios_getset;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import static Example_Screen.View.Administrador.Administrador.verUsuarioPorRol;
import static Example_Screen.View.Login.LoginGUI.idUsuarioActual;
import static Example_Screen.View.Login.LoginGUI.traerIDusuario;

public class VerUsuariosRegistrados {
    private JTable table1;
    private JPanel panelVerUsuario;
    private JPanel panelFiltro;
    private JTextField busqueda;
    private JLabel tipoUsuario;
    private JPanel panelTable;
    private JScrollPane scroll;

    public JTable getTable() {
        return table1;
    }

    public JTextField getBusqueda() {
        return busqueda;
    }


    public JPanel getPanel() {
        return panelVerUsuario;
    }

    public void mostrarRol(String texto) {
        tipoUsuario.setText(texto);
    }

    public TableCellRenderer getButtonRenderer() {
        return new ButtonRenderer();
    }

    public DefaultCellEditor getButtonEditor() {
        return new ButtonEditor(new JCheckBox());
    }



    // Declarar sorter como atributo de clase
    private TableRowSorter<DefaultTableModel> sorter;

    public void setSorter(TableRowSorter<DefaultTableModel> sorter) {
        this.sorter = sorter;
    }

    public void inicializarFiltro(JTextField busqueda, JTable table1) {
        busqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            public void removeUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            public void changedUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            private void aplicarFiltro() {
                String texto = busqueda.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });
    }

    public void obtenerDatosUsuario() {
        DefaultTableModel model = new DefaultTableModel() {
            // Para evitar que las celdas sean editables
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };

        model.addColumn("Tipo de Documento");
        model.addColumn("Número");
        model.addColumn("Nombres");
        model.addColumn("Apellidos");
        model.addColumn("Email");
        model.addColumn("Ver Perfil");  // Nueva columna
        model.addColumn("Editar");  // Nueva columna

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT tipo_dc, numero, nombres, apellidos, email_insti FROM usuarios WHERE id_rol = " + verUsuarioPorRol);

            while (rs.next()) {
                Object[] dato = new Object[7];
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = "Ver Perfil"; // El texto del botón
                dato[6] = "Editar"; // Texto del nuevo botón

                model.addRow(dato);
            }

            table1.setModel(model);
            sorter = new TableRowSorter<>(model);
            table1.setRowSorter(sorter);

            // Renderizador y editor para el botón
            table1.getColumn("Ver Perfil").setCellRenderer(new ButtonRenderer());
            table1.getColumn("Ver Perfil").setCellEditor(new ButtonEditor(new JCheckBox()));
            // Renderizador y editor para el botón
            table1.getColumn("Editar").setCellRenderer(new ButtonRenderer());
            table1.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox()));




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Renderiza el botón
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "Ver Perfil" : value.toString());
            return this;
        }
    }

    // Clase ButtonEditor mejorada para manejar diferentes acciones
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private int selectedRow;
        private int selectedColumn;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.table = table;
            this.selectedRow = row;
            this.selectedColumn = column;

            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }

            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // Obtener los datos de la fila seleccionada
                String tipoDoc = (String) table.getValueAt(selectedRow, 0);
                String numeroDoc = (String) table.getValueAt(selectedRow, 1);
                String nombres = (String) table.getValueAt(selectedRow, 2);
                String apellidos = (String) table.getValueAt(selectedRow, 3);
                String email = (String) table.getValueAt(selectedRow, 4);

                // Determinar qué botón fue presionado
                String columnName = table.getColumnName(selectedColumn);

                if ("Ver Perfil".equals(columnName)) {
                    // Acción para Ver Perfil - MODIFICADO
                    abrirPerfilUsuario(numeroDoc, tipoDoc);

                } else if ("Editar".equals(columnName)) {
                    // Acción para Editar Usuario
                    editarUsuario(tipoDoc, numeroDoc);
                }
            }
            isPushed = false;
            return new String(label);
        }


        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        private void abrirPerfilUsuario(String numeroDoc, String tipoDoc) {
            try {
                // Crear una nueva ventana modal
                JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(button),
                        "Perfil de Usuario", true);


                // Crear instancia de VisualizarPerfilGUI modificada para usuario específico
                VisualizarPerfilGUI perfilGUI = new VisualizarPerfilGUI(traerIDusuario, LoginGUI.idUsuarioActual);

                // Solo mostrar datos extra si se está en la tabla de aprendices

                // Cargar los datos del usuario específico
                perfilGUI.cargarDatosUsuarioEspecifico(numeroDoc, tipoDoc);

                // Configurar la ventana modal
                dialog.setContentPane(perfilGUI.panel1);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.pack();
                dialog.setLocationRelativeTo(button);
                dialog.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(button,
                        "Error al abrir el perfil del usuario: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // Este método debe validar si se está en la vista de aprendices
        private boolean esTablaAprendicesActiva() {
            // Aquí puedes validar por nombre de tabla, contexto o pestaña activa
            // Este es un ejemplo básico, ajústalo según tu implementación real
            return true; // Retornar true solo si la tabla mostrada es la de aprendices
        }


        // Método para editar usuario
        private void editarUsuario(String tipoDoc, String numeroDoc) {
            try {
                // Crear instancia del DAO
                UsuariosDAO dao = new UsuariosDAO();

                // Buscar el usuario completo por tipo y número de documento
                Usuarios_getset usuario = dao.obtenerUsuarioPorDocumento(tipoDoc, numeroDoc);

                if (usuario != null) {
                    // Crear y mostrar la ventana de edición
                    JFrame frameEditar = new JFrame("Editar Usuario");
                    EditarUsuario editarUsuario = new EditarUsuario(usuario);
                    frameEditar.setContentPane(editarUsuario.getMainPanel());
                    frameEditar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frameEditar.pack();
                    frameEditar.setLocationRelativeTo(null);
                    frameEditar.setVisible(true);

                    // Opcional: Actualizar la tabla después de cerrar la ventana de edición
                    frameEditar.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            // Refrescar la tabla
                            obtenerDatosUsuario();
                        }
                    });

                } else {
                    JOptionPane.showMessageDialog(button,
                            "No se pudo encontrar el usuario con los datos proporcionados.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(button,
                        "Error al abrir el formulario de edición: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    String rol = null;

    public void tipoDeUsuarioRegistrado(){
        if (verUsuarioPorRol == 1){
            rol = "Aprendices Registrados";
        }
        if (verUsuarioPorRol == 2){
            rol = "Evaluadores Registrados";
        }
        if (verUsuarioPorRol == 3){
            rol = "Empresas Registradas";
        }
        else if (verUsuarioPorRol == 4) {
            rol = "Auxiliares Registrados";
        }

        tipoUsuario.setText(rol);

    }

    public void componentesPersonalizado() {
        Border bottom = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.decode("#39A900"));
        busqueda.setBorder(bottom);
        table1.getTableHeader().setForeground(Color.decode("#ffffff")); // Color del texto
        table1.getTableHeader().setBackground(Color.decode("#39A900"));

        table1.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Cuerpo de la tabla
        table1.setRowHeight(25);

        table1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); // Encabezado

        scroll.getViewport().setBackground(Color.decode("#e8e6e8"));
        scroll.getViewport().setBackground(Color.decode("#e8e6e8"));
    }
    public void tipoDeUsuarioRegistrado1() {

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT rol FROM rol WHERE id_rol = " + verUsuarioPorRol);

            if (rs.next()) {
                rol = rs.getString("rol");
                tipoUsuario.setText(rol);  // mostrar rol en el JLabel
            } else {
                tipoUsuario.setText("Rol no encontrado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            tipoUsuario.setText("Error de conexión");
        }
        tipoUsuario.setText(rol + " Registrado");
    }
}
