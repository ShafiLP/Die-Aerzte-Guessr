import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Key Listener to confirm dropdown selection with enter key.
 */
public class GTOKeyListener extends Thread implements KeyListener {
    private GTOGui gui;

    public GTOKeyListener(GTOGui pGui) {
        gui = pGui;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Check if the pressed key is Enter
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            gui.submitButtonPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No action needed on key release
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No action needed on key typed
    }
}