/**
 * Main class
 * 
 * @author ShafiLP
 * @version 0.3.1
 */
public class Main {
    public static void main(String[] args) {
        new MainMenu();
    }
}

/**
 * *Neue Spielmodi:
 * 
 * *SONG DURCH LYRICS ERKENNEN
 * Ein wenig wie der erste Modus, allerdings werden die kompletten Lyrics als Sterne (***) angezeigt
 * Nach jedem Guess wird ein Wort aufgedeckt
 * Man hat eine bestimmte Anzahl Versuche, wie bei Ärztle
 * 
 * *SONG DURCH INSTRUMENTAL ERKENNEN
 * Es wird ein 3ek Audioauschnitt abgespielt, ohne Lyrics
 * Der Spieler muss den Song innerhalb eines Zeitlimits erraten
 * Es gibt mehrere Versuche, mehrere Instrumentals werden hintereinander abgespielt, bis die Versuche auf Null sind
 * Spielmodusname: Manche nennen es Musik
 * 
 * *Straigh Outta
 * Input soll nicht angenommen werden, wenn er ungültig ist
 * Mehr mögliche Textausschnitte hinzufügen
 * Optimierung (Alle Daten aus JSON werden als Objekt erstellt. Möglich über boolean array?)
 * 
 * *LÜCKENFÜLLER
 * Diskographie sowie Solokarrieren vervollständigen
 * 
 * *ÄRZTLE
 * Solokarrieren hinzufügen
 * Optimierung (Alle Daten aus JSON werden als Objekt erstellt. Möglich über boolean array?)
 * Eine neue Kategorie anstelle von der Wortanzahl
 */