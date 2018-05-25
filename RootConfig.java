package setup;

import methods.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Set;

/**
 * Created by subar on 5/30/2017.
 */

public class RootConfig extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        if(update.getMessage().getText().toLowerCase().endsWith("it")){
            String ittled = update.getMessage().getText()+"tle";
            SendMessage msg = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(ittled);
            try {
                sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        
        /* ROOTS CODE THY DOES START HERE NOW??!!1!?*/
        if(!update.getMessage().isReply()) {
            Settings settings = new Settings(update, new Helper(update));
            settings.start();
            Helper h = new Helper(update, settings);
            Ping p = new Ping(update, h);
            SummonAdmins sa = new SummonAdmins(update, h);
            Kick k = new Kick(update, h);
            Score s = new Score(update, h);
            Roll r = new Roll(update, h);
            CustomCommands cc = new CustomCommands(update, h);
            cc.start();
            p.start();
            sa.start();
            k.start();
            s.start();
            r.start();
        }
    }

    @Override
    public String getBotUsername() {
        return "bot_username";
    }

    @Override
    public String getBotToken() {
        return "bot:token";
    }
}










