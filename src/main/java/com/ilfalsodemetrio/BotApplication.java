package com.ilfalsodemetrio;

import com.ilfalsodemetrio.bots.GioforchioBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.BotSession;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbrtz on 14/09/16.
 */
@SpringBootApplication
public class BotApplication implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(BotApplication.class);

    private TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    private List<BotSession> botSessions = new ArrayList<>();

    @Autowired
    private GioforchioBot gioforchioBot;


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("start");

        try {
            botSessions.add(telegramBotsApi.registerBot(gioforchioBot));
        } catch (TelegramApiException e) {
            log.error("Failed to register bot {} due to error {}: {}", gioforchioBot.getBotUsername(), e.getMessage(), e.getApiResponse());
        }

    }

    @PreDestroy
    public void stop() {
        gioforchioBot.kill();

        if (botSessions != null) {
            for (BotSession botSession : botSessions) {
                botSession.close();
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

}
