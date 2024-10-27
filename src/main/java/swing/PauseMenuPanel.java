package swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.BufferedMatrix;

/**
 * The {@code PauseMenuPanel} class represents a pause menu in the game.
 * It provides options for the player to navigate back to the game,
 * access game controls, save the current game state, or return to the main menu.
 * This panel also listens for keyboard events to allow for quick navigation using keyboard keys.
 */
public class PauseMenuPanel extends JPanel implements KeyListener {
    /**
     * The {@code CardLayoutSwitcherPanel} used to switch between different panels.
     */
    private final CardLayoutSwitcherPanel switcher;

    /**
     * The {@code BufferedMatrix} that represents the game state.
     */
    private final BufferedMatrix<Boolean> matrix;

    /**
     * A flag to indicate whether the user has saved their progress.
     */
    private boolean didUserSave = false;

    /**
     * Constructs a new {@code PauseMenuPanel} with the specified switcher and matrix.
     *
     * @param switcher the {@code CardLayoutSwitcherPanel} used for panel switching
     * @param matrix   the {@code BufferedMatrix} representing the current game state
     */
    public PauseMenuPanel(CardLayoutSwitcherPanel switcher, BufferedMatrix<Boolean> matrix) {
        this.switcher = switcher;
        this.matrix = matrix;

        addKeyListener(this);
        setFocusable(true);

        render();
    }

    /**
     * Initializes and lays out the components in the pause menu.
     * This method sets up the buttons for navigating the pause menu,
     * and it defines their corresponding actions when clicked.
     */
    private void render() {
        this.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton backButton = createButton("Back");
        JButton controlsButton = createButton("Game Controls");
        JButton saveGameButton = createButton("Save Game");
        JButton mainMenuButton = createButton("Back To The Main Menu");

        buttonPanel.add(backButton);
        buttonPanel.add(controlsButton);
        buttonPanel.add(saveGameButton);
        buttonPanel.add(mainMenuButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JLabel titleLabel = new JLabel("Pause Menu", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 36));

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);

        backButton.addActionListener(e -> {
            switcher.switchTo("grid");
            switcher.registerKeyListener("nextStep");
            didUserSave = false;
        });

        controlsButton.addActionListener(e -> {
            switcher.switchTo("gameControls");
            switcher.unregisterKeyListener("pause");
        });

        saveGameButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON File", "json"));
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            int result = fileChooser.showSaveDialog(null);

            if (result != JFileChooser.APPROVE_OPTION) return;

            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".json")) fileToSave = new File(fileToSave.getAbsolutePath() + ".json");

            matrix.toJson(fileToSave);

            didUserSave = true;
        });

        mainMenuButton.addActionListener(e -> {
            if (!didUserSave) {
                int response = JOptionPane.showConfirmDialog(this,
                        "You have unsaved changes. Do you really want to quit?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            switcher.unregisterKeyListener("nextStep");
            switcher.unregisterKeyListener("pause");
            switcher.switchTo("home");
        });
    }

    /**
     * Creates a JButton with the specified text.
     *
     * @param text the text to be displayed on the button
     * @return a JButton configured with the specified text
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        return button;
    }

    /**
     * Handles the key typed event. This method is not implemented.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Handles the key pressed event.
     * If the Escape key is pressed, the user is returned to the game grid.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            didUserSave = false;
            if (this.hasFocus()) {
                switcher.switchTo("grid");
                switcher.registerKeyListener("nextStep");
            } else {
                switcher.switchTo("pause");
                switcher.unregisterKeyListener("nextStep");

                CellularAutomataPanel nextStep = (CellularAutomataPanel) switcher.getPanel("nextStep");
                if (nextStep != null)
                    nextStep.stopPlaying();
            }
        }
    }

    /**
     * Handles the key released event. This method is not implemented.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
