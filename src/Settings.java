public class Settings {
    // For GTO
    private String typeOfInput = "Dropdown Menü";
    private boolean showIcons = true;
    private boolean unlimitedTime = false;
    private boolean unlimitedLives = false;
    private boolean includeFarinLibrary = false;
    private boolean includeBelaLibrary = false;
    private boolean includeSahnieLibrary = false;
    private boolean activateSupportiveSahnie = false;
    private int timeLimit = 30;
    private int liveCount = 3;
    private int highscore = 0;

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
        typeOfInput = pTypeOfInput;
        showIcons = pShowIcons;
        unlimitedTime = pUnlimitedTime;
        unlimitedLives = pUnlimitedLives;
        includeFarinLibrary = pFarin;
        includeBelaLibrary = pBela;
        includeSahnieLibrary = pSahnie;
        timeLimit = pTimeLimit;
        liveCount = pLiveCount;
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

    public void setTypeOfInput(String pTypeOfInput) {
        typeOfInput = pTypeOfInput;
    }

    public String getTypeOfInput() {
        return typeOfInput;
    }

    public void setShowIcons(boolean pShowIcons) {
        showIcons = pShowIcons;
    }

    public boolean isShowIconsEnabled() {
        return showIcons;
    }

    public void setUnlimitedTime(boolean pUnlimitedTime) {
        unlimitedTime = pUnlimitedTime;
    }

    public boolean isUnlimitedTimeEnabled() {
        return unlimitedTime;
    }

    public void setUnlimitedLives(boolean pUnlimitedLives) {
        unlimitedLives = pUnlimitedLives;
    }

    public boolean isUnlimitedLivesEnabled() {
        return unlimitedLives;
    }

    public void setFarinLibrary(boolean pFarin) {
        includeFarinLibrary = pFarin;
    }

    public boolean isFarinEnabled() {
        return includeFarinLibrary;
    }

    public void setBelaLibrary(boolean pBela) {
        includeBelaLibrary = pBela;
    }

    public boolean isBelaEnabled() {
        return includeBelaLibrary;
    }

    public void setSahnieLibrary(boolean pSahnie) {
        includeSahnieLibrary = pSahnie;
    }

    public boolean isSahnieEnabled() {
        return includeSahnieLibrary;
    }

    public void setSupportiveSahnie(boolean pSupportiveSahnie) {
        activateSupportiveSahnie = pSupportiveSahnie;
    }

    public boolean isSupportiveSahnieEnabled() {
        return activateSupportiveSahnie;
    }

    public void setTimeLimit(int pTimeLimit) {
        timeLimit = pTimeLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setLiveCount(int pLiveCount) {
        liveCount = pLiveCount;
    }

    public int getLiveCount() {
        return liveCount;
    }

    public void setHighscore(int pHighscore) {
        highscore = pHighscore;
    }

    public int getHighscore() {
        return highscore;
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
}
