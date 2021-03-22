package de.main;


import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static de.main.Constants.STRINGSEPERATOR;
import static de.main.MessageIdentifier.LOGIN;

@ServerEndpoint("/api/v1/ws")
@ApplicationScoped


public class GameWebSocketHandler {

    Map<String, Session> sessions = new ConcurrentHashMap<>();
    int counter = 0;
    int currentSessionCounter;


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Websocket is Open!");
        System.out.println(session);
        currentSessionCounter = counter;
        counter++;
        sessions.put(String.valueOf(currentSessionCounter), session);

        //todo hier wird der player der sich verbinden will als neuer player geaddet (er ist noch nicht eingelogt!! Player --> user)

        }

    @OnClose
    public void onClose() {
        System.out.println("the websocket-connection is now closed!");
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println("An error occured when building connection from client.");
        //throwable.getStackTrace();
        //sessionArrayList.remove(session);

    }

    @OnMessage
    public void onMessage(String message) {
        //System.out.println(message);
        String[] dataArray = depackData(message);
        checkDataDir(dataArray);

    }

    private String[] depackData(String message){
        return message.split(STRINGSEPERATOR);
    }

    private void checkDataDir(String[] strings){
        for (MessageIdentifier messageIdentifier : MessageIdentifier.values()) {
            if(strings[0].equals(messageIdentifier.toString())){
                if(strings[0].equals(LOGIN.toString())){
                    Login login = new Login();
                    login.isLoginValid(strings, sessions.get(String.valueOf(currentSessionCounter)));
                }
            }
        }
    }

    public Map<String, Session> getSessions() {
        return sessions;
    }


    private void broadcast(String message) {
        System.out.println(message);
        /*
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
        */
    }
}
