package com.ilfalsodemetrio.bots;

import com.ilfalsodemetrio.api.HeadlessBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Created by lbrtz on 15/09/16.
 */
@Component
public class GioforchioBot extends HeadlessBot {

    @Override
    public String botAI(Message message) {
        return "hey";
    }

}
