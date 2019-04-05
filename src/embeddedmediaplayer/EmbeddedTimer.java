package embeddedmediaplayer;

@SuppressWarnings({"FieldCanBeLocal"})
public class EmbeddedTimer extends Thread{
    private static final Object monitor = new Object();
    private volatile boolean timerStarted;
    private EmbeddedMediaPlayer main;

    EmbeddedTimer(EmbeddedMediaPlayer main){
        this.main = main;
    }

    private synchronized void setTimerStarted(boolean timerStarted){
        this.timerStarted = timerStarted;
    }
    private synchronized boolean isTimerStarted() {return timerStarted;}

    @Override
    public void run(){
        System.out.println("iNIZIO TIMER");
        synchronized (monitor) {
            System.out.println("ingresso nella sezione atomica");
            //if (!isTimerStarted()) {
                System.out.println("inizio effettivo timer");
                //setTimerStarted(true);
                try {
                    System.out.println("Avviamento sleep");
                    sleep(1000);
                    System.out.println("fine sleep");
                    main.execute("start");
                    System.out.println("esecuzione di start");
                    ConnectionThread.counter = 0;
                } catch (InterruptedException ignored) {
                    System.out.println("timer interrotto");
                } finally {
                    System.out.println("fine timer");
                    //setTimerStarted(false);
                }
            //}
        }
        System.out.println("uscita dalla sezione atomica");
    }


}
