import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class for the game "Guess The Origin"
 * Implements TimerEvents to handle timer
 */
class GTOGame implements TimerEvents {
    private Settings settings;
    private GTOGui gui;
    private int score = 0;
    private int lives = 3; 
    private boolean blockWrongGuesses = false;
    private SongText currentSongText;
    private LinkedList<SongText> songTexts = new LinkedList<>();
    private int timeLimit = 30;

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
        // TODO

        // Add Bela songs if pBela = true
        // TODO

        // Add Sahnie songs if pSahnie = true
        if(settings.isSahnieEnabled()) songTexts = readSongsFromJson("data\\sahnieLyrics.json", songTexts);
 
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
        blockWrongGuesses = false;
        currentSongText = getRandomSongText();
        gui.guessTheOriginUpdate(currentSongText.getText());
        timeLimit = settings.getTimeLimit(); // Reset the timer
        gui.setTimerLabel(timeLimit + "s");
        score++;
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
            String[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast falsch geraten!\nDu hast " + score + " Punkte erreicht.",
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
     * Gets a random lyric from the lyrics.json file
     * @return random Lyric object
     */
    private SongText getRandomSongText() {
        int randomLyricIndex = (int) (Math.random() * songTexts.size());
        return songTexts.get(randomLyricIndex);
    }

    public LinkedList<SongText> readSongsFromJson(String filepath, LinkedList<SongText> songList) {
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
        gui.setTimerLabel(timeLimit + "s");

        if(timeLimit == 0 & blockWrongGuesses == false) {
            gui.infoBarWrong();
            wrongGuess(); // Count as a wrong guess if the timer runs out
        }
    }
}

//TODO:
// - Add a highscore system
// - QoL improvements for the dropdown menu
// - Fix lyric display (currently can be too long for the window)
// - Avoid reputition
