package com.aerzteguessr.game.lyricSnippets;

import java.util.Collections;
import java.util.LinkedList;

import com.aerzteguessr.models.LyricSnippet;
import com.aerzteguessr.service.JsonParser;
import com.aerzteguessr.service.Navigation;

public class LyricSnippetsControl {
    
    private final LyricSnippetsUI gui;
    private final Navigation nav;

    private final LinkedList<LyricSnippet> snippets;
    private LyricSnippet currentSnippet;

    private int health = 3;
    private int score = 0;

    public LyricSnippetsControl(LyricSnippetsUI gui, Navigation nav) {
        this.gui = gui;
        this.nav = nav;

        gui.setControlClass(this);

        snippets = JsonParser.getInstance().readSongsFromJson(
            getClass().getResource("/json/lyrics.json")
        );
        Collections.shuffle(snippets);

        insertSnippet();
    }

    private void insertSnippet() {
        if (snippets == null) return;
        if (snippets.size() <= 0) gui.initializeAndShowEndingScreen(score);
    
        currentSnippet = snippets.removeLast();

        gui.setSnippet(currentSnippet.Snippet);
        gui.startTimer();
    }

    public void compareGuess(String guess) {
        if (currentSnippet.Song.equals(guess)) {
            rightGuess();
        }
        else {
            wrongGuess();
        }

        insertSnippet();
        gui.startTimer();
    }

    private void rightGuess() {
        score++;
        gui.setScore(score);
    }

    private void wrongGuess() {
        decreaseHealth();
    }

    private void decreaseHealth() {
        health--;
        gui.removeHealthIcon();

        if (health <= 0) { 
            gui.initializeAndShowEndingScreen(score);
        }
    }

    public void endGame() {
        nav.close();
    }
}
