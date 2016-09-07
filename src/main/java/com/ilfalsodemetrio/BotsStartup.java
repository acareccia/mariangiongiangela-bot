package com.ilfalsodemetrio;

import com.ilfalsodemetrio.bots.IlfalsodemetrioBot;
import com.ilfalsodemetrio.bots.MariangiongiangelaBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

/**
 * Created by lbrtz on 01/09/16.
 */
public class BotsStartup {
    public static void main(String[] args) {
        System.out.println("BotsStartup ...");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MariangiongiangelaBot());
            telegramBotsApi.registerBot(new IlfalsodemetrioBot());
        } catch (TelegramApiException e) {
            System.out.println("Bot crash: "+e.getMessage());
            e.printStackTrace();
        }

    }
}
