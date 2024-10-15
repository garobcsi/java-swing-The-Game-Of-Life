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
        JLabel label = new JLabel("Pause Menu");
        label.setFont(new Font("Times New Roman", Font.PLAIN, 30));

        this.add(label);
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
