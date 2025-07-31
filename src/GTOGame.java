import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * Class for the game "Guess The Origin"
 * Implements TimerEvents to handle timer
 */
public class GTOGame implements TimerEvents {
    private Settings settings;
    private GTOGui gui;
    private int score = 0;
    private int lives;
    private boolean blockWrongGuesses = false;
    private SongText currentSongText;
    private LinkedList<SongText> songTexts = new LinkedList<>();
    private int timeLimit;

    /**
     * Constructor for the GTOGame class
     * @param pSettings Settings for the game
     */
    public GTOGame(Settings pSettings) {
        // Apply settings
        settings = pSettings;
        lives = settings.getLiveCount();
        timeLimit = settings.getTimeLimit();

        // Create a LinkedList containing all available song lyrics
        songTexts = readSongsFromJson("data\\lyrics.json", songTexts);

        // Add Farin songs if pFarin = true
        if(settings.isFarinEnabled()) songTexts = readSongsFromJson("data\\lyricsFarin.json", songTexts);

        // Add Bela songs if pBela = true
        if(settings.isBelaEnabled()) songTexts = readSongsFromJson("data\\lyricsBela.json", songTexts);

        // Add Sahnie songs if pSahnie = true
        if(settings.isSahnieEnabled()) songTexts = readSongsFromJson("data\\lyricsSahnie.json", songTexts);
 
        // Get a random song text part
        currentSongText = getRandomSongText();

        // Create GUI
        gui = new GTOGui(this, currentSongText.getText(), settings);

        // Start timer
        if(!settings.isUnlimitedTimeEnabled()) {
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
        score++;
        blockWrongGuesses = false;
        currentSongText = getRandomSongText();
        if(currentSongText == null) return;
        gui.guessTheOriginUpdate(currentSongText.getText());
        timeLimit = settings.getTimeLimit(); // Reset the timer
        gui.setTimerLabel(timeLimit + "s");
        gui.updateScore(score);
    }

    /**
     * Handles the case when the user guesses incorrectly
     * Displays a dialog with the score and options to start a new game or exit
     */
    public void wrongGuess() {
        if(!settings.isUnlimitedLivesEnabled()) {
            lives--;
            gui.removeHealth();
            blockWrongGuesses = true;
        }

        if(lives <= 0) {
            if(settings.getHighscore() < score) {
                settings.setHighscore(score);
                saveSettings(settings);
            }
            String[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast keine Versuche mehr übrig!\nDu hast " + score + " Punkte erreicht.",
                "ÄrzteGuessr",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    gui.dispose(); // Close the current GUI
                    new GTOGame(settings); // Restart the game
                    return;
                case 1:
                    gui.dispose(); // Close the GUI & exit the game
                    System.exit(0);
                    return;
            }
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
            if(settings.getHighscore() < score) {
                settings.setHighscore(score);
                saveSettings(settings);
            }
            Object[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast ALLE verfügbaren Songausschnitte erraten!\nDeine Punktzahl beträgt: " + score,
                "Errate den Ursprung",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    gui.dispose(); // Close the current GUI
                    new GTOGame(settings); // Restart the game
                    return null;
                case 1:
                    gui.dispose(); // Close the GUI & exit the game
                    System.exit(0);
                    return null;
            }
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
        return String.valueOf(currentSongText.getSongName().charAt(0));
    }

    /**
     * Creates a LinkedListy<SongText> object which contains SongText objects with all elements from the given filepath
     * Adds all elements to an existing given LinkedList
     * @param filepath path to the JSON file with song data
     * @param songList List to add the SongText objects to
     * @return
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
     * Overrides the current settings in the settings.json file
     * @param pSettings settings object to override the current settings
     */
    private void saveSettings(Settings pSettings) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("data\\settings.json")) {
            gson.toJson(pSettings, writer);
            System.out.println("Saved settings to \"data/settings.json\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the timer event
     */
    public void timerEvent() {
        timeLimit--;
        if(timeLimit >= 0)
        gui.setTimerLabel(timeLimit + "s");

        if(timeLimit == 0 & blockWrongGuesses == false) {
            gui.infoBarWrong();
            wrongGuess(); // Count as a wrong guess if the timer runs out
        }
    }
}

/**
 * TODO:
 * - Fix lyric display (currently can be too long for the window)
 * - ? Without creating a LinkedList with 1000 objects? (for performance)
 * - Font Settings
 */
