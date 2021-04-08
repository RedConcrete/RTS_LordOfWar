package de.communication;


import de.constants.MessageIdentifier;
import de.model.ServerLobby;
import de.model.User;
import de.processes.Login;
import de.processes.Register;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static de.constants.Constants.STRINGSEPERATOR;
import static de.constants.MessageIdentifier.*;

@ServerEndpoint("/api/v1/ws")
@ApplicationScoped


public class GameWebSocketHandler {

    Map<String, Session> sessions = new ConcurrentHashMap<>();
    Map<String, User> userSessions = new ConcurrentHashMap<>();
    Map<String, ServerLobby> lobbys = new ConcurrentHashMap<>();

    private DataPacker dataPacker = new DataPacker();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Websocket is Open!");
        System.out.println(session);
        sessions.put(session.getId(), session);
        session.getAsyncRemote().sendObject(dataPacker.packData(CONNECTION, session.getId()));
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

    private String[] depackData(String message) {
        return message.split(STRINGSEPERATOR);
    }

    private void checkDataDir(String[] strings) {
        //um... with multiple players connected this will only work for the last one to connect right?
        // bc the currentSessionCounter is the value used?
        //send session id on connect to identify? send username with every query and check if said username is currently logged in? idk
        //TODO change data processing (see dataSendConvention.txt)
        //TODO change all methods associated with data processing
        for (MessageIdentifier messageIdentifier : MessageIdentifier.values()) {
            if (strings[0].equals(messageIdentifier.toString())) {
                if (strings[0].equals(LOGIN.toString())) {
                    Login login = new Login();
                    User newUser = login.isLoginValid(strings, sessions.get(strings[1]));
                    loginUser(newUser);
                } else if (strings[0].equals(REGISTER.toString())) {
                    Register register = new Register();
                    register.isRegisterValid(strings, sessions.get(strings[1]));
                } else if (strings[0].equals(GET_GAME_POINTS.toString())) {
                    ServerLobby serverLobby=findLobby(strings);
                    if (serverLobby!=null){
                        sessions.get(strings[1]).getAsyncRemote().sendObject(dataPacker.packData(GET_GAME_POINTS, String.valueOf(serverLobby.getGame().getPoints(strings[1]))));
                    }
                } else if (strings[0].equals(CREATE_LOBBY.toString())) {
                    createLobby(strings);
                }
            }
        }
    }

    private void createLobby(String[] args) {
        //TODO where to put this?
        if (userSessions.containsKey(args[1])) {//user is logged in
            if (!lobbys.containsKey(args[2])) {//lobby doesnt exist
                ServerLobby newLobby = new ServerLobby(new User[]{userSessions.get(args[1])});
                lobbys.put(args[2], newLobby);
                ArrayList<String> data=new ArrayList<>();
                data.add("true");
                data.add(args[2]);
                sessions.get(args[1]).getAsyncRemote().sendObject(dataPacker.packData(CREATE_LOBBY, dataPacker.stringCombiner(data)));
            }
        }
        sessions.get(args[1]).getAsyncRemote().sendObject(dataPacker.packData(CREATE_LOBBY, "false"));
    }

    private ServerLobby findLobby(String[] args){
        return lobbys.getOrDefault(args[2], null);
    }


    private void loginUser(User user) {
        //TODO is this the most appropiate place for this?
        if (user != null && user.getuSession() != null) {
            String id = user.getuSession().getId();
            if (sessions.containsKey(id)) {
                userSessions.put(id, user);
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
