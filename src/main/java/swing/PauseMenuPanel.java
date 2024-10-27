package swing;

import game.BufferedMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class PauseMenuPanel extends JPanel implements KeyListener {
    private final CardLayoutSwitcherPanel switcher;
    private final BufferedMatrix<Boolean> matrix;

    private boolean didUserSave = false;

    PauseMenuPanel(CardLayoutSwitcherPanel switcher, BufferedMatrix<Boolean> matrix) {
        this.switcher = switcher;
        this.matrix = matrix;

        addKeyListener(this);
        setFocusable(true);

        render();
    }

    private void render() {
        this.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton backButton = new JButton("Back");
        JButton controlsButton = new JButton("Game Controls");
        JButton saveGameButton = new JButton("Save Game");
        JButton mainMenuButton = new JButton("Back To The Main Menu");

        buttonPanel.add(backButton);
        buttonPanel.add(controlsButton);
        buttonPanel.add(saveGameButton);
        buttonPanel.add(mainMenuButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JLabel titleLabel = new JLabel("Pause Menu", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));

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

    @Override
    public void keyTyped(KeyEvent e) {

    }

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

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
