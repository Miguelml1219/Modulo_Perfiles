package View;

import javax.swing.*;

public class modalPerfilInstructor {
    private JPanel main;
    private JButton comfirmarButton;

    public static void main(String[] args) {
        modalPerfilInstructor modal = new modalPerfilInstructor();
        JFrame frame = new JFrame();
        frame.setContentPane(new modalPerfilInstructor().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
