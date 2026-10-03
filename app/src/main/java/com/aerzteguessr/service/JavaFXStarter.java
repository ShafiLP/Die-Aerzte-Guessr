package com.aerzteguessr.service;

import javafx.application.Platform;

public final class JavaFXStarter {

    private static boolean started = false;

    private JavaFXStarter() {
    }

    public static void start() {

        if (started) {
            return;
        }

        started = true;

        Platform.startup(() -> {
            // JavaFX Toolkit ist jetzt initialisiert
        });
    }
}
