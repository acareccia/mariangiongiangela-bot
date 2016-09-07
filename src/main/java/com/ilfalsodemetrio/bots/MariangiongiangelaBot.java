package com.ilfalsodemetrio.bots;

import com.ilfalsodemetrio.Bot;
import org.telegram.telegrambots.api.objects.Message;

import java.util.*;

/**
 * Created by lbrtz on 04/08/16.
 */
public class MariangiongiangelaBot extends Bot {
    private static String BOT_NAME = "mariangiongiangelaBot";
    private static String INFO_COMMAND = "/info";
    private static String HELP_COMMAND = "/aiuto";
    private static String HELP_COMMAND_TEXT = "*TODO*";

    @Override
    public String botAI(Message message) {
        if (message.hasText()) {
            String text = message.getText();

            if (text.startsWith(INFO_COMMAND))
                return getBotUsername()+" is live with "+getUsers(message.getChat());

            if (text.startsWith(HELP_COMMAND))
                return HELP_COMMAND_TEXT;

            if (hasKeyword(text,keywords.get("names"),keywords.get("wiki"))) {
                //fixme:
                String term = message.getText().split(" ",3)[2];
                return mariangelize(searchWiki(term,"it"));
            }

            if (hasKeyword(text, keywords.get("names"),keywords.get("kicks")))
                return kick(message,randomResponse(message,responses.get("kicks"),null));

            if (hasKeyword(text,keywords.get("greetings")))
                return randomResponse(message,responses.get("greetings"),getUsers(message.getChat()));

            if (hasKeyword(text,keywords.get("names")))
                return randomResponse(message,responses.get("names"),getUsers(message.getChat()));

        }

        return null;
    }

    protected String mariangelize(String text) {
        StringTokenizer st = new StringTokenizer(text);
        StringBuilder stringBuilder = new StringBuilder();
        boolean doppio = false;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (token.length() >= 6 && !doppio) {
                if (token.endsWith("a")) {
                    stringBuilder.append("cosa");
                    doppio = true;
                } else if (token.endsWith("o")) {
                    stringBuilder.append("coso");
                    doppio = true;
                } else if (token.endsWith("e")) {
                    stringBuilder.append("cose");
                    doppio = true;
                } else if (token.endsWith("i")) {
                    stringBuilder.append("cosi");
                    doppio = true;
                } else {
                    stringBuilder.append(token);
                    doppio = false;
                }
            } else {
                stringBuilder.append(token);
                doppio = false;
            }

            stringBuilder.append(" ");
        }


        return stringBuilder.toString();
    }


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

}
