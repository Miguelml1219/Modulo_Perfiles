package View.Administrador;

import View.Aprendiz.Aprendiz;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InicioAdministrador {
    private JPanel main;
    private JButton verUsuariosButton;
    private JButton crearUsuarioButton;
    private JButton miPerfilButton;

    private JFrame frame;

    public InicioAdministrador(JFrame frame)
    {
        this.frame=frame;

    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("SAEP");
        InicioAdministrador administrador = new InicioAdministrador(frame); // Se pasa el frame al constructor de Menu
        frame.setContentPane(administrador.main);
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
