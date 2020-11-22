package com.ilfalsodemetrio.api.v2;

import com.ilfalsodemetrio.BotApplication;
import com.ilfalsodemetrio.api.v2.drivers.TelegramBot;
import com.ilfalsodemetrio.api.v2.enums.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotFactory {
    private static final Logger log = LoggerFactory.getLogger(BotFactory.class);

    private Platform platform;

    private BotFactory() {}

    public static Session createBot(Platform platform, Bot bot) {
        log.info("createBot {} {}",platform, bot );
        TelegramBot b = new TelegramBot();
        return b.getSession();
    }
}
