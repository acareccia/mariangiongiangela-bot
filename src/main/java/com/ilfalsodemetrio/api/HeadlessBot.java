package com.ilfalsodemetrio.api;

import com.ilfalsodemetrio.entity.ChatUser;
import com.ilfalsodemetrio.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by lbrtz on 15/09/16.
 */
@RestController
public abstract class HeadlessBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(HeadlessBot.class);

    @Autowired
    private Environment env;

    private Set<ChatUser> users = new TreeSet<ChatUser>();


//    public HeadlessBot() {
//        log.info("load props "+ getBotUsername());
//        Properties props = FileUtils.loadResource(getBotUsername()+".properties");
//    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() || update.hasEditedMessage()){
            Message message;
            if (update.hasMessage()) {
                message = update.getMessage();
            } else {
                message = update.getEditedMessage();
            }

            updateUsers(message);

            String response = botAI(message);

            if(response != null) {
                say(message.getChatId(),response);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getBotToken() {
        log.debug("get token: " + env.getProperty(getBotUsername()+".token"));
        return env.getProperty(getBotUsername()+".token");
        //return System.getenv(getBotUsername().toUpperCase()+"_TOKEN");
    }

    public Message say(String id, String text) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(id);
        sendMessageRequest.setText(text);
        try {
            return sendApiMethod(sendMessageRequest);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException e:"+e.getMessage());
        }
        return null;
    }

    public Message say(Long id, String text) {
        return say(id.toString(),text);
    }

    public Message sayToAll(String text) {
        return null;
    }

    public void kill() {
        sayToAll("addio mondo crudele");
    }

    protected Set<ChatUser> getUsers(Chat chat) {
        //fixme: filter
        return users;
    }

    private void updateUsers(Message message) {
        if (message.getNewChatMembers()!= null && message.getNewChatMembers().size() > 0) {
            users.add(new ChatUser(message.getChat(), message.getNewChatMembers().get(0)));
        } else if (message.getLeftChatMember() != null) {
            users.remove(new ChatUser(message.getChat(), message.getLeftChatMember()));
        } else {
            users.add(new ChatUser(message.getChat(), message.getFrom()));
        }
    }

    public abstract String botAI(Message message);
}
