package View.Aprendiz;

import javax.swing.*;

public class modalPerfilAprendiz {
    private JPanel main;
    private JButton confirmarButton;
    private JTextField textField1;

    public static void main(String[] args) {
        modalPerfilAprendiz modal = new modalPerfilAprendiz();
        JFrame frame = new JFrame();
        frame.setContentPane(new modalPerfilAprendiz().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
