package methods;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.api.objects.Update;
import setup.Helper;

import java.io.*;

import static setup.Vars.*;

public class Score {

    private String message;
    private String username;
    private Helper h;
    private String file;

    public Score(Update update, Helper h) {
        this.h = h;
        this.message = update.getMessage().getText();
        this.username = update.getMessage().getFrom().getUserName();
        this.file = username + ".json";
    }

    public void start() {
        add();
        score();
        hack();
    }

    public void add(){
        if (message.startsWith("/add")) {
            if (new File(username + ".json").exists()) {
                JSONObject jsonObject = parseString();
                JSONObject jObj = new JSONObject();
                Long newscore = (Long) jsonObject.get(username) + 1;
                jObj.put(username, newscore);
                updateFile(jObj);
                h.send("+1! score: " + newscore);
            } else {
                JSONObject jObj = new JSONObject();
                jObj.put(username, 1);
                newFile(jObj);
                h.send("(first point!) score: 1");
            }
        }
    }

    public void reset(){
        if (message.startsWith("/reset")) {
            String user = message.split(" ")[1];
            if (new File(user + ".json").exists()) {
                JSONObject jObj = new JSONObject();
                jObj.put(user, 0);
                updateFile(jObj);
                h.send("oof. " + user + "was reset! score: " + 0);
            } else {
                JSONObject jObj = new JSONObject();
                jObj.put(user, 1);
                newFile(jObj);
                h.send("(first point!) score: 1");
            }
        }
    }

    public void score() {
        JSONParser parser = new JSONParser();
        if (message.startsWith("/score")) {
            try {
                String user = message.split(" ")[1];
                Object obj = parser.parse(new FileReader(user + ".json"));
                JSONObject jsonObject = (JSONObject) obj;
                Long userscore = (Long) jsonObject.get(user);
                h.send(user +"'s score is: " + userscore);
            } catch (ParseException | IOException e) {
                h.error(String.valueOf(e));
            }
        }
    }

    public void hack() {
        if (message.startsWith("/hack")) {
            if (username.equals(dev) || username.equals(dev2) || username.equals(dev3)) {
                JSONObject jsonObject = parseString();
                JSONObject jObj = new JSONObject();
                String user = message.split(" ")[1];
                Long hacked = Long.valueOf(message.split(" ")[2]);
                Long current = (Long) jsonObject.get(user);
                Long newscore =  current + hacked;
                jObj.put(user, newscore);
                updateFile(jObj);
                h.send("injecting virtual currency... complete. " + user + "'s score is now: " + newscore + " B)");
            } else {
                h.send("lol you wish you had group clout");
            }
        }
    }

    private JSONObject parseString(){
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return (JSONObject) obj;
    }

    private void newFile(JSONObject obj) {
        File f = new File(file);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"))) {
            writer.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFile(JSONObject obj) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"))) {
            writer.write(obj.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
