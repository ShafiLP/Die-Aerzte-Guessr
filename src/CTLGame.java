import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class CTLGame implements TimerEvents {
    private Settings settings;
    private CTLGui gui;
    private LinkedList<SongTextWithGap> lyricsWithGaps;
    private SongTextWithGap randomTextWithGap;
    private boolean blockWrongGuesses = false;

    private int timeLimit;
    private int liveCount;
    private int score;

    /**
     * Constructor for "Complete The Lyrics" game
     * @param pSettings Settings object with all setting parameters
     */
    public CTLGame(Settings pSettings) {
        // Read settings
        settings = pSettings;
        timeLimit = settings.getCtlTimeLimit();
        liveCount = settings.getCtlLiveCount();

        // Read CTL elements from file
        lyricsWithGaps = new LinkedList<>();
        lyricsWithGaps = readSongsFromJson("data\\lyricCompletion.json", lyricsWithGaps);

        // Read only if enabled in settings
        if(settings.isCtlFarinEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionFarin", lyricsWithGaps);
        if(settings.isCtlBelaEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionBela", lyricsWithGaps);
        if(settings.isCtlSahnieEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletion.json", lyricsWithGaps);

        // Get random SongTextWithGap
        randomTextWithGap = getRandomSongTextWithGap(lyricsWithGaps);
        
        // Create GUI
        gui = new CTLGui(this, pSettings, randomTextWithGap);

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
        if(p1.trim().toLowerCase().equals(p2.trim().toLowerCase()))
        return true;
        return false;
    }

    /**
     * Checks if user input equals the missing song part
     * This is called when submit button is pressed on GUI
     * @param pUserInput input in JTextField
     */
    public void submitPressed(String pUserInput) {
        if(doMatch(randomTextWithGap.getGap(), pUserInput)) {
            // Update score
            score++;
            gui.setScoreLabel(score);

            // Update GUI
            randomTextWithGap = getRandomSongTextWithGap(lyricsWithGaps);
            gui.setInfoBarGreen();
            gui.setNewSongText(randomTextWithGap.getBeforeGap(), randomTextWithGap.getAfterGap());
            gui.setSongAndAlbum(randomTextWithGap.getSongName(), randomTextWithGap.getAlbum(), randomTextWithGap.getGap());

            // Reset timer
            timeLimit = settings.getCtlTimeLimit();
            gui.setTimerLabel(timeLimit);

            blockWrongGuesses = false;
        } else {
            // Remove one live and update GUI
            if(!settings.isCtlUnlimitedLivesEnabled()) {
                liveCount--;
                gui.removeHealth();
            }
            gui.setInfoBarRed();
            blockWrongGuesses = true;
        }
        if(liveCount == 0) {
            if(score > settings.getCtlHighscore()) {
                settings.setHighscore(score);
                saveSettings(settings);
            }
            Object[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast keine Versuche mehr übrig!\nDeine Punktzahl beträgt: " + score,
                "Lückenfüller",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    gui.dispose();         // Close the current GUI
                    new CTLGame(settings); // Restart the game
                case 1:
                    gui.dispose();         // Close the GUI & exit the game
                    System.exit(0);
            }
        }
    }

    /**
     * Gets the first letter of the solution as a hint
     * @return first letter of solution
     */
    public String requestHint() {
        return String.valueOf(randomTextWithGap.getGap().charAt(0));
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
            if(score > settings.getCtlHighscore()) {
                settings.setHighscore(score);
                saveSettings(settings);
            }
            Object[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast ALLE verfügbaren Lücken erraten!\nDeine Punktzahl beträgt: " + score,
                "Lückenfüller",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    gui.dispose();         // Close the current GUI
                    new CTLGame(settings); // Restart the game
                    return null;
                case 1:
                    gui.dispose();         // Close the GUI & exit the game
                    System.exit(0);
                    return null;
            }
        }

        int randomLyricIndex = (int) (Math.random() * pListWithData.size());
        SongTextWithGap randomSongTextWithGap =  pListWithData.get(randomLyricIndex);
        pListWithData.remove(randomLyricIndex); // Removes the object from the List to avoid reputition
        return randomSongTextWithGap;
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
     * This is called when a second passes
     */
    public void timerEvent() {
        timeLimit--;
        if(timeLimit >= 0)
        gui.setTimerLabel(timeLimit);
        if(timeLimit == 0 & !blockWrongGuesses) {
            submitPressed(""); // Submit as wrong guess when timer runs out
        }
    }
}

/**
 * TODO:
 * - Font settings
 */
