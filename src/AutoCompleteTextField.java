import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An object extending JTextField with suggestions
 */
public class AutoCompleteTextField extends JTextField {
    private final List<DropdownItem> suggestions;
    private final JPopupMenu popupMenu = new JPopupMenu();

    public AutoCompleteTextField(List<DropdownItem> pSuggestions) {
        suggestions = pSuggestions;

        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSuggestions(); }
            public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
            public void changedUpdate(DocumentEvent e) {}

            private void updateSuggestions() {
                String input = getText().toLowerCase().trim();

                popupMenu.setVisible(false);
                popupMenu.removeAll();

                if (input.isEmpty()) return;

                List<DropdownItem> matches = AutoCompleteTextField.this.suggestions.stream()
                        .filter(entry -> entry.getDropdownText().toLowerCase().contains(input))
                        .limit(10)
                        .collect(Collectors.toList());

                if (matches.isEmpty()) return;

                for (DropdownItem entry : matches) {
                    JMenuItem item = new JMenuItem(entry.getDropdownText());
                    item.setIcon(entry.getDropdownIcon());
                    item.addActionListener((ActionEvent _) -> {
                        setText(entry.getDropdownText());
                        popupMenu.setVisible(false);
                    });
                    popupMenu.add(item);
                }

                popupMenu.show(AutoCompleteTextField.this, 0, getHeight());
                popupMenu.setFocusable(false);
                requestFocusInWindow();
            }
        });
    }

    public AutoCompleteTextField(List<DropdownItem> pSuggestions, boolean pEnableIcons) {
        suggestions = pSuggestions;

        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSuggestions(); }
            public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
            public void changedUpdate(DocumentEvent e) {}

            private void updateSuggestions() {
                String input = getText().toLowerCase().trim();

                popupMenu.setVisible(false);
                popupMenu.removeAll();

                if (input.isEmpty()) return;

                List<DropdownItem> matches = AutoCompleteTextField.this.suggestions.stream()
                        .filter(entry -> entry.getDropdownText().toLowerCase().contains(input))
                        .limit(10)
                        .collect(Collectors.toList());

                if (matches.isEmpty()) return;

                for (DropdownItem entry : matches) {
                    JMenuItem item = new JMenuItem(entry.getDropdownText());
                    if (pEnableIcons) item.setIcon(entry.getDropdownIcon());
                    item.addActionListener((ActionEvent _) -> {
                        setText(entry.getDropdownText());
                        popupMenu.setVisible(false);
                    });
                    popupMenu.add(item);
                }

                popupMenu.show(AutoCompleteTextField.this, 0, getHeight());
                popupMenu.setFocusable(false);
                requestFocusInWindow();
            }
        });
    }
}
