import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

class GuessTheOriginGame {
    private GameGui gui = new GameGui();
    private Lyric currentLyric;
    private LinkedList<Lyric> lyrics = new LinkedList<>();

    public GuessTheOriginGame() {
        //Create a LinkedList with all the lyrics from the JSON file
        try {
            String content = new String(Files.readAllBytes(Paths.get("data\\lyrics.json")));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                lyrics.add(new Lyric(obj.getString("lyric"), obj.getString("song")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentLyric = getRandomLyric(); //Get a random lyric

        gui.guessTheOriginWindow(this, currentLyric.getLyric());
    }

    /**
     * Rerolls the current lyric and updates the GUI
     * This method is called when the user guesses the song correctly
     */
    public void songGuessed() {
        currentLyric = getRandomLyric();
        gui.guessTheOriginUpdate(currentLyric.getLyric());
    }

    /**
     * Gets the current song name from the current lyric
     * @return the current song name
     */
    public String getCurrentSong() {
        return currentLyric.getSong();
    }

    /**
     * Gets a random lyric from the lyrics.json file
     * @return random Lyric object
     */
    private Lyric getRandomLyric() {
        int randomLyricIndex = (int) (Math.random() * lyrics.size());
        return lyrics.get(randomLyricIndex);
    }
}