import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;

import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import swing.CardLayoutSwitcherPanel;

public class CardLayoutSwitcherPanelTest {

    private CardLayoutSwitcherPanel switcherPanel;
    private JPanel panel1;
    private JPanel panel2;

    @BeforeEach
    public void setUp() {
        switcherPanel = new CardLayoutSwitcherPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        switcherPanel.addPanel("Panel1", panel1);
        switcherPanel.addPanel("Panel2", panel2);
    }

    @Test
    public void testAddPanel() {
        assertEquals(panel1, switcherPanel.getPanel("Panel1"));
        assertEquals(panel2, switcherPanel.getPanel("Panel2"));
        assertNull(switcherPanel.getPanel("NonExistentPanel"));
    }

    @Test
    public void testSwitchTo() {
        switcherPanel.switchTo("Panel1");
        assertTrue(switcherPanel.isVisible());

        switcherPanel.switchTo("Panel2");
        assertTrue(panel2.isVisible());
        assertFalse(panel1.isVisible());
    }

    @Test
    public void testNonExistentNames() {
        switcherPanel.switchTo("notAPage");
        switcherPanel.registerKeyListener("notAPage");
        switcherPanel.unregisterKeyListener("notAPage");
    }

    @Test
    public void testRegisterKeyListener() {
        CountDownLatch latch = new CountDownLatch(1);
        panel1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A)
                    latch.countDown();
            }
        });

        switcherPanel.switchTo("Panel2");
        switcherPanel.registerKeyListener("Panel1");

        switcherPanel.dispatchEvent(new KeyEvent(switcherPanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a'));

        try {
            assertTrue(latch.await(1000, java.util.concurrent.TimeUnit.MILLISECONDS), "KeyListener on panel1 was not triggered");
        } catch (InterruptedException e) {
            fail("Test interrupted", e);
        }
    }

    @Test
    public void testUnregisterKeyListener() {
        CountDownLatch latch = new CountDownLatch(1);
        panel1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                latch.countDown();
            }
        });

        switcherPanel.registerKeyListener("Panel1");
        switcherPanel.unregisterKeyListener("Panel1");
        switcherPanel.switchTo("Panel2");

        switcherPanel.dispatchEvent(new KeyEvent(switcherPanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A'));

        try {
            assertFalse(latch.await(500, java.util.concurrent.TimeUnit.MILLISECONDS), "KeyListener should not have been triggered");
        } catch (InterruptedException e) {
            fail("Test interrupted", e);
        }
    }
}
