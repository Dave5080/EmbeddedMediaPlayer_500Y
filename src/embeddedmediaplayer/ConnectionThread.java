package embeddedmediaplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("ALL")
public class ConnectionThread extends Thread{
    private ServerSocket clientSocket;
    private EmbeddedMediaPlayer main;
    public static int counter = 0;
    public  ConnectionThread(EmbeddedMediaPlayer main, ServerSocket socket){
        this.main = main;
        this.clientSocket = socket;
    }

    @Override
    public void run(){
        try {
            Socket client = clientSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            int input;
            boolean old = false;
            String val = "";
            do {
                input = reader.read();
                switch (input){
                    case 0:
                        old = false;
                        break;
                    case 1:
                        if (old)
                            break;
                        old = true;
                        switch (++counter){
                            case 1:
                                val = "play";
                                break;
                            case 2:
                                val = "pause";
                                break;
                            case 3:
                                val = "play";
                                counter = 0;
                                break;
                        }
                        break;
                    default:
                        val = "exit";
                }
                writer.println(main.execute(val));
            } while (!val.equalsIgnoreCase("exit"));
        } catch (IOException ignored) {}
    }
}
