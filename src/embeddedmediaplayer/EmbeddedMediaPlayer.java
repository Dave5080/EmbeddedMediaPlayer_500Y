package embeddedmediaplayer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@SuppressWarnings("ALL")
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
        primaryStage.setFullScreen(true);
        mediaRoot = new Group();
        scene = new Scene(mediaRoot, 1920, 1080);
        view = new MediaView();
        mediaRoot.getChildren().add(view);
        primaryStage.setScene(scene);
        primaryStage.show();
        createSocket().start();
    }

    Timer timer = new Timer(this);
    public String execute(String cmd){
        System.out.println(cmd);
        switch (cmd){
            case "start":
                openNewVideo(scene, view, Configs.VIDEONAME.get(), ()->execute("start"));
                return "done";
            case "play":
                timer.interrupt();
                view.getMediaPlayer().play();
                return "done";
            case "pause":
                view.getMediaPlayer().pause();
                timer.start();
                return "done";
            case "stop":
                view.getMediaPlayer().pause();
                view.getMediaPlayer().seek(Duration.millis(0));
            case "exit":
                view.getMediaPlayer().stop();
                return "done";
            default:
                return "error";
        }
    }

    private ConnectionThread createSocket(){
        try {
            return new ConnectionThread(this, new ServerSocket(25000));
        } catch (IOException e) {}
        return null;
    }

    @SuppressWarnings("SameParameterValue")
    private void openNewVideo(Scene scene, MediaView view, String path, Runnable onEnd){
        if(view.getMediaPlayer() != null) view.getMediaPlayer().stop();
        MediaPlayer player = new MediaPlayer(new Media(getResource(path)));
        player.setOnEndOfMedia(onEnd);
        view.setMediaPlayer(player);
        scene.setRoot(mediaRoot);
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