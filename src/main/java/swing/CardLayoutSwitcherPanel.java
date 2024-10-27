package swing;

import java.awt.CardLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

/**
 * A JPanel that utilizes a CardLayout to switch between multiple panels.
 * This class allows adding, switching, and managing key listeners for different panels.
 * It captures key events and dispatches them to registered panels that are not currently focused.
 */
public class CardLayoutSwitcherPanel extends JPanel {

    /**
     * The CardLayout used to manage the layout of this panel.
     */
    private final CardLayout cardLayout;

    /**
     * A map that holds the association between panel names and their corresponding JPanel objects.
     */
    private final Map<String, JPanel> panelMap;

    /**
     * A set that keeps track of registered panels for key listener management.
     */
    private final Set<JPanel> registeredPanels;

    /**
     * Constructs a new CardLayoutSwitcherPanel with an initialized CardLayout.
     * Sets up a KeyEventDispatcher to handle key events for registered panels.
     */
    public CardLayoutSwitcherPanel() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        panelMap = new HashMap<>();
        registeredPanels = new HashSet<>();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            for (JPanel activePanel : new HashSet<>(registeredPanels)) {
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
        });
    }

    /**
     * Adds a panel to the CardLayoutSwitcherPanel.
     * 
     * @param name The name associated with the panel.
     * @param panel The JPanel to be added.
     */
    public void addPanel(String name, JPanel panel) {
        panelMap.put(name, panel);
        this.add(panel, name);
    }

    /**
     * Retrieves a panel by its name.
     * 
     * @param name The name of the panel to retrieve.
     * @return The JPanel associated with the given name, or null if no such panel exists.
     */
    public JPanel getPanel(String name) {
        if (!panelMap.containsKey(name))
            return null;
        return panelMap.get(name);
    }

    /**
     * Switches the visible panel to the one associated with the given name.
     * 
     * @param name The name of the panel to switch to.
     */
    public void switchTo(String name) {
        if (!panelMap.containsKey(name))
            return;
        cardLayout.show(this, name);
        panelMap.get(name).requestFocusInWindow();
    }

    /**
     * Registers a JPanel to receive key events when it is not focused.
     * 
     * @param name The name of the panel to register.
     */
    public void registerKeyListener(String name) {
        if (!panelMap.containsKey(name))
            return;
        JPanel panel = panelMap.get(name);
        registeredPanels.add(panel);
        panel.requestFocusInWindow();
    }

    /**
     * Unregisters a JPanel from receiving key events.
     * 
     * @param name The name of the panel to unregister.
     */
    public void unregisterKeyListener(String name) {
        if (!panelMap.containsKey(name))
            return;
        JPanel panel = panelMap.get(name);
        registeredPanels.remove(panel);
    }
}
