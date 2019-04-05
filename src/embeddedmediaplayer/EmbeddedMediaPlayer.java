package embeddedmediaplayer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;


@SuppressWarnings("SpellCheckingInspection")
public class EmbeddedMediaPlayer extends Application {

    private Group mediaRoot;
    private Scene scene;
    private MediaView view;
    @Override
    public void start(Stage primaryStage) {
        Configs.getConfig();
        Rectangle2D reference = Screen.getScreens().get(Configs.SCREEN.getInt()).getVisualBounds();
        primaryStage.setX(reference.getMinX());
        primaryStage.setY(reference.getMinY());
        primaryStage.setTitle("I.T.E.T. Leonardo Da Vinci");
        primaryStage.setFullScreen(false);
        mediaRoot = new Group();
        scene = new Scene(mediaRoot, 1920, 1080);
        view = new MediaView();
        mediaRoot.getChildren().add(view);
        primaryStage.setScene(scene);
        primaryStage.show();
        createSocket().start();
    }

    private EmbeddedTimer timer = new EmbeddedTimer(this);

    synchronized String execute(String cmd){
        switch (cmd){
            case "start":
                openNewVideo(scene, view, Configs.VIDEONAME.get(), null, null);
                return "done";
            case "play":
                view.getMediaPlayer().play();
                if(timer.isAlive())
                    timer.interrupt();
                return "done";
            case "pause":
                view.getMediaPlayer().pause();
                if(!timer.isAlive() && !timer.isInterrupted())
                    timer.start();
                return "done";
            case "exit":
                view.getMediaPlayer().stop();
                return "done";
            default:
                return "error";
        }
    }

    private ConnectionThread createSocket(){
        try {
            return new ConnectionThread(this, new ServerSocket(10002));
        } catch (IOException ignored) {}
        return null;
    }

    @SuppressWarnings("SameParameterValue")
    private void openNewVideo(Scene scene, MediaView view, String path, EventHandler<KeyEvent> event, Runnable onEnd, int i){
        if(view.getMediaPlayer() != null) view.getMediaPlayer().stop();
        MediaPlayer player = new MediaPlayer(new Media(getResource(path)));
        player.setOnEndOfMedia(onEnd);
        view.setMediaPlayer(player);
        scene.setRoot(mediaRoot);
        scene.setOnKeyPressed(event);
    }

    private void openNewVideo(Scene scene, MediaView view, String path, EventHandler<KeyEvent> event, Runnable onEnd){
        openNewVideo(scene, view, path, event, onEnd, -1);
    }

    private String getResource(String resourceName){
        try {
                return new File(String.format("%s/%s",
                        Configs.PATH.get(),
                        resourceName)).toURI().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(String.format("%s non è un file valido!\n", resourceName));
    }

    public static void main(String[] args) {launch(args);}
}