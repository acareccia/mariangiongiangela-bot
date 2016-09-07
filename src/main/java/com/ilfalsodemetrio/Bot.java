package com.ilfalsodemetrio;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by lbrtz on 01/09/16.
 */
public abstract class Bot extends TelegramLongPollingBot {
    private static String TELEGRAM_TOKENS = "tokens.properties";

    private Map randomCache = new HashMap();
    private Set<ChatUser> users = new TreeSet<ChatUser>();
    protected Map<String,List<String>> keywords = new HashMap<>();
    protected Map<String,List<String>> responses = new HashMap<>();

    public abstract String botAI(Message message);

    public Bot() {
        log("startup ...");
        Properties props = loadResource(getBotUsername()+".properties");

        for(Map.Entry<Object, Object> p : props.entrySet()) {
            String key = (String) p.getKey();
            List <String> values =  Arrays.asList(p.getValue().toString().split(","));

            if (key.endsWith("_keywords"))
                keywords.put(key.split("_",2)[0],values);
            else if (key.endsWith("_responses"))
                responses.put(key.split("_",2)[0],values);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() || update.hasEditedMessage()){
            Message message;
            if (update.hasMessage())
                message = update.getMessage();
            else
                message = update.getEditedMessage();

            if (message.getNewChatMember()!= null)
                users.add(new ChatUser(message.getChat(),message.getNewChatMember()));
            else if (message.getLeftChatMember() != null)
                users.remove(new ChatUser(message.getChat(),message.getLeftChatMember()));
             else
                 users.add(new ChatUser(message.getChat(),message.getFrom()));

            String response = botAI(message);

            if(response != null) {
                sendMsg(message.getChatId(),response);
            }
        }
    }

    @Override
    public String getBotToken() {
        final Properties properties = loadResource(TELEGRAM_TOKENS);
        String token_prop = properties.getProperty(getBotUsername());
        if (token_prop.startsWith("$"))
            return System.getenv(token_prop.substring(1));
        return token_prop;
    }

    public Message sendMsg(String id, String text) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(id);
        sendMessageRequest.setText(text);
        try {
            return sendMessage(sendMessageRequest);
        } catch (TelegramApiException e) {
            log("TelegramApiException e:"+e.getMessage());
        }
        return null;
    }

    public Message sendMsg(Long id, String text) {
        return sendMsg(id.toString(),text);
    }

    protected Properties loadResource(String res) {
        final Properties properties = new Properties();
        try (final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(res)) {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
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

    protected boolean hasKeyword(String text, String[] array) {
        return hasKeyword(text,Arrays.asList(array));
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
        List<ChatUser> chatUsers = new ArrayList<ChatUser>(users);

        String rndUser = "Coso";

        if (users != null) {
            rndUser = chatUsers.get(new Random().nextInt(users.size())).getUser().getFirstName();
        }

        String fmtRes = MessageFormat.format(notSoRandom(list), message.getFrom().getFirstName(),rndUser);

        return fmtRes;
    }

    /**
     * Search wikipedia
     *
     * @param term
     * @param lang
     * @return
     */
    protected String searchWiki(String term, String lang) {
        final String ENDPOINT = "https://"+lang+".wikipedia.org/w/api.php";
        log("search :"+term);

        String output = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            String param = URLEncoder.encode(term.trim().toLowerCase(), "utf-8");
            HttpPost request = new HttpPost(ENDPOINT+"?action=opensearch&limit=1&search="+param);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(entity);
            String responseText = EntityUtils.toString(buf);
            JSONArray jsonArray = new JSONArray(responseText);
            //fixme: empty array
            output = jsonArray.getJSONArray(2).get(0).toString();
            if (output.isEmpty())
                return output;
            log("search res:"+output);
        }catch (Exception ex) {
            log("wiki search error: "+ex);
            ex.printStackTrace();
        }
        return output;
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

    public Set<ChatUser> getUsers(Chat chat) {
        //fixme: filter
        return users;
    }
}
