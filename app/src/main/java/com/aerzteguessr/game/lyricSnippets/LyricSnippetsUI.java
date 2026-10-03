package com.aerzteguessr.game.lyricSnippets;

import com.aerzteguessr.service.GameTimer;
import com.aerzteguessr.service.Navigation;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LyricSnippetsUI extends BorderPane {

    // Global services
    private final Navigation nav;
    private final GameTimer gameTimer;

    // Global UI elements
    private final Label snippet;
    private final Label timerLabel;

    // Constants
    private final int HEADER_HEIGHT = 30;
    private final int TIMER_SECS = 60;
    private final int HEALTH = 3;

    public LyricSnippetsUI(Navigation nav) {
        this.nav = nav;
        nav.setTitle("Straight Outta...");
        gameTimer = new GameTimer(TIMER_SECS);

        // ===== TOP =====
        HBox topBox = new HBox(4);
        topBox.setStyle("-fx-background-color: #ff4040;");
        topBox.setPadding(new Insets(3, 3, 3, 3));
        topBox.setPrefHeight(HEADER_HEIGHT + 6);
        topBox.setMinHeight(HEADER_HEIGHT + 6);
        topBox.setMaxHeight(HEADER_HEIGHT + 6);

        // Health points
        HBox healthBox = new HBox(2);
        healthBox.setPrefHeight(HEADER_HEIGHT);
        Image healthImg = new Image(getClass().getResourceAsStream("/images/health.png"));
        for (int i = 0; i < HEALTH; i++) {
            healthBox.getChildren().add(new ImageView(healthImg) {{
                setPreserveRatio(true);
                setSmooth(true);

                setFitHeight(HEADER_HEIGHT);
                setPreserveRatio(true);
            }});
        }

        // Header
        Image headerIcon = new Image(getClass().getResourceAsStream("/images/gamemodeTitles/straightoutta.png"));
        ImageView headerImage = new ImageView(headerIcon);
        headerImage.setFitHeight(HEADER_HEIGHT);
        headerImage.setPreserveRatio(true);

        // Timer + Score
        VBox timerScoreBox = new VBox(2);
        timerLabel = new Label("Timer: 0s");
        Label scoreLabel = new Label("Score: 0");
        timerScoreBox.getChildren().addAll(timerLabel, scoreLabel);

        topBox.getChildren().addAll(healthBox, getHorizontalSpacer(), headerImage, getHorizontalSpacer(), timerScoreBox);
        setTop(topBox);

        // ===== CENTER =====
        Pane centerBox = new Pane();

        snippet = new Label("Loading...");
        snippet.getStyleClass().add("snippet");
        snippet.setWrapText(true);

        centerBox.getChildren().addAll(snippet);
        setCenter(centerBox);

        // ===== BOTTOM =====
        HBox bottomBox = new HBox(4);

        TextField searchField = new TextField();
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button submitButton = new Button("Raten");

        bottomBox.getChildren().addAll(searchField, submitButton);
        setBottom(bottomBox);

        startTimer();
    }

    public void setSnippet(String text) {
        snippet.setText(text);
    }

    public void startTimer() {
        gameTimer.start(
            () -> Platform.runLater(() -> {
                timerLabel.setText(gameTimer.getRemainingSeconds() + "s");
            }),
            () -> { 
                // Todo
            }
        );
    }

    public void stopTimer() {
        gameTimer.stop();
    }

    private Region getHorizontalSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return spacer;
    }
}
