package setup;

import org.telegram.telegrambots.api.objects.Update;

import java.io.*;
import java.util.Random;

public class Type {

    private String message;
    private String chatWord;
    private String username;
    private Long chatId;
    private Long date;
    private String[] words = {"car", "loser", "creative", "delta", "bittle"};

    public Type(Update update) {
        this.username = update.getMessage().getFrom().getUserName();
        this.message = update.getMessage().getText();
        this.chatId = update.getMessage().getChatId();
    }

    public void start() {
        if (message.equals("/type")) {
            if (!read().equals(":")) {
                Helper.send(chatId, "Theres still a game in progress!");
            } else {
                int random = new Random().nextInt(words.length);
                String word = words[random];
                Helper.send(chatId, "Type: " + word);
                write(word, String.valueOf(System.currentTimeMillis()));
            }
        }
        if(message.equals("/reset") && (username.equals("hey_root") || username.equals("terrybawk"))){
            Helper.send(chatId, "Type game reset!");
            write("", "");
        } else if(message.equals("/reset") && (!username.equals("hey_root") || !username.equals("terrybawk"))){
            Helper.send(chatId, "Only admins can reset the game!");
        }
        if (read().split(":")[0] != null) {
            if (message.equals(read().split(":")[0])) {
                Long reaction = System.currentTimeMillis() - Long.valueOf(read().split(":")[1]);
                Helper.send(chatId, username + " won! " + reaction + "ms");
                write("", "");
            }
        }
    }

    public String read() {
        String fileName = chatId + ".txt";
        String line = null;
        try {
            FileReader fileReader =
                    new FileReader(fileName);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return line;
    }

    public void write(String word, String date) {
        try {
            FileWriter fileWriter =
                    new FileWriter(chatId + ".txt");
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);
            bufferedWriter.write(word + ":" + date);
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file");
        }
    }
}
