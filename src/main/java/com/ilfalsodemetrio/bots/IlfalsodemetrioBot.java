package com.ilfalsodemetrio.bots;

import com.ilfalsodemetrio.Bot;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Created by lbrtz on 01/09/16.
 */
public class IlfalsodemetrioBot extends Bot {
    private static String BOT_NAME = "ilfalsodemetrioBot";
    private static String INFO_COMMAND = "/info";
    private static String HELP_COMMAND = "/aiuto";
    private static String HELP_COMMAND_TEXT = "*sob*";


    @Override
    public String botAI(Message message) {

        if (message.hasText()) {
            String text = message.getText();

            if (text.startsWith(INFO_COMMAND))
                return getBotUsername()+" is live with "+getUsers(message.getChat());

            if (text.startsWith(HELP_COMMAND))
                return HELP_COMMAND_TEXT;

            if (hasKeyword(text,keywords.get("names")))
                return randomResponse(message,responses.get("names"),getUsers(message.getChat()));

        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

}
