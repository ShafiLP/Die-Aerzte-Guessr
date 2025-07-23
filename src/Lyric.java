import java.util.LinkedList;

public class Lyric {
    String lyric;
    String song;

    Lyric(String lyric, String song) {
        this.lyric = lyric;
        this.song = song;
    }

    /**
     * Gets the lyric of the song
     * @return the lyric of the song
     */
    public String getLyric() {
        return lyric;
    }

    /**
     * Gets the song name
     * @return the song name
     */
    public String getSong() {
        return song;
    }

    public LinkedList<Lyric> readLyricsFromJson(String filename) {
        LinkedList<Lyric> lyrics = new LinkedList<>();
        // Implement JSON reading logic here
        // For example, using a library like Gson or Jackson to parse the JSON file
        // and populate the LinkedList with Lyric objects.
        return lyrics;
    }
}
