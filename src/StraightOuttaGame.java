import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * Class for the game "Straight Outta..."
 * Implements TimerEvents to handle timer
 */
public class StraightOuttaGame implements GameMode, TimerEvents  {
    private final Settings settings;
    private final StraightOuttaGui gui;
    private boolean multiplayer;
    private String p1;
    private String p2;
    private int currentPlayer = 1;
    private int score = 0;
    private int scoreP2 = 0;
    private int lives;
    private int livesP2;
    private int hints;
    private int hintsP2;
    private boolean blockWrongGuesses = false;
    private SongText currentSongText;
    private LinkedList<SongText> songTexts = new LinkedList<>();
    private int timeLimit;

    /**
     * Constructor for the GTOGame class
     * @param settings Settings for the game
     */
    public StraightOuttaGame(Settings settings, boolean multiplayer, String p1, String p2) {
        // Apply settings
        this.settings = settings;
        this.multiplayer = multiplayer;
        this.p1 = p1;
        this.p2 = p2;
        lives = settings.getGtoLiveCount();
        timeLimit = settings.getGtoTimeLimit();
        hints = settings.getGtoHintCount();
        livesP2 = lives;
        hintsP2 = hints;

        // Create a LinkedList containing all available song lyrics
        songTexts = readSongsFromJson("data\\lyrics.json", songTexts);

        // Read only if enabled
        if (settings.isFarinEnabled()) songTexts = readSongsFromJson("data\\lyricsFarin.json", songTexts);
        if (settings.isBelaEnabled()) songTexts = readSongsFromJson("data\\lyricsBela.json", songTexts);
        if (settings.isSahnieEnabled()) songTexts = readSongsFromJson("data\\lyricsSahnie.json", songTexts);
 
        // Get a random song text part
        currentSongText = getRandomSongText();

        // Create GUI
        gui = new StraightOuttaGui(this, currentSongText.getText(), settings, multiplayer);

        // Start timer
        if(!settings.isGtoUnlimitedTimeEnabled()) {
            TimerEventManager timer = new TimerEventManager(this);
            timer.start();
        } else {
            gui.setTimerLabel("Kein Timer");
        }
    }

    /**
     * Rerolls the current lyric and updates the GUI
     * This method is called when the user guesses the song correctly
     */
    public void songGuessed() {
        if (multiplayer) {
            if (currentPlayer == 1) {
                score++;
            } else {
                scoreP2++;
            }
            swapPlayer();
            gui.setActivePlayer(currentPlayer == 1 ? p1 : p2);
        } else {
            score++;
        }
        blockWrongGuesses = false;
        currentSongText = getRandomSongText();
        if (currentSongText == null) return;
        gui.guessTheOriginUpdate(currentSongText.getText());
        timeLimit = settings.getGtoTimeLimit(); // Reset the timer
        gui.setTimerLabel(timeLimit + "s");
        gui.updateScore(score + scoreP2);
    }

    /**
     * Handles the case when the user guesses incorrectly
     * Displays a dialog with the score and options to start a new game or exit
     */
    public void wrongGuess() {
        if (!settings.isGtoUnlimitedLivesEnabled()) {
            if (multiplayer) {
                if (currentPlayer == 1) {
                    lives--;
                } else {
                    livesP2--;
                }
            } else {
                lives--;
            }
            gui.removeHealth();
            blockWrongGuesses = true;
        }

        if (lives <= 0 || livesP2 <= 0) {
            if(settings.getGtoHighscore() < score & !multiplayer) {
                settings.setGtoHighscore(score);
                Settings.write(settings);
            }
            openEndingScreen("Du hast keine Versuche mehr übrig!", "Du hast " + score + " Punkte erreicht.");
        }
    }

    /**
     * Gets the current song name from the current lyric
     * @return the current song name
     */
    public String getCurrentSong() {
        return currentSongText.getSongName();
    }

    /**
     * Gets a SongText object from the lyrics.json file
     * Removes the SongText from the List to avoid reputition
     * Checks if the List is empty
     * @return Random SongText object
     */
    private SongText getRandomSongText() {
        // Checks if all songs were guessed
        if(songTexts.isEmpty()) {
            openEndingScreen("Du hast ALLE Textauschnitte dieser Version erraten!", "Deine Punktzahl beträgt: " + score);
            return new SongText(getCurrentSong(), getCurrentSong()); // Shouldn't be called, but must have a return value
        }

        int randomLyricIndex = (int) (Math.random() * songTexts.size());
        SongText randomSongText =  songTexts.get(randomLyricIndex);
        songTexts.remove(randomLyricIndex); // Removes the object from the List to avoid reputition
        return randomSongText;
    }

    /**
     * Gives a hint for the current song text
     * @return first letter of the current song
     */
    public String requestHint() {
        if (multiplayer) {
            if (currentPlayer == 1) {
                if (hints > 0) {
                    hints--;
                    gui.setHints("P1: " + hints + " / P2: " + hintsP2);
                    return String.valueOf(currentSongText.getSongName().charAt(0));
                } else {
                    return "Keine Hinweise mehr übrig.";
                }
            } else {
                if (hintsP2 > 0) {
                    hintsP2--;
                    gui.setHints("P1: " + hints + " / P2: " + hintsP2);
                    return String.valueOf(currentSongText.getSongName().charAt(0));
                } else {
                    return "Keine Hinweise mehr übrig.";
                }
            }
        } else {
            if (hints > 0) {
                hints--;
                gui.setHints(String.valueOf(hints));
                return String.valueOf(currentSongText.getSongName().charAt(0));
            } else {
                return "Keine Hinweise mehr übrig.";
            }
        }
    }

    /**
     * Changes the active player
     */
    public void swapPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    /**
     * Gets and returns a player name
     * @param pPlayer Number of player (1 or 2)
     * @return Player name
     */
    public String getPlayerName(int pPlayer) {
        if (pPlayer == 1) {
            return p1;
        } else {
            return p2;
        }
    }

    public int getActivePlayer() {
        return currentPlayer;
    }

    public int getHintCount() {
        return hints;
    }

    public int getHintsCountP2() {
        return hintsP2;
    }

    public void openEndingScreen(String pRow1, String pRow2) {
        gui.setInteractable(false);
        new EndingScreen(this, gui, "Straight Outta", settings.isColourfulGuiEnabled() ? settings.isDarkMode() ? new Color(90, 40, 40) : new Color(255, 220, 220) : Color.WHITE,
        new Color(150, 100, 100) , pRow1, pRow2, settings);
    }

    public void restartGame() {
        gui.dispose();
        new StraightOuttaGame(settings, multiplayer, p1, p2);
    }

    public void closeGame() {
        gui.dispose();
    }

    /**
     * Creates a LinkedListy<SongText> object which contains SongText objects with all elements from the given filepath
     * Adds all elements to an existing given LinkedList
     * @param filepath path to the JSON file with song data
     * @param songList List to add the SongText objects to
     * @return LinkedList with all songs from given file path
     */
    private LinkedList<SongText> readSongsFromJson(String filepath, LinkedList<SongText> songList) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                songList.add(new SongText(obj.getString("lyric"), obj.getString("song")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songList;
    }

    /**
     * Handles the timer event
     */
    public void timerEvent() {
        timeLimit--;
        if (timeLimit >= 0) gui.setTimerLabel(timeLimit + "s");

        if (timeLimit == 0 & !blockWrongGuesses) {
            gui.infoBarWrong();
            wrongGuess(); // Count as a wrong guess if the timer runs out
        }
    }
}
