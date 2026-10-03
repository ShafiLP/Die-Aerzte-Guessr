package com.aerzteguessr.service;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Navigation {
    
    private final Stage stage;
    private final Scene scene;

    public Navigation(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(new StackPane(), 800, 600);

        ThemeManager.getInstance().registerScene(scene);

        stage.setScene(scene);
    }

    public void show(Parent view) {
        scene.setRoot(view);
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }
}
