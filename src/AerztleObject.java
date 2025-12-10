public class AerztleObject {
    private String songName;

    private String album;

    private int releaseYear;

    private String streams;

    private int durationMinutes;
    private int durationSeconds;

    private int playedLive;

    private String singer;

    private boolean isASingle;


    /**
     * Constructor for AerztleObject
     * @param pAlbum Name of the song's album
     * @param pReleaseYear Release year of the song
     * @param pStreams Spotify streams of the song
     * @param pDurationMinutes Minutes duration of the song
     * @param pDurationSeconds Seconds duration of the song (excluding full minutes)
     * @param pPlayedLive Amount of times the band played the song live
     * @param pSinger Singer of the song (Bela, Farin, Rod or Sahnie)
     * @param pSingle Boolean if the song was released as a single
     */
    public AerztleObject(String pSongName, String pAlbum, int pReleaseYear, String pStreams, int pDurationMinutes, int pDurationSeconds, int pPlayedLive, String pSinger, boolean pSingle) {
        songName = pSongName;
        album = pAlbum;
        releaseYear = pReleaseYear;
        streams = pStreams;
        durationMinutes = pDurationMinutes;
        durationSeconds = pDurationSeconds;
        playedLive = pPlayedLive;
        singer = pSinger;
        isASingle = pSingle;
    }

    /**
     * Get operation for the song's name
     * @return Name of the song as a String
     */
    public String getSongName() {
        return songName;
    }

    /**
     * Get operation for the name of the song's album
     * @return Album name of the song as a String
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Get operation for the song's release year
     * @return Release year of the song as an Integer
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Get operation for the song's spotify streams
     * @return Spotify streams of the song as a String
     */
    public String getStreamsAsText() {
        return streams;
    }

    /**
     * Get operation for the song's spotify streams
     * @return Spotify streams of the song as an Integer
     */
    public int getStreamsAsInteger() {
        return Integer.parseInt(streams.replace(".", ""));
    }

    /**
     * Get operation for the song's minutes duration
     * @return Minute duration of the song (mm:--) as an Integer
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Get operation for the song's seconds duration
     * @return Seconds duration of the song (--:ss) as an Integer
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Get operation for the songs full furation in seconds
     * @return Full duration of the song as an Integer
     */
    public int getFullDurationInSecs() {
        return (durationMinutes * 60) + durationSeconds;
    }

    /**
     * Get operation for the song's word count (including backing vocals, intro and outro)
     * @return Word count of the song as an Integer
     */
    public int getLivePlays() {
        return playedLive;
    }

    /**
     * Get operation for the song's singer
     * @return Singer of the song (Bela, Farin, Rod or Sahnie) as a String
     */
    public String getSinger() {
        return singer;
    }

    /**
     * Get operation for wheter the song was released as a single
     * @return true if the song was released as a single, false otherwise
     */
    public boolean isSingle() {
        return isASingle;
    }
}
