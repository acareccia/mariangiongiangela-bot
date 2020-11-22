package com.ilfalsodemetrio.bots;


import com.ilfalsodemetrio.api.v2.Bot;
import com.ilfalsodemetrio.api.v2.beans.Message;

public class GioforchioBot implements Bot {

    public Message ai(Message in) {
        String res = null;

        if (in.hasText()) {
            String text = in.getText();

            if (text.toLowerCase().startsWith("ciao"))
                res = "Ciao "+ in.getFrom().getName();
        }

        return new Message(res, in.getFrom(), in.getTo());
    }
}
