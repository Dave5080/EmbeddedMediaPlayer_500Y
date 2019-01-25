package embeddedmediaplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("ALL")
public class ConnectionThread extends Thread{
    private Socket client;
    private EmbeddedMediaPlayer main;
    public  ConnectionThread(EmbeddedMediaPlayer main, Socket socket){
        this.main = main;
        this.client = socket;
    }

    @Override
    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            String input;
            do {
                input = reader.readLine();
                writer.println(main.execute(input));
            } while (input.equalsIgnoreCase("exit"));
        } catch (IOException ignored) {}
    }
}
