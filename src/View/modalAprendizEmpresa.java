package View;

import javax.swing.*;

public class modalAprendizEmpresa {
    private JPanel main;

    public static void main(String[] args) {
        modalAprendizEmpresa modal = new modalAprendizEmpresa();
        JFrame frame = new JFrame();
        frame.setContentPane(new modalAprendizEmpresa().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

