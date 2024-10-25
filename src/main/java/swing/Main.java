package swing;

import game.CellularAutomata;
import game.NotMatrixException;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws NotMatrixException {
        JFrame frame = new JFrame("The Game Of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        frame.setLocationRelativeTo(null);

        boolean[][] matrix = new boolean[40][40];

        CardLayoutSwitcher switcher = new CardLayoutSwitcher();
        switcher.addPanel("home", new MainMenu(switcher));
        switcher.addPanel("pause", new PauseMenu(switcher));
        switcher.addPanel("matrixSize", new MatrixSizeMenu(switcher, matrix));
        switcher.addPanel("grid", new ScalableGrid(matrix));
        switcher.addPanel("nextStep",new CellularAutomata(matrix));

        switcher.switchTo("home");

        frame.add(switcher);

        frame.setVisible(true);
    }
}
