package swing;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("The Game Of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        frame.setLocationRelativeTo(null);

        CardLayoutSwitcher switcher = new CardLayoutSwitcher();
        switcher.addPanel("home",new MainMenu(switcher));
        switcher.addPanel("esc",new PauseMenu(switcher));
        switcher.addPanel("grid",new ScalableGrid(new boolean[20][20]));

        switcher.switchTo("home");

        frame.add(switcher);

        frame.setVisible(true);
    }
}
