package setup;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by subar on 5/30/2017.
 */

public class Main {
    private static RootConfig rf;
    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            rf = new RootConfig();
            botsApi.registerBot(rf);
            SimpleDateFormat sdfDate = new SimpleDateFormat("hh:mm MM-dd-yyyy");
            Date now = new Date();
            String date = sdfDate.format(now);

            /*SendMessage chat1 = new SendMessage()
                    .setChatId("-1001102732445")
                    .setText("Bot online! Welcome back. (" + date + ")");

            try {
                rf.sendMessage(chat1);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }*/
        } catch (TelegramApiRequestException e1) {
            e1.printStackTrace();
        }
        console();
    }

    public static void console() {
        for (int i = 0; i < 999; i++) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Message: ");
                String msg = br.readLine();
                System.out.print("Chat id: ");
                String chat = br.readLine();
                System.out.println("Sending \"" + msg + "\" to \"@" + chat + "\"...");
                SendMessage message = new SendMessage()
                        .setChatId("@" + chat)
                        .setText(msg);
                try {
                    rf.sendMessage(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                System.out.println("Message sent succesfully!\n----------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
