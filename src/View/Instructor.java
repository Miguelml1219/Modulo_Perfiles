package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Instructor {
    private JPanel main;
    private JTable table1;

    public Instructor(JFrame frame) {
        cargarTabla();
    }

    private void cargarTabla() {
        Object[][] datos = {
                {"C.C", "111701673", "Cesar Angulo", "Colombina S.A.S", "3012534945", "👁", "👤"},
                {"C.C", "111701573", "Maikol Castañeda", "Ariel Motors S.A.S", "3012534844", "👁", "👤"},
                {"C.C", "111701473", "Miguel Medina", "IAS SOFTWARE S.A.S", "3012534743", "👁", "👤"},
        };

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Tipo");
        modelo.addColumn("Número");
        modelo.addColumn("Nombre");
        modelo.addColumn("Empresa");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Visualizar");
        modelo.addColumn("Perfil");

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }

        table1.setModel(modelo);
        table1.setRowHeight(36  ); // Altura de las filas
        modelo.setRowCount(11); // Altura de las filas
        table1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));


    }
    

    public static void main(String[] args) {

        JFrame frame = new JFrame("Instructor");
        Instructor instructor = new Instructor(frame);
        frame.setContentPane(instructor.main);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                int option = JOptionPane.showConfirmDialog(frame, "¿Está seguro de que desea salir?\nCualquier operación que esté realizando y no haya guardado se perderá.","Confirmar Salida",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if(option == JOptionPane.YES_OPTION)
                {
                    frame.dispose(); // Cierra la ventana
                    System.exit(0);
                }
            }
        });

    }
}