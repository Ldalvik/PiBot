package methods;

import org.telegram.telegrambots.api.objects.Update;
import setup.Helper;

public class Roll {

    private String message;
    private Helper h;

    public Roll(Update update, Helper h) {
        this.h = h;
        this.message = update.getMessage().getText();
    }

    public void start() {
        if (message.startsWith("/roll")) {
            if (h.canMembersKick()) {
                String random = String.valueOf((int) (Math.random() * (99999999 - 11111111) + 11111111));
                String[][] roll = new String[][]{
                        {"11111111", "1111111", "111111", "11111", "1111", "111", "11", "1"},
                        {"22222222", "2222222", "222222", "22222", "2222", "222", "22", "2"},
                        {"33333333", "3333333", "333333", "33333", "3333", "333", "33", "3"},
                        {"44444444", "4444444", "444444", "44444", "4444", "444", "44", "4"},
                        {"55555555", "5555555", "555555", "55555", "5555", "555", "55", "5"},
                        {"66666666", "6666666", "666666", "66666", "6666", "666", "66", "6"},
                        {"77777777", "7777777", "777777", "77777", "7777", "777", "77", "7"},
                        {"88888888", "8888888", "888888", "88888", "8888", "888", "88", "8"},
                        {"99999999", "9999999", "999999", "99999", "9999", "999", "99", "9"},
                        {"Octuples!", "Septuples!", "Sextuples!", "Quintiple!", "Quadruples!", "Triples!", "Doubles!", "Singles!"}
                };

                for (int column = 0; column <= 8; column++) {
                    for (int row = 0; row <= 7; row++) {
                        String number = roll[column][row];
                        System.out.println(random + ":" + number);
                        if (random.endsWith(number)) {
                            System.out.println(roll[9][row]);
                            h.send(random + "\n" + roll[9][row]);
                        }
                    }
                }
            }
        }
    }
}
