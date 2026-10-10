package com.aerzteguessr.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DropdownItem {

    private final int ICON_SIZE = 15;

    private final String text;
    private final ImageView icon;

    public DropdownItem(String text, String iconPath) {
        // Text
        this.text = text;
        
        // Icon
        Image iconImage = new Image(
            iconPath,
            ICON_SIZE,
            ICON_SIZE,
            true,
            true
        );
        this.icon = new ImageView(iconImage);
    }

    public String getText() {
        return text;
    }

    public ImageView getIcon() {
        return icon;
    }
}
