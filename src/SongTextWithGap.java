public class SongTextWithGap {
    private String beforeGap;
    private String gap;
    private String afterGap;
    private String songName;
    private String album;

    public SongTextWithGap(String pBeforeGap, String pGap, String pAfterGap, String pSongName, String pAlbumName) {
        beforeGap = pBeforeGap;
        gap = pGap;
        afterGap = pAfterGap;
        songName = pSongName;
        album = "images\\" + pAlbumName + ".png";
    }

    /**
     * Gets the text before the gap
     * @return String before gap
     */
    public String getBeforeGap() {
        return beforeGap;
    }

    /**
     * Gets the missing text in the gap
     * @return String in gap
     */
    public String getGap() {
        return gap;
    }

    /**
     * Gets the text after the gap
     * @return String after gap
     */
    public String getAfterGap() {
        return afterGap;
    }

    public String getSongName() {
        return songName;
    }

    public void setAlbum(String pAlbum) {
        album = "images\\" + pAlbum + ".png";
    }

    /**
     * Gets the path to the album image
     * @return path to album image
     */
    public String getAlbum() {
        return album;
    }
}
