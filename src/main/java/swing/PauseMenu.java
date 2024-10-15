package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenu extends JPanel implements KeyListener {
    private final CardLayoutSwitcher switcher;

    PauseMenu(CardLayoutSwitcher switcher) {
        this.switcher = switcher;

        addKeyListener(this);
        setFocusable(true);

        render();
    }

    private void render() {
        this.setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setLayout(new BorderLayout(10, 10));

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

        menuPanel.add(titleLabel, BorderLayout.NORTH);
        menuPanel.add(buttonPanel, BorderLayout.CENTER);

        this.add(menuPanel);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (this.hasFocus()) {
                switcher.switchTo("grid");
            } else {
                switcher.switchTo("esc");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
