public class Settings {
    // General settings
    private String fontType = "Folio Extra";
    private int fontSize = 12;
    private boolean showIcons = true;

    // For GTO
    private String gtoTypeOfInput = "Suchleiste";
    private boolean gtoUnlimitedTime = false;
    private boolean gtoUnlimitedLives = false;
    private boolean gtoIncludeFarinLibrary = false;
    private boolean gtoIncludeBelaLibrary = false;
    private boolean gtoIncludeSahnieLibrary = false;
    private boolean gtoActivateSupportiveSahnie = false;
    private int gtoTimeLimit = 30;
    private int gtoLiveCount = 3;
    private int gtoHighscore = 0;

    // For CTL
    private boolean ctlHardMode = false;
    private boolean ctlShowIcons = true;
    private boolean ctlUnlimitedTime = false;
    private boolean ctlUnlimitedLives = false;
    private boolean ctlIncludeFarin = false;
    private boolean ctlIncludeBela = false;
    private boolean ctlIncludeSahnie = false;
    private boolean ctlActivateSupportSahnie = false;
    private int ctlLiveCount = 3;
    private int ctlTimeLimit = 30;
    private int ctlHighscore = 0;

    // For Ärztle
    private boolean aeIncludeFarin = false;
    private boolean aeIncludeBela = false;
    private boolean aeIncludeSahnie = false;
    private int aeTries = 7;
    private int aeHighscore = 0;

    public Settings() {
        // Keep default settings
    }

    /**
     * Constructor for GTO
     * @param pShowIcons
     * @param pUnlimitedTime
     * @param pUnlimitedLives
     * @param pFarin
     * @param pBela
     * @param pSahnie
     * @param pTimeLimit
     * @param pLiveCount
     */
    public Settings(String pTypeOfInput, boolean pShowIcons, boolean pUnlimitedTime, boolean pUnlimitedLives, boolean pFarin,
    boolean pBela, boolean pSahnie, int pTimeLimit, int pLiveCount) {
        gtoTypeOfInput = pTypeOfInput;
        showIcons = pShowIcons;
        gtoUnlimitedTime = pUnlimitedTime;
        gtoUnlimitedLives = pUnlimitedLives;
        gtoIncludeFarinLibrary = pFarin;
        gtoIncludeBelaLibrary = pBela;
        gtoIncludeSahnieLibrary = pSahnie;
        gtoTimeLimit = pTimeLimit;
        gtoLiveCount = pLiveCount;
    }

    public Settings(boolean pHardmode, boolean pShowIcons, boolean pUnlimitedTime, boolean pUnlimitedLives, boolean pFarin,
    boolean pBela, boolean pSahnie, int pTimeLimit, int pLiveCount) {
        ctlHardMode = pHardmode;
        ctlShowIcons = pShowIcons;
        ctlUnlimitedTime = pUnlimitedTime;
        ctlUnlimitedLives = pUnlimitedLives;
        ctlIncludeFarin = pFarin;
        ctlIncludeBela = pBela;
        ctlIncludeSahnie = pSahnie;
        ctlTimeLimit = pTimeLimit;
        ctlLiveCount = pLiveCount;
    }

    //* GENERAL SETTINGS

    public void setFontType(String pFontType) {
        fontType = pFontType;
    }
    
    public String getFontType() {
        return fontType;
    }

    public void setFontSize(int pFontSize) {
        fontSize = pFontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setShowIcons(boolean pShowIcons) {
        showIcons = pShowIcons;
    }

    public boolean isShowIconsEnabled() {
        return showIcons;
    }

    //* GTO SETTINGS

    public void setGtoTypeOfInput(String pTypeOfInput) {
        gtoTypeOfInput = pTypeOfInput;
    }

    public String getGtoTypeOfInput() {
        return gtoTypeOfInput;
    }

    public void setGtoUnlimitedTime(boolean pUnlimitedTime) {
        gtoUnlimitedTime = pUnlimitedTime;
    }

    public boolean isGtoUnlimitedTimeEnabled() {
        return gtoUnlimitedTime;
    }

    public void setGtoUnlimitedLives(boolean pUnlimitedLives) {
        gtoUnlimitedLives = pUnlimitedLives;
    }

    public boolean isGtoUnlimitedLivesEnabled() {
        return gtoUnlimitedLives;
    }

    public void setGtoFarinLibrary(boolean pFarin) {
        gtoIncludeFarinLibrary = pFarin;
    }

    public boolean isGtoFarinEnabled() {
        return gtoIncludeFarinLibrary;
    }

    public void setGtoBelaLibrary(boolean pBela) {
        gtoIncludeBelaLibrary = pBela;
    }

    public boolean isGtoBelaEnabled() {
        return gtoIncludeBelaLibrary;
    }

    public void setGtoSahnieLibrary(boolean pSahnie) {
        gtoIncludeSahnieLibrary = pSahnie;
    }

    public boolean isGtoSahnieEnabled() {
        return gtoIncludeSahnieLibrary;
    }

    public void setGtoSupportSahnie(boolean pSupportiveSahnie) {
        gtoActivateSupportiveSahnie = pSupportiveSahnie;
    }

    public boolean isGtoSupportSahnieEnabled() {
        return gtoActivateSupportiveSahnie;
    }

    public void setGtoTimeLimit(int pTimeLimit) {
        gtoTimeLimit = pTimeLimit;
    }

    public int getGtoTimeLimit() {
        return gtoTimeLimit;
    }

    public void setGtoLiveCount(int pLiveCount) {
        gtoLiveCount = pLiveCount;
    }

    public int getGtoLiveCount() {
        return gtoLiveCount;
    }

    public void setGtoHighscore(int pHighscore) {
        gtoHighscore = pHighscore;
    }

    public int getGtoHighscore() {
        return gtoHighscore;
    }

    //* CTL SETTINGS

    public void setCtlLiveCount(int pLiveCount) {
        ctlLiveCount = pLiveCount;
    }

    public int getCtlLiveCount() {
        return ctlLiveCount;
    }

    public void setCtlTimeLimit(int pTimeLimit) {
        ctlTimeLimit = pTimeLimit;
    }

    public int getCtlTimeLimit() {
        return ctlTimeLimit;
    }

    public void setCtlHardmode(boolean pHardmode) {
        ctlHardMode = pHardmode;
    }

    public boolean isCtlHardmodeEnabled() {
        return ctlHardMode;
    }

    public void setCtlShowIcons(boolean pShowIcons) {
        ctlShowIcons = pShowIcons;
    }

    public boolean isCtlShowIconsEnabled() {
        return ctlShowIcons;
    }

    public void setCtlUnlimitedTime(boolean pUnlimitedTime) {
        ctlUnlimitedTime = pUnlimitedTime;
    }

    public boolean isCtlUnlimitedTimeEnabled() {
        return ctlUnlimitedTime;
    }

    public void setCtlUnlimitedLives(boolean pUnlimitedLives) {
        ctlUnlimitedLives = pUnlimitedLives;
    }

    public boolean isCtlUnlimitedLivesEnabled() {
        return ctlUnlimitedLives;
    }

    public void setCtlFarin(boolean pFarin) {
        ctlIncludeFarin = pFarin;
    }

    public boolean isCtlFarinEnabled() {
        return ctlIncludeFarin;
    }

    public void setCtlBela(boolean pBela) {
        ctlIncludeBela = pBela;
    }

    public boolean isCtlBelaEnabled() {
        return ctlIncludeBela;
    }

    public void setCtlSahnie(boolean pSahnie) {
        ctlIncludeSahnie = pSahnie;
    }

    public boolean isCtlSahnieEnabled() {
        return ctlIncludeSahnie;
    }

    public void setCtlSupportSahnie(boolean pSupportSahnie) {
        ctlActivateSupportSahnie = pSupportSahnie;
    }

    public boolean isCtlSupportSahnieEnabled() {
        return ctlActivateSupportSahnie;
    }

    public void setCtlHighscore(int pScore) {
        ctlHighscore = pScore;
    }
    
    public int getCtlHighscore() {
        return ctlHighscore;
    }

    //* ÄRZTLE SETTINGS

    public void setAeFarin(boolean pFarin) {
        aeIncludeFarin = pFarin;
    }
    
    public boolean isAeFarinEnabled() {
        return aeIncludeFarin;
    }

    public void setAeBela(boolean pBela) {
        aeIncludeBela = pBela;
    }

    public boolean isAeBelaEnabled() {
        return aeIncludeBela;
    }

    public void setAeSahnie(boolean pSahnie) {
        aeIncludeSahnie = pSahnie;
    }

    public boolean isAeSahnieEnabled() {
        return aeIncludeSahnie;
    }

    public void setAeTries(int pTries) {
        aeTries = pTries;
    }

    public int getAeTries() {
        return aeTries;
    }

    public void setAeHighscore(int pScore) {
        aeHighscore = pScore;
    }

    public int getAeHighscore() {
        return aeHighscore;
    }
}
