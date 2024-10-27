package swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.BufferedMatrix;

/**
 * MainMenuPanel represents the main menu of the game, allowing users to start a new game,
 * load a saved game, or exit the application.
 * 
 * <p>This panel contains buttons for navigating to different functionalities of the game.
 * It utilizes a CardLayoutSwitcherPanel to switch between different game panels.</p>
 */
public class MainMenuPanel extends JPanel {
    
    /**
     * The panel that handles switching between different game panels.
     */
    private final CardLayoutSwitcherPanel switcher;
    
    /**
     * The matrix representing the game state.
     */
    private final BufferedMatrix<Boolean> matrix;

    /**
     * Constructs a MainMenuPanel with the specified CardLayoutSwitcherPanel and BufferedMatrix.
     *
     * @param switcher The panel used for switching between different game views.
     * @param matrix   The matrix that holds the game's state data.
     */
    public MainMenuPanel(CardLayoutSwitcherPanel switcher, BufferedMatrix<Boolean> matrix) {
        this.switcher = switcher;
        this.matrix = matrix;

        setFocusable(true);
        render();
    }

    /**
     * Sets up the layout and components of the main menu.
     * This includes adding buttons for playing, loading a game, and exiting.
     */
    private void render() {
        this.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton playButton = createButton("Play");
        JButton loadGameButton = createButton("Load Game");
        JButton exitButton = createButton("Exit");

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

            if (result != JFileChooser.APPROVE_OPTION) return;

            File file = fileChooser.getSelectedFile();
            if (!file.isFile()) return;

            matrix.fromJson(file);

            switcher.switchTo("grid");
            switcher.registerKeyListener("pause");
            switcher.registerKeyListener("nextStep");
        });

        exitButton.addActionListener(event -> System.exit(0));
    }

    /**
     * Creates a JButton with the specified text.
     *
     * @param text The text to be displayed on the button.
     * @return A JButton instance with the specified text.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        return button;
    }
}
