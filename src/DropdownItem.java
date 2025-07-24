import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class DropdownItem {
    private String dropdownText;
    private Icon dropdownIcon;

    DropdownItem(String pText, Icon pIcon) {
        dropdownText = pText;
        // Scale the icon to a smaller size for the dropdown
        ImageIcon icon = new ImageIcon(pIcon.toString());
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        dropdownIcon = new ImageIcon(img);
    }

    public String getDropdownText() {
        return dropdownText;
    }

    public Icon getDropdownIcon() {
        return dropdownIcon;
    }

    @Override
    public String toString() {
        return dropdownText; // Wichtig für Tastaturnavigation!
    }
}
