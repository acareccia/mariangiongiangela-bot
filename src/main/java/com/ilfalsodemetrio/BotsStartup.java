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

        Bot ilfalsodemetrioBot = new IlfalsodemetrioBot();
        try {
            telegramBotsApi.registerBot(ilfalsodemetrioBot);
        } catch (TelegramApiException e) {
            System.out.println("BotsStartup crash: "+e.getMessage());
        }

        Bot mariangiongiangelaBot = new MariangiongiangelaBot();
        try {
            telegramBotsApi.registerBot(mariangiongiangelaBot);
        } catch (TelegramApiException e) {
            System.out.println("BotsStartup crash: "+e.getMessage());
        }

        System.out.println("BotsStartup Running ...");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("BotsStartup Shutdown ...");
                ilfalsodemetrioBot.shutdown();
                mariangiongiangelaBot.shutdown();
            }
        });
    }
}
