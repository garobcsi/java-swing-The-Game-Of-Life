package swing;

import game.BufferedMatrix;
import game.CellularAutomata;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("The Game Of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        frame.setLocationRelativeTo(null);

        BufferedMatrix<Boolean> bufferedMatrix = new BufferedMatrix<>(40,40,false);

        CardLayoutSwitcherPanel switcher = new CardLayoutSwitcherPanel();
        switcher.addPanel("home", new MainMenuPanel(switcher,bufferedMatrix));
        switcher.addPanel("pause", new PauseMenuPanel(switcher,bufferedMatrix));
        switcher.addPanel("matrixSize", new MatrixSizeMenuPanel(switcher, bufferedMatrix));
        switcher.addPanel("grid", new ScalableGridPanel(bufferedMatrix));
        switcher.addPanel("nextStep",new CellularAutomataPanel(new CellularAutomata(bufferedMatrix)));

        switcher.switchTo("home");

        frame.add(switcher);

        frame.setVisible(true);
    }
}
