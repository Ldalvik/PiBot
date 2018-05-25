package methods;

import org.telegram.telegrambots.api.objects.Update;
import setup.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Ping {

    private String message;
    private Helper h;

    public Ping(Update update, Helper h) {
        this.h = h;
        this.message = update.getMessage().getText();
    }

    public void start() {
        if (message.startsWith("/ping")) {
            long start = System.currentTimeMillis();
            pinging();
            long end = System.currentTimeMillis();
            long pong = end - start;
            h.send("pong: " + pong + "ms");
        }
    }

    private static void pinging(){
        try {
            URL url = new URL("https://telegram.org");
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while (in.readLine() != null) {}
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
