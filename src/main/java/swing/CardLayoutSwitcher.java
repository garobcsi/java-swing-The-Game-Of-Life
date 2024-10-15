package swing;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CardLayoutSwitcher extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Map<String, JPanel> panelMap;
    private Set<JPanel> activePanels;

    public CardLayoutSwitcher() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        panelMap = new HashMap<>();
        activePanels = new HashSet<>();

        this.setLayout(new BorderLayout());
        this.add(cardPanel, BorderLayout.CENTER);

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
        cardPanel.add(panel, name);
    }

    public int switchTo(String name) {
        if (!panelMap.containsKey(name))
            return 1;
        cardLayout.show(cardPanel, name);
        panelMap.get(name).requestFocusInWindow();
        return 0;
    }

    public void registerActivePanel(JPanel panel) {
        activePanels.add(panel);
        panel.requestFocusInWindow();
    }

    public void unregisterActivePanel(JPanel panel) {
        activePanels.remove(panel);
    }
}
