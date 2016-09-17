package com.ilfalsodemetrio.api;

import com.ilfalsodemetrio.entity.ChatUser;
import com.ilfalsodemetrio.utils.DatabaseManager;
import com.ilfalsodemetrio.utils.FileUtils;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by lbrtz on 01/09/16.
 */
public abstract class OldPollingBot extends HeadlessBot {
    private static String TELEGRAM_TOKENS = "tokens.properties";

    private Map randomCache = new HashMap();
    private Set<ChatUser> users = new TreeSet<ChatUser>();
    protected Map<String,List<String>> keywords = new HashMap<>();
    protected Map<String,List<String>> responses = new HashMap<>();
    private boolean mute = false;

    public abstract String botAI(Message message);

    public OldPollingBot() {
        log("startup ...");
        log("load props ...");
        Properties props = FileUtils.loadResource(getBotUsername()+".properties");

        for(Map.Entry<Object, Object> p : props.entrySet()) {
            String key = (String) p.getKey();
            List <String> values =  Arrays.asList(p.getValue().toString().split(","));

            if (key.endsWith("_keywords"))
                keywords.put(key.split("_",2)[0],values);
            else if (key.endsWith("_responses"))
                responses.put(key.split("_",2)[0],values);
        }
        startup();
    }

    public void startup() {
        log("load objects ...");
        try {
            //users = (Set<ChatUser>) FileUtils.loadObject(getBotUsername()+".out");
            users = (Set<ChatUser>) DatabaseManager.readObject(getBotUsername()+".users");
            mute = DatabaseManager.read(getBotUsername()+".quiet").equals("yes");

        } catch (Exception e) {
            log("error :"+e);
        }
        log("users: "+users);
        log("mute: "+mute);
    }

    public void shutdown() {
        log("save objects ...");
        try {
            //FileUtils.persistObject(getBotUsername()+".out",users);
            DatabaseManager.writeObject(getBotUsername()+".users",users);
            DatabaseManager.write(getBotUsername()+".quiet","no");
        } catch (Exception e) {
            log("error :"+e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() || update.hasEditedMessage()){
            Message message;
            if (update.hasMessage())
                message = update.getMessage();
            else
                message = update.getEditedMessage();

            updateUsers(message);

            String response = botAI(message);

            if(response != null) {
                say(message.getChatId(),response);
            }
        }
    }

    @Override
    public String getBotToken() {
        final Properties properties = FileUtils.loadResource(TELEGRAM_TOKENS);
        String token_prop = properties.getProperty(getBotUsername());
        if (token_prop.startsWith("$"))
            return System.getenv(token_prop.substring(1));
        return token_prop;
    }

    protected String notSoRandom(List list) {
        String r = list.get(new Random().nextInt(list.size())).toString();

//        if (r.equals(randomCache.get(list))) {
//            return notSoRandom(list);
//        }

        //randomCache.put(list,r);
        return r;
    }

    protected boolean hasKeyword(String text, List<String>...liste) {
        text = text.toLowerCase().trim();
        boolean found=false;

        for (List lista: liste) {
            found=false;
            for (Object s : lista) {
                if (text.contains(s.toString())) found=true;
            }
            if (!found) return false;
        }
        return found;
    }

    protected String getKeyword(String text, List<String>...liste) {
        text = text.toLowerCase().trim();
        boolean found=false;
        String argument = null;

        for (List lista: liste) {
            found=false;
            for (Object s : lista) {
                if (text.contains(s.toString())) {
                    argument = text.substring(text.indexOf(s.toString())+s.toString().length());
                    found = true;
                }
            }
            if (!found) return null;
        }
        return argument;
    }

    protected boolean hasKeyword(String text, String[] array) {
        return hasKeyword(text,Arrays.asList(array));
    }

    private void updateUsers(Message message) {
        if (message.getNewChatMember()!= null)
            users.add(new ChatUser(message.getChat(),message.getNewChatMember()));
        else if (message.getLeftChatMember() != null)
            users.remove(new ChatUser(message.getChat(),message.getLeftChatMember()));
        else
            users.add(new ChatUser(message.getChat(),message.getFrom()));
    }

    // response Handlers

    /**
     * randomResponse
     *
     * @param message input message
     * @param list list of responsesg
     * @param users list of chat users
     * @return
     */
    protected String randomResponse(Message message, List<String> list,Collection<ChatUser> users) {
        String rndUser = "Coso";

        if (users != null) {
            List<ChatUser> chatUsers = new ArrayList<ChatUser>(users);
            rndUser = chatUsers.get(new Random().nextInt(users.size())).getUser().getFirstName();
        }

        String fmtRes = MessageFormat.format(notSoRandom(list), message.getFrom().getFirstName(),rndUser);

        return fmtRes;
    }

    protected String randomResponse(Message message,List<String> list) {
        return randomResponse(message,list,null);
    }

    /**
     * Kick
     *
     * @param message
     * @param text
     * @return
     */
    protected String kick(Message message, String text) {
        KickChatMember kickChatMember = new KickChatMember();
        kickChatMember.setChatId(message.getChatId().toString());
        kickChatMember.setUserId(message.getFrom().getId());

        try {
            this.kickMember(kickChatMember);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return text;
    }

    /**
     * Simple logger
     *
     * @param message
     */
    protected void log(String message) {
        System.out.println(getBotUsername() + " - " + message);
    }

    protected Set<ChatUser> getUsers(Chat chat) {
        //fixme: filter
        return users;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
