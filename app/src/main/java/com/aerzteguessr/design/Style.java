package com.aerzteguessr.design;

import java.awt.Color;
import java.awt.Font;

public final class Style {
    private Style() {}

    // TODO: Lese aus json
    public static final Color PRIMARY = new Color(41, 128, 185);
    public static final Color BACKGROUND = new Color(30, 30, 30);
    public static final Color TEXT = Color.WHITE;

    public static final Font FONT_NORMAL =
            new Font("Segoe UI", Font.PLAIN, 14);

    public static final Font FONT_TITLE =
            new Font("Segoe UI", Font.BOLD, 20);
}
