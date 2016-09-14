package com.ilfalsodemetrio.bots;

import com.ilfalsodemetrio.Bot;
import com.ilfalsodemetrio.DatabaseManager;
import com.ilfalsodemetrio.dai.MariangelizeHandler;
import com.ilfalsodemetrio.dai.WikipediaHandler;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Created by lbrtz on 01/09/16.
 */
public class IlfalsodemetrioBot extends Bot {
    private static String BOT_NAME = "ilfalsodemetrioBot";
    private static String INFO_COMMAND = "/info";
    private static String HELP_COMMAND = "/aiuto";
    private static String HELP_COMMAND_TEXT = "*TODO*";


    @Override
    public String botAI(Message message) {
        String res = null;

        if (message.hasText()) {
            String text = message.getText();

            // commands
            if (text.startsWith(INFO_COMMAND))
                return getBotUsername()+" is live with "+getUsers(message.getChat());

            if (text.startsWith(HELP_COMMAND))
                return HELP_COMMAND_TEXT;


            // keywords

            if (hasKeyword(text,keywords.get("names"),keywords.get("find"))) {
                res = WikipediaHandler.process(getKeyword(text,keywords.get("find")),"en",randomResponse(message,responses.get("find")));
            }

            if (hasKeyword(text,keywords.get("names"),keywords.get("off"))) {
                setMute(true);
                return randomResponse(message,responses.get("off"));
            }

            if (hasKeyword(text,keywords.get("names"),keywords.get("on"))) {
                setMute(false);
                return  randomResponse(message,responses.get("on"));
            }

            if (hasKeyword(text,keywords.get("names")))
                res = randomResponse(message,responses.get("names"),getUsers(message.getChat()));

            // quiet
            if (isMute()) {
                return null;
            }
        }
        return res;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

}
