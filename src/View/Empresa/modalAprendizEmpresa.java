package View.Empresa;

import javax.swing.*;

public class modalAprendizEmpresa {
    private JPanel main;
    private JTextField textField1;

    public void main() {
        JFrame frame = new JFrame();
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

