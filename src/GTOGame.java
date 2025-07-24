import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class for the game "Guess The Origin"
 */
class GTOGame implements TimerEvents {
    private GTOGui gui = new GTOGui();
    private int score = 0;
    private SongText currentSongText;
    private LinkedList<SongText> songTexts = new LinkedList<>();
    private int countdown = 30;

    /**
     * Constructor for the GTOGame class
     */
    public GTOGame() {
        //Create a LinkedList with all the lyrics from the JSON file
        try {
            String content = new String(Files.readAllBytes(Paths.get("data\\lyrics.json")));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                songTexts.add(new SongText(obj.getString("lyric"), obj.getString("song")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentSongText = getRandomSongText(); //Get a random lyric

        gui.guessTheOriginWindow(this, currentSongText.getText());
        TimerEventManager timer = new TimerEventManager(this);
        timer.run();
    }

    /**
     * Rerolls the current lyric and updates the GUI
     * This method is called when the user guesses the song correctly
     */
    public void songGuessed() {
        currentSongText = getRandomSongText();
        gui.guessTheOriginUpdate(currentSongText.getText());
        countdown = 30; // Reset the timer
        gui.setTimerLabel(countdown + "s");
        score++;
        gui.updateScore(score);
    }

    /**
     * Handles the case when the user guesses incorrectly
     * Displays a dialog with the score and options to start a new game or exit
     */
    public void wrongGuess() {
        String[] options = {"Neues Spiel", "Beenden"};
        int n = JOptionPane.showOptionDialog(
            gui,
            "Du hast falsch geraten!\nDu hast " + score + " Punkte erreicht.",
            "ÄrzteGuessr",
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION,
            null, // Icon
            options,
            options[1]
        );
        switch(n) {
            case 0:
                gui.dispose(); // Close the current GUI
                new GTOGame(); // Restart the game
                return;
            case 1:
                gui.dispose(); // Close the GUI & exit the game
                System.exit(0);
                return;
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

    /**
     * Handles the timer event
     */
    public void timerEvent() {
        if(countdown < 0) {
            String[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Die Zeit ist abgelaufen!\nDu hast " + score + " Punkte erreicht.",
                "ÄrzteGuessr",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[1]
            );
            switch(n) {
                case 0:
                    gui.dispose(); // Close the current GUI
                    new GTOGame(); // Restart the game
                    return;
                case 1:
                    gui.dispose(); // Close the GUI & exit the game
                    System.exit(0);
                    return;
            }
        }
        countdown--;
        gui.setTimerLabel(countdown + "s");
    }
}
