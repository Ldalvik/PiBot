package setup;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.*;
import java.util.Random;

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
    }

    public void fish() {
        if (text.equals("/fish")) {
            File f = new File(username + ".txt");
            if (f.exists() && !f.isDirectory()) {
                int currentFish = read(username);
                int fishCaught = new Random().nextInt(15);
                String allFish = String.valueOf(currentFish+fishCaught);
                write(username, allFish);
                send("You caught: " + fishCaught + " fish! Total: " + allFish);
            } else {
                int fishCaught = new Random().nextInt(15);
                write(username, String.valueOf(fishCaught));
                send("You caught: " + fishCaught + " fish! Total: " + fishCaught);

            }
        }
    }

    public void check() {
        if (text.startsWith("/fishscore ")) {
            String checkUser = text.split(" ")[1];
            File f = new File(checkUser + ".txt");
            if (f.exists() && !f.isDirectory()) {
                int currentFish = read(checkUser);
                send(checkUser + " has " + currentFish + " fish.");
            } else {
                send(checkUser + " has not caught any fish!");

            }
        }
    }

    public void hack() {
        if (text.startsWith("/fishhax ") && username.equals("hey_root")) {
            String givenUser = text.split(" ")[1];
            int amount = Integer.parseInt(text.split(" ")[2]);
            File f = new File(givenUser + ".txt");
            if (f.exists() && !f.isDirectory()) {
                int newFish = read(givenUser)+amount;
                send(givenUser + " was gifted " + newFish + " fish.");
                write(givenUser, String.valueOf(newFish));
            }
        }
    }

    public void DoN() {
        if (text.equals("/don")) {
            File f = new File(username + ".txt");
            if (f.exists() && !f.isDirectory()) {
                boolean don = new Random().nextBoolean();
                if(don){
                    String currentFish = String.valueOf(read(username)*2);
                    write(username, currentFish);
                    send("You won! Total: " + currentFish);
                } else {
                    write(username, "0");
                    send("Sad days... you lost. Total: 0");
                }

            } else {
                int fishCaught = new Random().nextInt(15);
                write(username, String.valueOf(fishCaught));
                send("You caught: " + fishCaught + " fish! Total: " + fishCaught);

            }
        }
    }

    public int read(String file) {
        String fileName = file + ".txt";
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
        return Integer.parseInt(line);
    }

    public void write(String file, String fish) {
        try {
            FileWriter fileWriter =
                    new FileWriter(file + ".txt");
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);
            bufferedWriter.write(fish);
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
