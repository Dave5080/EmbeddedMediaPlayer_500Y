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
    @Override
    public void start(Stage primaryStage) {
        Configs.getConfig();
        Rectangle2D reference = Screen.getScreens().get(Configs.SCREEN.getInt()).getVisualBounds();
        primaryStage.setX(reference.getMinX());
        primaryStage.setY(reference.getMinY());
        primaryStage.setTitle("I.T.E.T. Leonardo Da Vinci");
        primaryStage.setFullScreen(true);
        mediaRoot = new Group();
        Scene scene = new Scene(mediaRoot, 1920, 1080);
        MediaView mediaView = new MediaView();
        mediaRoot.getChildren().add(mediaView);
        primaryStage.setScene(scene);
        primaryStage.show();
        createSocket().start();
    }

    public String execute(String cmd){
        switch (cmd){
            case "start":
                //TODO: Cosa fare a start
                return "done";
            case "play":
                //TODO: Cosa fare a play
                return "done";
            case "pause":
                //TODO: Cosa fare a pause
                return "done";
            case "exit":
                //TODO: Cosa fare a exit
                return "done";
            default:
                //TODO: Cosa fare in error
                return "error";
        }
    }

    private ConnectionThread createSocket(){
        try {
            return new ConnectionThread(this, new ServerSocket(25000).accept());
        } catch (IOException e) {}
        return null;
    }

    @SuppressWarnings("SameParameterValue")
    private void openNewVideo(Scene scene, MediaView view, String path, EventHandler<KeyEvent> event, Runnable onEnd, int i){
        if(view.getMediaPlayer() != null) view.getMediaPlayer().stop();
        MediaPlayer player = new MediaPlayer(new Media(getResource(path,i)));
        player.setOnEndOfMedia(onEnd);
        player.setAutoPlay(true);
        view.setMediaPlayer(player);
        scene.setRoot(mediaRoot);
        scene.setOnKeyPressed(event);
    }

    private void openNewVideo(Scene scene, MediaView view, String path, EventHandler<KeyEvent> event, Runnable onEnd){
        openNewVideo(scene, view, path, event, onEnd, -1);
    }

    private String getResource(String resourceName, int i){
        try {
            if(i >= 0)
                return new File(String.format("%s%d/%s",
                        Configs.PATH.get(),
                        i,
                        resourceName)).toURI().toURL().toExternalForm();
            else
                return new File(resourceName).toURI().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(String.format("%s non è un file valido!\n", resourceName));
    }

    public static void main(String[] args) {launch(args);}
}