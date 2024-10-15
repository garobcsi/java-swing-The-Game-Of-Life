package swing;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("The Game Of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        frame.setLocationRelativeTo(null);

        boolean [][] matrix = new boolean[1][1];

        CardLayoutSwitcher switcher = new CardLayoutSwitcher();
        switcher.addPanel("home",new MainMenu(switcher));
        switcher.addPanel("esc",new PauseMenu(switcher));
        switcher.addPanel("matrixSize", new MatrixSizeMenu(switcher,matrix));
        switcher.addPanel("grid",new ScalableGrid(matrix));

        switcher.switchTo("home");

        frame.add(switcher);

        frame.setVisible(true);
    }
}
