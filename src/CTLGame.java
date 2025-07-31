import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class CTLGame implements TimerEvents {
    private Settings settings;
    private CTLGui gui;
    private SongTextWithGap randomTextWithGap;

    private int timeLimit;
    private int liveCount;
    private int score;

    public CTLGame(Settings pSettings) {
        // Read settings
        settings = pSettings;
        timeLimit = settings.getCtlTimeLimit();
        liveCount = settings.getCtlLiveCount();

        // Read CTL elements from file
        LinkedList<SongTextWithGap> lyricsWithGaps = new LinkedList<>();
        lyricsWithGaps = readSongsFromJson("data\\lyricCompletion.json", lyricsWithGaps);

        // Get random SongTextWithGap
        randomTextWithGap = getRandomSongTextWithGap(lyricsWithGaps);
        
        // Create GUI
        gui = new CTLGui(this, pSettings, randomTextWithGap.getBeforeGap(), randomTextWithGap.getAfterGap(), randomTextWithGap.getSongName(), randomTextWithGap.getAlbum());
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

    public void submitPressed(String pUserInput) {
        if(doMatch(randomTextWithGap.getGap(), pUserInput)) {
            System.out.println("Richtig!");
        } else {
            liveCount--;
            System.out.println("Falsch!");
        }
        if(liveCount == 0) {
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

    private SongTextWithGap getRandomSongTextWithGap(LinkedList<SongTextWithGap> pListWithData) {
        // Checks if all songs were guessed
        if(pListWithData.isEmpty()) {
            //TODO: Set highscore
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

    public void timerEvent() {
        timeLimit--;

        if(timeLimit == 0) {
            //TODO: Count as wrong
        }
    }
}
