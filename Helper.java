package setup;

import methods.Settings;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by subar on 5/30/2017.
 */

public class Helper {

    private RootConfig rf = new RootConfig();
    private String text;
    private Long chatId;
    private Settings s;

    public Helper(Update update, Settings s) {
        this.text = update.getMessage().getText();
        this.chatId = update.getMessage().getChatId();
        this.s = s;
    }

    public Helper(Update update) {
        this.text = update.getMessage().getText();
        this.chatId = update.getMessage().getChatId();
    }

    public void sendImage() {
            URL urlObj = null;
            try {
                urlObj = new URL("heyroot.com/raspberry.html");
            URLConnection con = urlObj.openConnection();
            con.connect();
            SendPhoto sp = new SendPhoto();
            sp.setNewPhoto("raspberry_cam", con.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void contains(String sent, String response) {
        if (text.contains(sent)) {
            send(response);
        }
    }

    public void equals(String sent, String response) {
        if (text.equals(sent)) {
            SendMessage message = new SendMessage()
                    .setChatId(chatId)
                    .setText(response);
            try {
                rf.sendMessage(message);
            } catch (TelegramApiException e) {
                error(String.valueOf(e));
            }
        }
    }

    public void starts(String sent, String response) {
        if (text.startsWith(sent)) {
            SendMessage message = new SendMessage()
                    .setChatId(chatId)
                    .setText(response);
            try {
                rf.sendMessage(message);
            } catch (TelegramApiException e) {
                error(String.valueOf(e));
            }
        }
    }

    public void send(String msg) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);
        try {
            rf.sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendConsole(String chatid, String msg) {
        SendMessage message = new SendMessage()
                .setChatId(chatid)
                .setText(msg);
        try {
            rf.sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void error(String response) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(response);
        try {
            rf.sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String url(String url) {
        try {
            URL urlObj = new URL(url);
            URLConnection con = urlObj.openConnection();
            con.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String[] admins() {
        String[] admins = null;
        GetChatAdministrators adminList = new GetChatAdministrators().setChatId(chatId);
        try {
            for (int i = 0; i < rf.getChatAdministrators(adminList).size(); i++) {
                assert admins != null;
                admins[i] = rf.getChatAdministrators(adminList).listIterator(i).next().getUser().getUserName();
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public boolean canPlayGames(){
        return s.canPlayGames().equals("true");
    }

    public boolean canMembersKick(){
        return s.canMembersKick().equals("true");
    }

    /*public static void consolePic() {
        if (messageText.equals("saveForRoot")) {
            PhotoSize img = getPhoto();
            String fileurl = getFilePath(img);
            try (InputStream in = new URL("https://api.telegram.org/file/bot" + BotConfig.token + "/" + fileurl).openStream()) {
                Files.copy(in, Paths.get(dirUser(rUsername)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void log() {
        if (isReply) {
            System.out.println(replied_username + ": " + replied_text + "\nREPLY:\n" + username + ": " + text + "\n--------------------------");
        } else {
            System.out.println("(" + chatId + ")" + username + ": " + text + "\n--------------------------");
        }
    }

    public static void relay(String group, String group2) {
        if (chatId.toString().equals(group) && isReply) {
            send(replied_username + ": " + replied_text + "\n\nREPLY:\n\n" + username + ": " + text);
        } else {
            send(username + ": " + text);
        }
        if (chatId.toString().equals(group2) && isReply) {
            send(replied_username + ": " + replied_text + "\n\nREPLY:\n\n" + username + ": " + text);
        } else {
            send(username + ": " + text);
        }
    }*/
    }
