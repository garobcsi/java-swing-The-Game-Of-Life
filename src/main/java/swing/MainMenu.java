package swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel {
    private final CardLayoutSwitcher switcher;

    MainMenu(CardLayoutSwitcher switcher) {
        this.switcher = switcher;
        setFocusable(true);
        render();
    }

    private void render() {


        this.setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton playButton = new JButton("Play");
        JButton loadGameButton = new JButton("Load Game");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(playButton);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(exitButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        // Create the title label
        JLabel titleLabel = new JLabel("The Game Of Life", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 36));

        // Add the title to the top and buttons to the center
        menuPanel.add(titleLabel, BorderLayout.NORTH);
        menuPanel.add(buttonPanel, BorderLayout.CENTER);

        this.add(menuPanel);
    }
}
