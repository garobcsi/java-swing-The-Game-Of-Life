package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private final CardLayoutSwitcher switcher;

    MainMenu(CardLayoutSwitcher switcher) {
        this.switcher = switcher;
        setFocusable(true);
        render();
    }

    private void render() {
        JButton button = new JButton("Click Me");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switcher.registerActivePanel("esc");
                switcher.switchTo("grid");
            }
        });

        this.add(button);
    }
}
