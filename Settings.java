package methods;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.api.objects.Update;
import setup.Helper;

import java.io.*;

public class Settings {

    private String text;
    private String chatId;
    private Helper h;

    public Settings(Update update, Helper h){
        this.h = h;
        this.text = update.getMessage().getText();
        this.chatId = update.getMessage().getChatId().toString();
    }

    public void start(){
        if(text.startsWith("/settings ")) {
            String[] commands = text.split(" ")[1].split(",");
            String canAdminsKick = commands[0];
            String canPlayGames = commands[1];
            JSONObject obj = new JSONObject();
            obj.put("can_members_kick", canAdminsKick);
            obj.put("can_play_games", canPlayGames);
            updateSettings(obj);
        }
    }

    public void updateSettings(JSONObject obj){
            File f = new File(chatId + ".json");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(chatId + ".json"), "utf-8"))) {
                writer.write(obj.toJSONString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            h.send("Settings for chat(" + chatId + ") updated. Can members kick bot: " + canMembersKick() + ", Can members play games: " + canPlayGames());
        }

    public String canMembersKick(){
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(new FileReader(chatId + ".json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return obj.get("can_members_kick").toString();
    }

    public String canPlayGames(){
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(new FileReader(chatId + ".json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return obj.get("can_play_games").toString();
    }
}
