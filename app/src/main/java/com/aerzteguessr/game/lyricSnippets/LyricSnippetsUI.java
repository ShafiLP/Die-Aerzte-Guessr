package com.aerzteguessr.game.lyricSnippets;

import com.aerzteguessr.service.GameTimer;
import com.aerzteguessr.service.JsonParser;
import com.aerzteguessr.ui.AutoCompleteTextField;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LyricSnippetsUI extends BorderPane {

    private LyricSnippetsControl control;

    // Global services
    private GameTimer gameTimer;

    // Global UI elements
    private final Label snippet;
    private final Label scoreLabel;
    private final Label timerLabel;
    private final HBox healthBox;
    private final AutoCompleteTextField searchField;

    // Constants
    private final int HEADER_HEIGHT = 30;
    private final int BUTTON_WIDTH = 100;
    private final int TIMER_SECS = 60;
    private final int HEALTH = 3;

    public LyricSnippetsUI() {
        gameTimer = new GameTimer(TIMER_SECS);
        setStyle("-fx-background-color: -snippets-background");


        // ===== TOP =====
        HBox topBox = new HBox(4);
        topBox.setStyle("-fx-background-color: -snippets-primary;");
        topBox.setPadding(new Insets(3, 6, 3, 6));
        topBox.setPrefHeight(HEADER_HEIGHT + 6);
        topBox.setMinHeight(HEADER_HEIGHT + 6);
        topBox.setMaxHeight(HEADER_HEIGHT + 6);

        // Health points
        healthBox = new HBox(2);
        healthBox.setPrefHeight(HEADER_HEIGHT);
        Image healthImg = new Image(
            getClass().getResourceAsStream("/images/health.png"),
            HEADER_HEIGHT,
            HEADER_HEIGHT,
            true,
            true
        );
        for (int i = 0; i < HEALTH; i++) {
            healthBox.getChildren().add(new ImageView(healthImg) {{
                setPreserveRatio(true);
                setSmooth(true);
                setFitHeight(HEADER_HEIGHT);
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
        timerLabel.getStyleClass().add("info");

        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("info");
        timerScoreBox.getChildren().addAll(timerLabel, scoreLabel);

        topBox.getChildren().addAll(healthBox, getHorizontalSpacer(), headerImage, getHorizontalSpacer(), timerScoreBox);
        setTop(topBox);


        // ===== CENTER =====
        StackPane centerBox = new StackPane();

        snippet = new Label("Loading...");
        snippet.getStyleClass().add("snippet");
        snippet.setStyle("-fx-visibility: collapsed;");
        snippet.setWrapText(true);

        centerBox.getChildren().addAll(snippet);
        setCenter(centerBox);


        // ===== BOTTOM =====
        HBox bottomBox = new HBox(4);
        bottomBox.setPadding(new Insets(5, 5, 5, 5));

        // Search Box
        searchField = new AutoCompleteTextField(JsonParser.getInstance().getSongsAsDropdownItems());
        searchField.getStyleClass().add("snippets-text-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        // Hint Button
        Button hintButton = new Button("Hinweis");
        hintButton.getStyleClass().add("snippets-button");
        hintButton.setPrefWidth(BUTTON_WIDTH);

        // Submit Button
        Button submitButton = new Button("Raten");
        submitButton.getStyleClass().add("snippets-button");
        submitButton.setPrefWidth(BUTTON_WIDTH);
        submitButton.setOnAction(event -> {
            submitButtonPressed();
        });

        searchField.prefHeightProperty().bind(submitButton.heightProperty());

        bottomBox.getChildren().addAll(searchField, hintButton, submitButton);
        setBottom(bottomBox);


        // ===== SHORTCUTS =====
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) submitButton.fire();
        });
    }

    private void submitButtonPressed() {
        if (control == null) return;

        control.compareGuess(searchField.getText());
        searchField.clear();
    }

    public void setSnippet(String text) {
        snippet.setText(text);
    }

    public void removeHealthIcon() {
        healthBox.getChildren().removeLast();
    }

    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void setControlClass(LyricSnippetsControl control) {
        this.control = control;
    }

    public void startTimer() {
        if (gameTimer.getRemainingSeconds() > 0) gameTimer.stop();

        gameTimer = new GameTimer(TIMER_SECS);

        gameTimer.start(
            () -> Platform.runLater(() -> {
                timerLabel.setText("Timer: " + gameTimer.getRemainingSeconds() + "s");
            }),
            () -> { 
                submitButtonPressed();
            }
        );
    }

    private Region getHorizontalSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return spacer;
    }
}
