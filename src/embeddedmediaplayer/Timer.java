package embeddedmediaplayer;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Timer extends Thread{

    private volatile boolean timerStarted;
    private EmbeddedMediaPlayer main;

    Timer(EmbeddedMediaPlayer main){
        this.main = main;
    }

    private synchronized void setTimerStarted(boolean timerStarted){
        this.timerStarted = timerStarted;
    }
    @Override
    public void run(){
        setTimerStarted(true);
        try {
            Thread.sleep(Configs.TIMER.getInt());
            main.execute("exit");
            main.execute("start");
            ConnectionThread.counter = 0;
        } catch (InterruptedException ignored) {
        } finally {
            setTimerStarted(false);
        }
    }
}
