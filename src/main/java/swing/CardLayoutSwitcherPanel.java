package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CardLayoutSwitcherPanel extends JPanel {

    private final CardLayout cardLayout;
    private final Map<String, JPanel> panelMap;
    private final Set<JPanel> registeredPanels;

    public CardLayoutSwitcherPanel() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        panelMap = new HashMap<>();
        registeredPanels = new HashSet<>();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {

                for (JPanel activePanel : registeredPanels) {
                    if (!activePanel.hasFocus()) {
                        for (KeyListener listener : activePanel.getKeyListeners()) {
                            if (e.getID() == KeyEvent.KEY_PRESSED) {
                                listener.keyPressed(e);
                            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                                listener.keyReleased(e);
                            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                                listener.keyTyped(e);
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    public void addPanel(String name, JPanel panel) {
        panelMap.put(name, panel);
        this.add(panel, name);
    }

    public void switchTo(String name) {
        if (!panelMap.containsKey(name))
            return;
        cardLayout.show(this, name);
        panelMap.get(name).requestFocusInWindow();
    }

    public void registerKeyListener(String name) {
        if (!panelMap.containsKey(name))
            return;
        JPanel panel = panelMap.get(name);
        registeredPanels.add(panel);
        panel.requestFocusInWindow();
    }

    public void unregisterKeyListener(String name) {
        if (!panelMap.containsKey(name))
            return;
        JPanel panel = panelMap.get(name);
        registeredPanels.remove(panel);
    }
}
