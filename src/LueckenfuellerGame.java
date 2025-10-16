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
    private Settings settings;
    private LueckenfuellerGui gui;
    private LinkedList<SongTextWithGap> lyricsWithGaps;
    private SongTextWithGap randomTextWithGap;
    private boolean blockWrongGuesses = false;

    private int timeLimit;
    private int liveCount;
    private int hintCount;
    private int score;

    /**
     * Constructor for "Complete The Lyrics" game
     * @param pSettings Settings object with all setting parameters
     */
    public LueckenfuellerGame(Settings pSettings) {
        // Read settings
        settings = pSettings;
        timeLimit = settings.getCtlTimeLimit();
        liveCount = settings.getCtlLiveCount();
        hintCount = settings.getCtlHintCount();

        // Read CTL elements from file
        lyricsWithGaps = new LinkedList<>();
        lyricsWithGaps = readSongsFromJson("data\\lyricCompletion.json", lyricsWithGaps);

        // Read only if enabled in settings
        if(settings.isFarinEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionFarin.json", lyricsWithGaps);
        if(settings.isBelaEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionBela.json", lyricsWithGaps);
        if(settings.isSahnieEnabled()) lyricsWithGaps = readSongsFromJson("data\\lyricCompletionSahnie.json", lyricsWithGaps);

        // Get random SongTextWithGap
        randomTextWithGap = getRandomSongTextWithGap(lyricsWithGaps);
        
        // Create GUI
        gui = new LueckenfuellerGui(this, pSettings, randomTextWithGap);

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
            if(!settings.isCtlUnlimitedTimeEnabled()) {
                timeLimit = settings.getCtlTimeLimit();
                gui.setTimerLabel(timeLimit);
            }
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
                settings.setCtlHighscore(score);
                saveSettings(settings);
            }
            openEndingScreen("Du hast keine Leben mehr übrig!", "Deine Punktzahl beträgt: " + score);
        }
    }

    /**
     * Gets the first letter of the solution as a hint
     * @return first letter of solution
     */
    public String requestHint() {
        if(hintCount > 0) {
            hintCount--;
            gui.setHints(hintCount);
            return String.valueOf(randomTextWithGap.getGap().charAt(0));
        } else {
            return "Keine Hinweise mehr übrig.";
        }
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
                settings.setCtlHighscore(score);
                saveSettings(settings);
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
        new EndingScreen(this, "Lückenfüller", settings.isColourfulGuiEnabled() ? new Color(220, 220, 255) : Color.WHITE,
        new Color(100, 100, 150) , pRow1, pRow2, settings);
    }

    public void restartGame() {
        gui.dispose();
        new LueckenfuellerGame(settings);
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
