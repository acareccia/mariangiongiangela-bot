package com.ilfalsodemetrio.api.v2.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String text;
    private User from;
    private User to;

    public Message(String text, User from, User to) {
        this.text = text;
        this.from = from;
        this.to = to;
    }

    public boolean hasText() {
        return !text.isEmpty();
    }
}
