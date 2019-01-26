package embeddedmediaplayer;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Timer extends Thread{

    private volatile boolean timerStarted;
    private EmbeddedMediaPlayer main;

    Timer(EmbeddedMediaPlayer main){
        this.main = main;
    }
    @Override
    public synchronized void interrupt(){
        synchronized (this) {
            if(timerStarted)
                super.interrupt();
        }
    }
    private void setTimerStarted(boolean timerStarted){
        synchronized (this) {
            this.timerStarted = timerStarted;
        }
    }
    @Override
    public void run(){
        setTimerStarted(true);
        try {
            Thread.sleep(Configs.TIMER.getInt());
            main.execute("stop");
        } catch (InterruptedException ignored) {
        } finally {
            setTimerStarted(false);
        }
    }
}
