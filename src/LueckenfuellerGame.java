import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class LueckenfuellerGame implements GameMode, TimerEvents {
    private final Settings settings;
    private final LueckenfuellerGui gui;
    private LinkedList<SongTextWithGap> lyricsWithGaps;
    private SongTextWithGap randomTextWithGap;
    private boolean blockWrongGuesses = false;

    private int timeLimit;
    private int lives;
    private int hints;
    private int score;

    //* For Multiplayer
    private boolean multiplayer;
    private String p1;
    private String p2;
    private int currentPlayer = 1;
    private int livesP2;
    private int scoreP2 = 0;
    private int hintsP2;

    /**
     * Constructor for LueckenfuellerGame class
     * @param settings Settings object
     * @param multiplayer boolean if multiplayer is enabled
     * @param p1 Player 1 name
     * @param p2 Player 2 name
     */
    public LueckenfuellerGame(Settings settings, boolean multiplayer, String p1, String p2) {
        // Apply settings
        this.settings = settings;
        this.multiplayer = multiplayer;
        this.p1 = p1;
        this.p2 = p2;
        timeLimit = settings.getCtlTimeLimit();
        lives = settings.getCtlLiveCount();
        hints = settings.getCtlHintCount();
        livesP2 = lives;
        hintsP2 = hints;

        // Read Lueckenfueller elements from file
        lyricsWithGaps = new LinkedList<>();
        lyricsWithGaps = readSongsFromJson("data\\lyricCompletion.json", lyricsWithGaps);

        // Read only if enabled
        if (settings.isFarinEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionFarin.json", lyricsWithGaps);
        if (settings.isBelaEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionBela.json", lyricsWithGaps);
        if (settings.isSahnieEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionSahnie.json", lyricsWithGaps);

        // Get random SongTextWithGap
        randomTextWithGap = getRandomSongTextWithGap(lyricsWithGaps);
        
        // Create GUI
        gui = new LueckenfuellerGui(this, settings, multiplayer, randomTextWithGap);

        // Start timer
        if(!settings.isCtlUnlimitedTimeEnabled()) {
            TimerEventManager timer = new TimerEventManager(this);
            timer.start();
        } else {
            gui.setTimerLabel("Kein Timer");
        }
    }

    /**
     * Checks if two String are the same, ignoring upper and lower case and spaces
     * @param p1 First String to compare
     * @param p2 Second String to compare
     * @return boolean if the given Strings do match
     */
    private boolean doMatch(String p1, String p2) {
        return p1.trim().toLowerCase().equals(p2.trim().toLowerCase());
    }

    /**
     * Checks if user input equals the missing song part
     * This is called when submit button is pressed on GUI
     * @param pUserInput input in JTextField
     */
    public void submitPressed(String pUserInput) {
        // * If guess was correct
        if (doMatch(randomTextWithGap.getGap(), pUserInput)) {
            // Update score
            if (multiplayer) {
                if (currentPlayer == 1) {
                    score++;
                } else {
                    scoreP2++;
                }
                gui.setScoreLabel(score + scoreP2);
                swapPlayer();
                gui.setActivePlayer(currentPlayer == 1 ? p1 : p2);
            } else {
                score++;
                gui.setScoreLabel(score);
            }

            // Update GUI
            randomTextWithGap = getRandomSongTextWithGap(lyricsWithGaps);
            gui.setInfoBarGreen();
            gui.setNewSongText(randomTextWithGap.getBeforeGap(), randomTextWithGap.getAfterGap());
            gui.setSongAndAlbum(randomTextWithGap.getSongName(), randomTextWithGap.getAlbum(), randomTextWithGap.getGap());

            // Reset timer
            if(!settings.isCtlUnlimitedTimeEnabled()) {
                timeLimit = settings.getCtlTimeLimit();
                gui.setTimerLabel(timeLimit);
            }
            blockWrongGuesses = false;
        // *  If guess was incorrect
        } else {
            // Remove one live and update GUI
            if (!settings.isCtlUnlimitedLivesEnabled()) {
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
            }
            gui.setInfoBarRed();
            blockWrongGuesses = true;
        }

        if (lives <= 0 || livesP2 <= 0) {
            if (score > settings.getCtlHighscore() & !multiplayer) {
                settings.setCtlHighscore(score);
                Settings.write(settings);
            }
            openEndingScreen("Du hast keine Leben mehr übrig!", "Deine Punktzahl beträgt: " + score);
        }
    }

    /**
     * Gets the first letter of the solution as a hint
     * @return first letter of solution
     */
    public String requestHint() {
        if (multiplayer) {
            if (currentPlayer == 1) {
                if (hints > 0) {
                    hints--;
                    gui.setHints("P1: " + hints + " / P2: " + hintsP2);
                    return String.valueOf(randomTextWithGap.getGap().charAt(0));
                } else {
                    return "Keine Hinweise mehr übrig.";
                }
            } else {
                if (hintsP2 > 0) {
                    hintsP2--;
                    gui.setHints("P1: " + hints + " / P2: " + hintsP2);
                    return String.valueOf(randomTextWithGap.getGap().charAt(0));
                } else {
                    return "Keine Hinweise mehr übrig.";
                }
            }
        } else {
            if (hints > 0) {
                hints--;
                gui.setHints(String.valueOf(hints));
                return String.valueOf(randomTextWithGap.getGap().charAt(0));
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
        } else  {
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

    /**
     * Gets a random SongTextWithGap item from a LinkedList and removes the item to avoid reputition
     * JOptionPane with game end is shown when LinkedList is empty
     * @param pListWithData LinkedList with SongTextWithGap items
     * @return random SongTextWithGap ite form LinkedList
     */
    private SongTextWithGap getRandomSongTextWithGap(LinkedList<SongTextWithGap> pListWithData) {
        // Checks if all songs were guessed
        if(pListWithData.isEmpty()) {
            if(score > settings.getCtlHighscore() & !multiplayer) {
                settings.setCtlHighscore(score);
                Settings.write(settings);
            }
            openEndingScreen("Du hast ALLE Lücken dieser Version gefüllt!", "Deine Punktzahl beträgt. " + score);
        }

        int randomLyricIndex = (int) (Math.random() * pListWithData.size());
        SongTextWithGap randomSongTextWithGap =  pListWithData.get(randomLyricIndex);
        pListWithData.remove(randomLyricIndex); // Removes the object from the List to avoid reputition
        return randomSongTextWithGap;
    }

    public void openEndingScreen(String pRow1, String pRow2) {
        gui.setInteractable(false);
        new EndingScreen(this, gui, "Lückenfüller", settings.isColourfulGuiEnabled() ? settings.isDarkMode() ? new Color(40, 40, 90) : new Color(220, 220, 255) : Color.WHITE,
        new Color(100, 100, 150) , pRow1, pRow2, settings);
    }

    public void restartGame() {
        gui.dispose();
        new LueckenfuellerGame(settings, multiplayer, p1, p2);
    }

    public void closeGame() {
        gui.dispose();
    }

    /**
     * Adds SongTextWithGap items read from a JSON file to a LinkedList 
     * @param pFilepath path to JSON file with SongTextWithGap elements
     * @param pTextsWithGaps LinkedList to add the elements to
     * @return Given LinkedList with elements from given JSON file
     */
    private LinkedList<SongTextWithGap> readSongsFromJson(String pFilepath, LinkedList<SongTextWithGap> pTextsWithGaps) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(pFilepath)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                pTextsWithGaps.add(new SongTextWithGap(obj.getString("before"), obj.getString("gap"),
                obj.getString("after"), obj.getString("song"), obj.getString("album")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pTextsWithGaps;
    }

    /**
     * Handles the timer event
     * This is called when a second passes
     */
    public void timerEvent() {
        timeLimit--;
        if(timeLimit >= 0) gui.setTimerLabel(timeLimit);
        if(timeLimit == 0 & !blockWrongGuesses) {
            submitPressed(""); // Submit as wrong guess when timer runs out
        }
    }
}
