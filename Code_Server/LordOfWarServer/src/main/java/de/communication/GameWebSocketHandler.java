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

        sessions.put(session.getId(), session);
        session.getAsyncRemote().sendObject(dataPacker.packData(CONNECTION, session.getId()));
        //todo hier wird der player der sich verbinden will als neuer player geaddet (er ist noch nicht eingelogt!! Player --> user)

    }

    @OnClose
    public void onClose() {

    }

    @OnError
    public void onError(Throwable throwable) {


    }

    @OnMessage
    public void onMessage(String message) {

        String[] dataArray = depackData(message);
        checkDataDir(dataArray);

    }

    private String[] depackData(String message) {
        return message.split(STRINGSEPERATOR);
    }

    private void checkDataDir(String[] data) {
        //um... with multiple players connected this will only work for the last one to connect right?
        // bc the currentSessionCounter is the value used?
        //send session id on connect to identify? send username with every query and check if said username is currently logged in? idk
        //TODO change data processing (see dataSendConvention.txt)
        //TODO change all methods associated with data processing
        for (MessageIdentifier messageIdentifier : MessageIdentifier.values()) {
            if (data[0].equals(messageIdentifier.toString())) {
                if (data[0].equals(LOGIN.toString())) {
                    Login login = new Login();
                    User newUser = login.isLoginValid(data, sessions.get(data[1]));
                    loginUser(newUser);
                } else if (data[0].equals(REGISTER.toString())) {
                    Register register = new Register();
                    register.isRegisterValid(data, sessions.get(data[1]));
                } else if (data[0].equals(GET_GAME_POINTS.toString())) {
                    ServerLobby serverLobby = findLobby(data);
                    if (serverLobby != null) {
                        sessions.get(data[1]).getAsyncRemote().sendObject(dataPacker.packData(GET_GAME_POINTS, String.valueOf(serverLobby.getGame().getPoints(data[1]))));
                    }
                } else if (data[0].equals(CREATE_LOBBY.toString())) {
                    createLobby(data);

                } else if (data[0].equals(GET_LOBBYS.toString())) {
                    sendLobbysToClient(data[1]);
                }else if (data[0].equals(JOIN_LOBBY.toString())){
                    joinLobby(data);
                }else if (data[0].equals(LEAVE_LOBBY.toString())){
                    leaveLobby(data);
                }
            }
        }
    }

    private void sendLobbysToClient(String userID) {
        ArrayList<String> arr = new ArrayList<>();
        for (Map.Entry<String, ServerLobby> entry : lobbys.entrySet()) {
            if (entry.getValue().getGame() == null) {
                arr.add(entry.getKey());
                arr.add(entry.getValue().getLobbyMap());
                arr.add(entry.getValue().getGamemode());
                arr.add(String.valueOf(entry.getValue().getPlayerAmount()));
            }
        }

        sessions.get(userID).getAsyncRemote().sendObject(dataPacker.packData(GET_LOBBYS, dataPacker.stringCombiner(arr)));
        System.out.println(arr.toString());
    }

    private void leaveLobby(String[] data){
        ServerLobby lobby=findLobby(data);
        if (lobby!=null){
            boolean leave= lobby.leaveLobby(userSessions.get(data[1]));
        if (leave){
            if (lobby.getAdmin()==null){
                lobbys.remove(lobby.getLobbyName());
            }
        }
        }
    }

    private void joinLobby(String[] data) {
        boolean joined = false;
        ServerLobby lobby = findLobby(data);
        if (lobby != null) {
            if (lobby.getGame() == null) {
                joined = lobby.joinLobby(userSessions.get(data[1]));
                if (joined) {
                    ArrayList<String> lobbyDataToSend = new ArrayList<>();
                    lobbyDataToSend.add("true");
                    lobbyDataToSend.add(lobby.getLobbyName());
                    sessions.get(data[1]).getAsyncRemote().sendObject(dataPacker.packData(JOIN_LOBBY, dataPacker.stringCombiner(lobbyDataToSend)));
                    System.out.println(joined);
                }
            }
        }
        if (!joined) {
            sessions.get(data[1]).getAsyncRemote().sendObject(dataPacker.packData(JOIN_LOBBY, "false"));
        }
        //TODO send joined success back
    }

    private void createLobby(String[] data) {
        //TODO where to put this?
        if (userSessions.containsKey(data[1])) {
            if (!lobbys.containsKey(data[2])) {
                User[] users = new User[Integer.parseInt(data[4])];
                users[0] = userSessions.get(data[1]);
                ServerLobby newLobby = new ServerLobby(users, data[2], data[3], users.length, data[5]);
                lobbys.put(data[2], newLobby);

                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("true");
                dataList.add(data[2]);
                sessions.get(data[1]).getAsyncRemote().sendObject(dataPacker.packData(CREATE_LOBBY, dataPacker.stringCombiner(dataList)));
            }
        }
        sessions.get(data[1]).getAsyncRemote().sendObject(dataPacker.packData(CREATE_LOBBY, "false"));
    }

    private ServerLobby findLobby(String[] args) {
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

    }
}
