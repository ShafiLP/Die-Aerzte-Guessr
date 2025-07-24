public class SongText {
    private String text;
    private String song;

    /**
     * Constructor for SongText
     * @param pText part of the lyrics of a song
     * @param pSong name of the song
     */
    SongText(String pText, String pSong) {
        text = pText;
        song = pSong;
    }

    /**
     * Gets the 
     * @return the lyric of the song
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the song name
     * @return the song name
     */
    public String getSongName() {
        return song;
    }
}
