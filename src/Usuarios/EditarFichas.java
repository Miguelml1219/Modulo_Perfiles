package Usuarios;

import javax.swing.*;
import java.awt.event.*;
import java.text.*;

public class EditarFichas {
    private JPanel main;
    private JTextField codigo;
    private JTextField fechainicio;
    private JTextField fechafinlectiva;
    private JTextField fechafin;
    private JComboBox<Integer> idprograma;
    private JComboBox<Integer> idsede;
    private JComboBox<String> modalidad;
    private JComboBox<String> jornada;
    private JComboBox<String> nivelformacion;
    private JComboBox<String> estado;
    private JButton confirmarButton;
    private JButton cancelar;

    private FichasDAO dao = new FichasDAO(ConexionBD.getConnection());
    private Fichas_setget fichaActual;

    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public JPanel getMainPanel() {
        return main;
    }

    public EditarFichas(Fichas_setget ficha) {
        this.fichaActual = ficha;
        cargarDatosFicha();
        cargarCombos();

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });

        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
                frame.dispose();
            }
        });
    }
    private void cargarCombos() {
        idprograma.removeAllItems();
        idsede.removeAllItems();

        // Cargar programas desde la base de datos
        ProgramasDAO programasDAO = new ProgramasDAO();
        for (Programas_getset programa : programasDAO.listarProgramas()) {
            idprograma.addItem(programa.getID_programas()); // sin convertir a String
        }

        // Cargar sedes desde la base de datos
        SedeDAO sedeDAO = new SedeDAO();
        for (Sede_getset sede : sedeDAO.listarSedes()) {
            idsede.addItem(sede.getID_sede()); // sin convertir a String
        }

        // Otros combos fijos
        modalidad.setModel(new DefaultComboBoxModel<>(new String[]{"Presencial", "Virtual"}));
        jornada.setModel(new DefaultComboBoxModel<>(new String[]{"Diurna", "Nocturna", "Mixta"}));
        nivelformacion.setModel(new DefaultComboBoxModel<>(new String[]{"Técnico", "Tecnólogo"}));
        estado.setModel(new DefaultComboBoxModel<>(new String[]{"Activa", "Inactiva"}));
    }



    private void cargarDatosFicha() {
        codigo.setText(fichaActual.getCodigo());
        fechainicio.setText(formatoFecha.format(fichaActual.getFecha_inicio()));
        fechafinlectiva.setText(formatoFecha.format(fichaActual.getFecha_fin_lec()));
        fechafin.setText(formatoFecha.format(fichaActual.getFecha_final()));

        idprograma.setSelectedItem(fichaActual.getNombre_programa());
        idsede.setSelectedItem(fichaActual.getNombre_sede());


        modalidad.setSelectedItem(fichaActual.getModalidad());
        jornada.setSelectedItem(fichaActual.getJornada());
        nivelformacion.setSelectedItem(fichaActual.getNivel_formacion());
        estado.setSelectedItem(fichaActual.getEstado());
    }

    private void guardarCambios() {
        try {
            fichaActual.setCodigo(codigo.getText());
            fichaActual.setFecha_inicio(formatoFecha.parse(fechainicio.getText()));
            fichaActual.setFecha_fin_lec(formatoFecha.parse(fechafinlectiva.getText()));
            fichaActual.setFecha_final(formatoFecha.parse(fechafin.getText()));
            fichaActual.setNombre_programa((String) idprograma.getSelectedItem());
            fichaActual.setNombre_sede((String) idsede.getSelectedItem());
            fichaActual.setModalidad((String) modalidad.getSelectedItem());
            fichaActual.setJornada((String) jornada.getSelectedItem());
            fichaActual.setNivel_formacion((String) nivelformacion.getSelectedItem());
            fichaActual.setEstado((String) estado.getSelectedItem());

            if (dao.actualizarFicha(fichaActual)) {
                JOptionPane.showMessageDialog(null, "Ficha actualizada correctamente.");
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
                frame.dispose();

                FichasDAO fichasDAO = new FichasDAO(ConexionBD.getConnection());
                Fichas_setget fichas = fichasDAO.obtenerFichaPorID(fichaActual.getID_Fichas());

                if(fichas != null){
                    JFrame frameEditarFichas = new JFrame("Editar Fichas");
                    EditarFichas editarFichas = new EditarFichas(fichas);
                    frameEditarFichas.setContentPane(editarFichas.getMainPanel());
                    frameEditarFichas.pack();
                    frameEditarFichas.setLocationRelativeTo(null);
                    frameEditarFichas.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar ficha.");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Error al parsear la fecha: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar cambios: " + e.getMessage());
        }
    }
}
