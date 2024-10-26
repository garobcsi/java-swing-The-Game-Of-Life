package swing;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private final CardLayoutSwitcherPanel switcher;

    MainMenuPanel(CardLayoutSwitcherPanel switcher) {
        this.switcher = switcher;
        setFocusable(true);
        render();
    }

    private void render() {
        this.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton playButton = new JButton("Play");
        JButton loadGameButton = new JButton("Load Game");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(playButton);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(exitButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JLabel titleLabel = new JLabel("The Game Of Life", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 36));

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);

        playButton.addActionListener(event -> {
            switcher.switchTo("matrixSize");
        });

        loadGameButton.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON File", "json"));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
            }
        });

        exitButton.addActionListener(event -> System.exit(0));
    }
}
