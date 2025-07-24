public class TimerEventManager extends Thread{
    private TimerEvents timerObject;
    private int millis = 1000;;

    /**
     * Constructor for the TimerEventManager class
     * @param pTimerObject the object that will handle the timer events
     */
    public TimerEventManager(TimerEvents pTimerObject) {
        timerObject = pTimerObject;
    }
    public TimerEventManager(TimerEvents pTimerObject, int pMillis) {
        timerObject = pTimerObject;
        millis = pMillis;
    }

    /**
     * Starts the timer
     */
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(millis);
            } catch (Exception e) {
                System.out.println("Timer Error.");
            }
            try {
                timerObject.timerEvent();
            } catch (Exception e) {
                System.out.println("Timer Event Error.");
            }
            
        }
    }
}
