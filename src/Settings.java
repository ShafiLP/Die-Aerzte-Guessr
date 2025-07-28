public class Settings {
    private boolean showIcons = true;
    private boolean unlimitedTime = false;
    private boolean unlimitedLives = false;
    private boolean includeFarinLibrary = false;
    private boolean includeBelaLibrary = false;
    private boolean includeSahnieLibrary = false;
    private int timeLimit = 30;
    private int liveCount = 3;

    private int highscore = 0;

    public Settings() {
        // Keep default settings
    }

    public Settings(boolean pShowIcons, boolean pUnlimitedTime, boolean pUnlimitedLives, boolean pFarin,
    boolean pBela, boolean pSahnie, int pTimeLimit, int pLiveCount){
        showIcons = pShowIcons;
        unlimitedTime = pUnlimitedTime;
        unlimitedLives = pUnlimitedLives;
        includeFarinLibrary = pFarin;
        includeBelaLibrary = pBela;
        includeSahnieLibrary = pSahnie;
        timeLimit = pTimeLimit;
        liveCount = pLiveCount;
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
}
