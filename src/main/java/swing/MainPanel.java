package swing;

import game.CellularAutomata;
import game.NotMatrixException;

import javax.swing.*;

public class MainPanel {
    public static void main(String[] args) throws NotMatrixException {
        JFrame frame = new JFrame("The Game Of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        frame.setLocationRelativeTo(null);

        boolean[][] matrix = new boolean[40][40];

        CardLayoutSwitcherPanel switcher = new CardLayoutSwitcherPanel();
        switcher.addPanel("home", new MainMenuPanel(switcher));
        switcher.addPanel("pause", new PauseMenuPanel(switcher));
        switcher.addPanel("matrixSize", new MatrixSizeMenuPanel(switcher, matrix));
        switcher.addPanel("grid", new ScalableGridPanel(matrix));
        switcher.addPanel("nextStep",new CellularAutomata(matrix));

        switcher.switchTo("home");

        frame.add(switcher);

        frame.setVisible(true);
    }
}
