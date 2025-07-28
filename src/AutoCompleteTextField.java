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
    private final List<String> suggestions;
    private final JPopupMenu popupMenu = new JPopupMenu();

    public AutoCompleteTextField(List<String> suggestions) {
        this.suggestions = suggestions;

        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSuggestions(); }
            public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
            public void changedUpdate(DocumentEvent e) {}

            private void updateSuggestions() {
                String input = getText().toLowerCase().trim();

                popupMenu.setVisible(false);
                popupMenu.removeAll();

                if (input.isEmpty()) return;

                List<String> matches = AutoCompleteTextField.this.suggestions.stream()
                        .filter(item -> item.toLowerCase().contains(input))
                        .limit(10)
                        .collect(Collectors.toList());

                if (matches.isEmpty()) return;

                for (String match : matches) {
                    JMenuItem item = new JMenuItem(match);
                    item.addActionListener((ActionEvent _) -> {
                        setText(match);
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
