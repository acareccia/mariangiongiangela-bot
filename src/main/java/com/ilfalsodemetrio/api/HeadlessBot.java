package com.ilfalsodemetrio.api;

import com.ilfalsodemetrio.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.Properties;

/**
 * Created by lbrtz on 15/09/16.
 */
@RestController
public abstract class HeadlessBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(HeadlessBot.class);

//    public HeadlessBot() {
//        log.info("load props "+ getBotUsername());
//        Properties props = FileUtils.loadResource(getBotUsername()+".properties");
//    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() || update.hasEditedMessage()){
            Message message;
            if (update.hasMessage())
                message = update.getMessage();
            else
                message = update.getEditedMessage();

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
        return System.getenv(getBotUsername().toUpperCase()+"_TOKEN");
    }

    public Message say(String id, String text) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(id);
        sendMessageRequest.setText(text);
        try {
            return sendMessage(sendMessageRequest);
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


    @RequestMapping("/")
    public String test() {
        return "hello from" +getBotUsername();
    }

    public abstract String botAI(Message message);

}
