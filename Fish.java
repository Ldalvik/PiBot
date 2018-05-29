package setup;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("fish.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        Long fish = Long.valueOf(0);
        if(jsonObject.get(username)!=null){
            fish = (Long) jsonObject.get(username);
        }
        return fish;
    }

    public JSONObject getFile(){
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("fish.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (JSONObject) obj;

    }

    public void setFish(String username, Long fish){
        JSONObject obj = getFile();
        Long newFish = getFish(username) + fish;
        obj.put(username, newFish);
        write(obj);
    }

    public void write(JSONObject obj) {
        try {
            FileWriter fileWriter =
                    new FileWriter("fish.json");
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);
            bufferedWriter.write(obj.toJSONString());
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
