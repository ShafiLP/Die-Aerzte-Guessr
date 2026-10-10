package com.aerzteguessr.ui;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class AutoCompleteTextField extends TextField {

    private final List<DropdownItem> suggestions;
    private final ContextMenu popupMenu = new ContextMenu();

    public AutoCompleteTextField(List<DropdownItem> suggestions) {
        this.suggestions = suggestions;

        textProperty().addListener((observable, oldValue, newValue) -> {
            updateSuggestions(newValue);
        });

        // Handle Enter
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!popupMenu.isShowing()) {
                    return;
                }

                if (!popupMenu.getItems().isEmpty()) {
                    popupMenu.getItems().get(0).fire();
                }

                event.consume();
            }
        });
    }

    private void updateSuggestions(String text) {
        String input = text.toLowerCase().trim();

        popupMenu.hide();
        popupMenu.getItems().clear();

        if (input.isEmpty()) {
            return;
        }

        List<DropdownItem> matches = suggestions.stream()
            .filter(entry ->
                    entry.getText()
                            .toLowerCase()
                            .contains(input)
            )
            .limit(10)
            .collect(Collectors.toList());

        if (matches.isEmpty()) {
            return;
        }

        for (DropdownItem entry : matches) {
            MenuItem item = new MenuItem(entry.getText());

            item.setGraphic(entry.getIcon());

            item.setOnAction(event -> {
                setText(entry.getText());
                positionCaret(getText().length());
                popupMenu.hide();
            });

            popupMenu.getItems().add(item);
        }

        // Show popup below textfield
        popupMenu.show(
            this,
            localToScreen(0, getHeight()).getX(), 
            localToScreen(0, getHeight()).getY()
        );
    }
}
