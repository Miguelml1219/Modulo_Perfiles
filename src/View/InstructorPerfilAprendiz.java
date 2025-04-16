package View;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InstructorPerfilAprendiz {
    private JPanel main;
    private JButton visualizarPerfilButton;
    private JButton XYZButton;

    public InstructorPerfilAprendiz(JFrame frame) {
    }


    public static void main(String[] args) {

        JFrame frame = new JFrame("Instructor");
        InstructorPerfilAprendiz instructorPerfilAprendiz = new InstructorPerfilAprendiz(frame);
        frame.setContentPane(instructorPerfilAprendiz.main);
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


