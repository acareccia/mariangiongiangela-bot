package com.ilfalsodemetrio.bots;

import com.ilfalsodemetrio.api.OldPollingBot;
import com.ilfalsodemetrio.handlers.MariangelizeHandler;
import com.ilfalsodemetrio.handlers.WikipediaHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Created by lbrtz on 04/08/16.
 */
@Component
public class MariangiongiangelaBot extends OldPollingBot {
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

            if (hasKeyword(text,keywords.get("names"),keywords.get("wiki"))) {
                String term = message.getText().split(" ",3)[2];
                res = MariangelizeHandler.process(WikipediaHandler.process(term,"it",randomResponse(message,responses.get("wiki"))));
            }

            if (hasKeyword(text, keywords.get("names"),keywords.get("kicks")))
                res = kick(message,randomResponse(message,responses.get("kicks"),null));

            if (hasKeyword(text,keywords.get("names"),keywords.get("off"))) {
                setMute(true);
                return randomResponse(message,responses.get("off"));
            }

            if (hasKeyword(text,keywords.get("names"),keywords.get("on"))) {
                setMute(false);
                return randomResponse(message,responses.get("on"));
            }

            if (hasKeyword(text,keywords.get("greetings")))
                res = randomResponse(message,responses.get("greetings"),getUsers(message.getChat()));

            if (hasKeyword(text,keywords.get("names")))
                res =  randomResponse(message,responses.get("names"),getUsers(message.getChat()));

            // quiet
            if (isMute()) {
                return null;
            }
        }

        return res;
    }
}
