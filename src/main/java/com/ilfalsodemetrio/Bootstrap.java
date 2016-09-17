package com.ilfalsodemetrio;

import com.ilfalsodemetrio.api.OldPollingBot;
import com.ilfalsodemetrio.bots.IlfalsodemetrioBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbrtz on 01/09/16.
 */
public class Bootstrap {
    private TelegramBotsApi telegramBotsApi;
    private List<OldPollingBot> botList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Bootstrap ...");

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.loadBot(new IlfalsodemetrioBot());
        //bootstrap.loadBot(new MariangiongiangelaBot());

        System.out.println("Bootstrap Running...");
    }

    public Bootstrap() {
        telegramBotsApi = new TelegramBotsApi();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Bootstrap Shutdown ...");
                for (OldPollingBot b : botList) {
                    b.shutdown();
                }
            }
        });
    }

    public void loadBot(OldPollingBot bot) {
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
