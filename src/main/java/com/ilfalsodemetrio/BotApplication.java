package com.ilfalsodemetrio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BotApplication implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(BotApplication.class);

//    private final List<BotSession> bots = new ArrayList<>();
//    @Autowired
//    private GioforchioBot gioforchioBot;
//
//    @Autowired
//    private MariangiongiangelaBot mariangiongiangelaBot;

    @Override
    public void run(ApplicationArguments applicationArguments) {

//        try {
//            log.info("register {}",gioforchioBot.getBotUsername());
//            bots.add(new TelegramBotsApi(DefaultBotSession.class).registerBot(gioforchioBot));
//            //log.info("register {}",mariangiongiangelaBot.getBotUsername());
//            //bots.add(new TelegramBotsApi(DefaultBotSession.class).registerBot(mariangiongiangelaBot));
//        } catch (TelegramApiException e) {
//            log.error("Failed to register bot {} ", e.getMessage());
//        }
    }
//
//    @PreDestroy
//    public void stop() {
//        for (BotSession botSession : bots) {
//            log.info("stop {} ",botSession);
//            botSession.stop();
//        }
//    }

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

}
