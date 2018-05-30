package setup;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.*;
import java.util.Random;

import static setup.Helper.send;
import static setup.Vars.dir;

public class Fish {

    private Update update;
    private Helper h;
    private Long chatId;
    private String text;
    private String username;

    public Fish(Update update) {
        this.update = update;
        this.chatId = update.getMessage().getChatId();
        this.text = update.getMessage().getText();
        this.username = update.getMessage().getFrom().getUserName();
    }

    public void start() {
        fish();
        DoN();
        check();
        hack();
        leaderboard();
    }

    public void fish() {
        if (text.equals("/fish")) {
            Long currentFish = getFish(username);
            int fishCaught = new Random().nextInt(15);
            Long allFish = currentFish + fishCaught;
                setFish(username, allFish);
                send("You caught: " + fishCaught + " fish! Total: " + allFish);
        }
    }

    public void check() {
        if (text.startsWith("/fishscore ")) {
            String checkUser = text.split(" ")[1];
            Long currentFish = getFish(checkUser);
            send(checkUser + " has " + currentFish + " fish.");
        }
    }

    public void leaderboard() {
        if (text.startsWith("/fishleaderboard")) {
            send(read());
        }
    }

    public void hack() {
        if (text.startsWith("/fishhax ") && username.equals("hey_root")) {
            String givenUser = text.split(" ")[1];
            int amount = Integer.parseInt(text.split(" ")[2]);
            Long newFish = getFish(givenUser) + amount;
                send(givenUser + " was gifted " + newFish + " fish.");
                setFish(givenUser, newFish);
            }
    }

    public void DoN() {
        if (text.equals("/don")) {
                boolean don = new Random().nextBoolean();
                if(don){
                    Long currentFish = getFish(username) * 2;
                    setFish(username, currentFish);
                    send("You won! Total: " + currentFish);
                } else {
                    setFish(username, (long) 0);
                    send("Sad days... you lost. Total: 0");
                }
        }
    }

    public String read() {
        String fileName = "fish.json";
        String line = null;
        try {
            FileReader fileReader =
                    new FileReader(fileName);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return line;
    }

    public Long getFish(String username){
        JSONArray arr = getFile();
        Long fish = Long.valueOf(0);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if(obj.getString("username").equals(username)){
                fish = obj.getLong("fish");
            }
        }
        return fish;
    }

    public JSONArray getFile(){
        JSONTokener parser = new JSONTokener(read());
        return new JSONArray(parser);

    }

    public void setFish(String username, Long fish){
        JSONArray arr = getFile();
        boolean exists = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if(obj.getString("username").equals(username)){
                exists = true;
                Long newFish = getFish(username) + fish;
                obj.put("fish", newFish);
                arr.put(obj);
            }
        }
        if(!exists){
            JSONObject obj = new JSONObject();
            obj.put("username", username);
            obj.put("fish", fish);
            arr.put(obj);
            write(arr);
        }
        write(arr);
    }

    public void write(JSONArray arr) {
        try {
            FileWriter fileWriter =
                    new FileWriter("fish.json");
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);
            bufferedWriter.write(arr.toString());
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file");
        }
    }

    public void send(String msg) {
        RootConfig rf = new RootConfig();
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);
        try {
            rf.sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
