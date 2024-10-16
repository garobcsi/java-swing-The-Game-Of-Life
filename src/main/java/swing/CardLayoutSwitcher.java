package swing;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CardLayoutSwitcher extends JPanel {

    private final CardLayout cardLayout;
    private final Map<String, JPanel> panelMap;
    private final Set<JPanel> activePanels;

    public CardLayoutSwitcher() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        panelMap = new HashMap<>();
        activePanels = new HashSet<>();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {

                for (JPanel activePanel : activePanels) {
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

    public void registerActivePanel(String name) {
        if (!panelMap.containsKey(name))
            return;
        JPanel panel = panelMap.get(name);
        activePanels.add(panel);
        panel.requestFocusInWindow();
    }

    public void unregisterActivePanel(String name) {
        if (!panelMap.containsKey(name))
            return;
        JPanel panel = panelMap.get(name);
        activePanels.remove(panel);
    }
}
