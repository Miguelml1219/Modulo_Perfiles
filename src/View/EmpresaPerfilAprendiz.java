package View;

import javax.swing.*;
        import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EmpresaPerfilAprendiz {

    private JPanel main;
    private JButton visualizarPerfilButton;
    private JButton XYZButton;
    private JFrame frame;

    public EmpresaPerfilAprendiz(JFrame frame)
    {
        this.frame=frame;

    }



    public static void main(String[] args) {

        JFrame frame = new JFrame("SAEP");
        EmpresaPerfilAprendiz empresaPerfilAprendiz = new EmpresaPerfilAprendiz(frame); // Se pasa el frame al constructor de Menu
        frame.setContentPane(empresaPerfilAprendiz.main);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //frame.setUndecorated(true);
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
