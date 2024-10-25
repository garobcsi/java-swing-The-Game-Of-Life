package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenuPanel extends JPanel implements KeyListener {
    private final CardLayoutSwitcherPanel switcher;

    PauseMenuPanel(CardLayoutSwitcherPanel switcher) {
        this.switcher = switcher;

        addKeyListener(this);
        setFocusable(true);

        render();
    }

    private void render() {
        this.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton backButton = new JButton("Back");
        JButton saveGameButton = new JButton("Save Game");
        JButton mainMenuButton = new JButton("Back To The Main Menu");

        buttonPanel.add(backButton);
        buttonPanel.add(saveGameButton);
        buttonPanel.add(mainMenuButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JLabel titleLabel = new JLabel("Pause Menu", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (this.hasFocus()) {
                switcher.switchTo("grid");
            } else {
                switcher.switchTo("pause");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
