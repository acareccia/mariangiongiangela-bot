package com.ilfalsodemetrio.entity;


import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.Serializable;

/**
 * Created by lbrtz on 07/09/16.
 */
public class ChatUser implements Comparable,Serializable {

    private Chat chat;
    private User user;
    private boolean active;

    public ChatUser(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return user.getFirstName();
    }

    @Override
    public int compareTo(Object o) {
        return ((ChatUser) o).user.getId().compareTo(user.getId());
    }
}
