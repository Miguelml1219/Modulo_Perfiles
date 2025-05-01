package Example_Screen.View.Administrador;

import Example_Screen.Model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Administrador {
    private JPanel main;
    private JButton verUsuariosButton;
    private JButton crearUsuariosButton;
    private JButton ajustesButton;
    private JButton permisosButton;
    private JButton miPerfil;
    private JButton aprendices;
    private JButton evaluadores;
    private JButton coevaluadores;
    private JButton auxiliares;
    private JButton inicio;
    private JTable table1;
    private JFrame frame;
    private Usuario usuario;

    public Administrador(JFrame frame, Usuario usuario) {

        this.usuario=usuario;

        if(usuario.getRol().equalsIgnoreCase("Aprendiz"))
        {
            verUsuariosButton.setVisible(false);
            crearUsuariosButton.setVisible(false);
            permisosButton.setVisible(false);
            ajustesButton.setVisible(false);
            aprendices.setVisible(false);
            evaluadores.setVisible(false);
            coevaluadores.setVisible(false);
            auxiliares.setVisible(false);
        }

        if(usuario.getRol().equalsIgnoreCase("Admin"))
        {
            verUsuariosButton.setVisible(true);
            crearUsuariosButton.setVisible(true);
            permisosButton.setVisible(true);
            ajustesButton.setVisible(true);
            aprendices.setVisible(true);
            evaluadores.setVisible(true);
            coevaluadores.setVisible(true);
            auxiliares.setVisible(true);
        }



        verUsuariosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        crearUsuariosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        miPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        permisosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ajustesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aprendices.setCursor(new Cursor(Cursor.HAND_CURSOR));
        evaluadores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        coevaluadores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        auxiliares.setCursor(new Cursor(Cursor.HAND_CURSOR));


        verUsuariosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                verUsuariosButton.setBackground(new Color(0, 120, 50)); // Azul más claro al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                verUsuariosButton.setBackground(new Color(57,169,0)); // Restaurar color base
            }
        });

        crearUsuariosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                crearUsuariosButton.setBackground(new Color(0, 120, 50)); // Azul más claro al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                crearUsuariosButton.setBackground(new Color(57,169,0)); // Restaurar color base
            }
        });

        permisosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                permisosButton.setBackground(new Color(0, 120, 50)); // Azul más claro al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                permisosButton.setBackground(new Color(57,169,0)); // Restaurar color base
            }
        });

        miPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                miPerfil.setBackground(new Color(0, 120, 50)); // Azul más claro al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                miPerfil.setBackground(new Color(57,169,0)); // Restaurar color base
            }
        });

        ajustesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ajustesButton.setBackground(new Color(0, 120, 50)); // Azul más claro al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ajustesButton.setBackground(new Color(57,169,0)); // Restaurar color base
            }
        });

        verUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aprendices.isVisible()) {
                    // Si están visibles, ocultarlos
                    aprendices.setVisible(false);
                    evaluadores.setVisible(false);
                    coevaluadores.setVisible(false);
                    auxiliares.setVisible(false);
                } else {
                    // Si no están visibles, mostrarlos
                    aprendices.setVisible(true);
                    evaluadores.setVisible(true);
                    coevaluadores.setVisible(true);
                    auxiliares.setVisible(true);
                }
            }
        });




    }


    public static void main(String[] args) {

        Usuario usuario = new Usuario("Carlos", "Admin"); // Cambia a "Admin" para probar el otro perfil
        JFrame frame = new JFrame("SAEP");
        Administrador administrador = new Administrador(frame, usuario); // Se pasa el frame al constructor de Menu
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
