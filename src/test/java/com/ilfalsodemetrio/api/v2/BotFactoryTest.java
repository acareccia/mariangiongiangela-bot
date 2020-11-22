package com.ilfalsodemetrio.api.v2;

import com.ilfalsodemetrio.api.v2.enums.Platform;
import com.ilfalsodemetrio.bots.GioforchioBot;
import junit.framework.TestCase;

public class BotFactoryTest extends TestCase {

    public void testCreateBot() {
        GioforchioBot gioforchioBot = new GioforchioBot();
        Session telegram = BotFactory.createBot(Platform.TELEGRAM,gioforchioBot);
        Session slack = BotFactory.createBot(Platform.SLACK,gioforchioBot);
        Session dummy = BotFactory.createBot(Platform.DUMMY,gioforchioBot);
    }
}
