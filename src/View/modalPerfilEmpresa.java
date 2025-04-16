package View;

import javax.swing.*;

public class modalPerfilEmpresa {
    private JPanel main;
    private JButton comfirmarButton;
    private JTextField textField1;

    public static void main(String[] args) {
        modalPerfilEmpresa modal = new modalPerfilEmpresa();
        JFrame frame = new JFrame();
        frame.setContentPane(new modalPerfilEmpresa().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
