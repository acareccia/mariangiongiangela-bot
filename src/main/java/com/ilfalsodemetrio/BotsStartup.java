package com.ilfalsodemetrio;

import com.ilfalsodemetrio.bots.IlfalsodemetrioBot;
import com.ilfalsodemetrio.bots.MariangiongiangelaBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbrtz on 01/09/16.
 */
public class BotsStartup {
    private TelegramBotsApi telegramBotsApi;
    private List<Bot> botList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("BotsStartup ...");

        BotsStartup botsStartup = new BotsStartup();
        botsStartup.loadBot(new IlfalsodemetrioBot());
        botsStartup.loadBot(new MariangiongiangelaBot());

        System.out.println("BotsStartup Running...");
    }

    public BotsStartup() {
        telegramBotsApi = new TelegramBotsApi();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("BotsStartup Shutdown ...");
                for (Bot b : botList) {
                    b.shutdown();
                }
            }
        });
    }

    public void loadBot(Bot bot) {
        try {
            if (bot.getBotToken() != null) {
                telegramBotsApi.registerBot(bot);
                botList.add(bot);
            } else
                System.out.println(bot.getBotUsername()+ " disabled");
        } catch (TelegramApiException e) {
            System.out.println(bot.getBotUsername()+" crash: "+e.getMessage());
        }

    }
}
