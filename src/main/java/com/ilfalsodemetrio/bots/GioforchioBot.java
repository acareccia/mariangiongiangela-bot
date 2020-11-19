package com.ilfalsodemetrio.bots;

import com.ilfalsodemetrio.api.HeadlessBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Created by lbrtz on 15/09/16.
 */
@Component
public class GioforchioBot extends HeadlessBot {


    @Override
    public String botAI(Message message) {
        String res = null;

        if (message.hasText()) {
            String text = message.getText();

            if (text.toLowerCase().startsWith("ciao"))
                res = "Ciao "+ message.getFrom().getFirstName();
        }

        return res;
    }

}
