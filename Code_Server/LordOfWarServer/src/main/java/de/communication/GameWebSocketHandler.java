package de.communication;


import de.model.ServerGame;
import de.model.ServerLobby;
import de.model.User;
import de.processes.DataManager;
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

/**
 * Handles all direct communication with the client.
 *
 * @author Franz Klose,Cem Arslan
 */
@ServerEndpoint("/api/v1/ws")
@ApplicationScoped
public class GameWebSocketHandler {

    private Map<String, Session> sessions = new ConcurrentHashMap<>();//connected sessions (sessionid,Session)
    private Map<String, User> userSessions = new ConcurrentHashMap<>();//logged in users (sessionId,User object)
    private Map<String, ServerLobby> lobbys = new ConcurrentHashMap<>();//current lobbys (sessionID, lobby)
    private DataManager dataManager = new DataManager();

    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        session.getAsyncRemote().sendText(DataPacker.packData(CONNECTION, session.getId()));
        //todo hier wird der player der sich verbinden will als neuer player geaddet (er ist noch nicht eingelogt!! Player --> user)

    }

    @OnError
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message) {
        String[] dataArray = depackData(message);
        checkDataDir(dataArray);
    }

    @OnClose
    public void onClose(Session session,
                        CloseReason closeReason) {
        System.out.println(session.getId());
        System.out.println(closeReason.toString());
        //TODO remove closed ppl
    }

    private String[] depackData(String message) {
        return message.split(STRINGSEPERATOR);
    }


    /**
     * Decision tree based on  {@link de.constants.MessageIdentifier}
     *
     * @param data received String
     */
    private void checkDataDir(String[] data) {
        if (data[0].equals(LOGIN.toString())) {
            User newUser = new Login(dataManager).isLoginValid(data, sessions.get(data[1]));
            loginUser(newUser);
        } else if (data[0].equals(REGISTER.toString())) {
            Register register = new Register(dataManager);
            register.isRegisterValid(data, sessions.get(data[1]));
        } else if (data[0].equals(GET_GAME_POINTS.toString())) {
            ServerLobby serverLobby = findLobby(data);
            if (serverLobby != null) {
                sessions.get(data[1]).getAsyncRemote().sendText(DataPacker.packData(GET_GAME_POINTS, String.valueOf(serverLobby.getGame().getPoints(data[1]))));
            }
        } else if (data[0].equals(CREATE_LOBBY.toString())) {
            createLobby(data);
        } else if (data[0].equals(GAME_START.toString())) {
            startGame(data);
        } else if (data[0].equals(GET_LOBBYS.toString())) {
            sendLobbysToClient(data[1]);
        } else if (data[0].equals(JOIN_LOBBY.toString())) {
            joinLobby(data);
        } else if (data[0].equals(LEAVE_LOBBY.toString())) {
            leaveLobby(data);
        } else if (data[0].equals(LOBBY_PLAYERS.toString())) {
            sendPlayerListUpdate(data);
        } else if (data[0].equals(UPDATE_SOLDIER_POS.toString())) {
            genSoldierPosArray(data);
        } else if (data[0].equals(UPDATE_CASTLE_POS.toString())) {
            genCastlePosArray(data);
        } else if (data[0].equals(ATTACK_UNIT_UPDATE.toString())) {
            attackUnitUpdate(data);
        }
    }

    /**
     * Starts a {@link ServerGame} for the given lobby.
     *
     * @param data received data
     */
    private void startGame(String[] data) {
        ServerLobby lobby = lobbys.get(data[2]);
        if (lobby.getGame() == null) {
            if (lobby.getAdmin() == userSessions.get(data[1])) {
                ArrayList<String> gameData = new ArrayList<>();
                gameData.add(lobby.getLobbyName());
                gameData.add(lobby.getGamemode());
                gameData.add(lobby.getLobbyMap());
                lobby.setGame(new ServerGame(lobby.getPlayers()));//TODO add rest of data
                int i = 1;
                String stringRep;
                for (User player : lobby.getPlayers()) {
                    stringRep = String.valueOf(i);
                    gameData.add(stringRep);//give startingposition
                    player.getuSession().getAsyncRemote().sendText(DataPacker.packData(GAME_START, DataPacker.stringCombiner(gameData)));
                    gameData.remove(stringRep);//remove startingposition for next loop
                    i++;
                }
            }
        }
    }

    /**
     * Sends all current lobbys that arent ingame to given client.
     *
     * @param userID sessionID of the given user
     */
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
        sessions.get(userID).getAsyncRemote().sendText(DataPacker.packData(GET_LOBBYS, DataPacker.stringCombiner(arr)));

    }

    /**
     * Given user is removed from given lobby
     *
     * @param data received data
     */
    private void leaveLobby(String[] data) {
        ServerLobby lobby = findLobby(data);
        if (lobby != null) {
            boolean leave = lobby.leaveLobby(userSessions.get(data[1]));
            if (leave) {
                sendPlayerChangeUpdate(lobby);
                if (lobby.getAdmin() == null) {
                    lobbys.remove(lobby.getLobbyName());
                }
            }
        }
    }

    /**
     * Sends a update to all players in the given lobby about which players are connected to the given lobby.
     *
     * @param data received data
     */
    //ENUM,LOBBYID,Usersession
    public void sendPlayerListUpdate(String[] data) {//this should get triggered once the game is over (incase anyone left during the game); maybe public later
        ServerLobby lobby = lobbys.get(data[2]);
        if (lobby.getGame() == null) {
            ArrayList<String> changedData = new ArrayList<>(lobby.getPlayerNames());
            changedData.add(String.valueOf(lobby.getPlayerOrder(userSessions.get(data[1]))));
            sessions.get(data[1]).getAsyncRemote().sendText(DataPacker.packData(LOBBY_PLAYERS, DataPacker.stringCombiner(changedData)));
        }
    }

    /**
     * Sends a update to all players in the given lobby about what their join order is.
     *
     * @param lobby received data
     */
    public void sendPlayerChangeUpdate(ServerLobby lobby) {
        if (lobby.getGame() == null) {
            for (User user : lobby.getPlayers()) {
                ArrayList<String> changedData = new ArrayList<>(lobby.getPlayerNames());
                changedData.add(String.valueOf(lobby.getPlayerOrder(user)));
                user.getuSession().getAsyncRemote().sendText(DataPacker.packData(LOBBY_PLAYERS, DataPacker.stringCombiner(changedData)));
            }
        }
    }

    /**
     * Join process for the given player and lobby. Sends an update to all players in the lobby if join is successful.
     *
     * @param data received data
     */
    private void joinLobby(String[] data) {
        boolean joined = false;
        ServerLobby lobby = findLobby(data);
        if (lobby != null) {
            if (lobby.getGame() == null) {
                joined = lobby.joinLobby(userSessions.get(data[1]));
                if (joined) {
                    sendPlayerChangeUpdate(lobby);
                    ArrayList<String> lobbyDataToSend = new ArrayList<>();
                    lobbyDataToSend.add("true");
                    lobbyDataToSend.add(lobby.getLobbyName());
                    lobbyDataToSend.add(lobby.getLobbyMap());
                    lobbyDataToSend.add(String.valueOf(lobby.getPlayerAmount()));
                    lobbyDataToSend.add(lobby.getGamemode());
                    lobbyDataToSend.add(String.valueOf(lobby.getPlayerOrder((userSessions.get(data[1])))));
                    sessions.get(data[1]).getAsyncRemote().sendText(DataPacker.packData(JOIN_LOBBY, DataPacker.stringCombiner(lobbyDataToSend)));
                }
            }
        }
        if (!joined) {
            sessions.get(data[1]).getAsyncRemote().sendText(DataPacker.packData(JOIN_LOBBY, "false"));
        }
    }

    /**
     * Creates a lobby with the given parameters if allowed
     *
     * @param data received data
     */
    private void createLobby(String[] data) {
        if (userSessions.containsKey(data[1])) {
            if (!lobbys.containsKey(data[2])) {
                User[] users = new User[Integer.parseInt(data[4])];
                users[0] = userSessions.get(data[1]);
                ServerLobby newLobby = new ServerLobby(users, data[2], data[3], users.length, data[5]);
                lobbys.put(data[2], newLobby);

                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("true");
                dataList.add(data[2]);
                dataList.add(String.valueOf(newLobby.getPlayerOrder(users[0])));
                sessions.get(data[1]).getAsyncRemote().sendText(DataPacker.packData(CREATE_LOBBY, DataPacker.stringCombiner(dataList)));
                return;
            }
        }
        sessions.get(data[1]).getAsyncRemote().sendText(DataPacker.packData(CREATE_LOBBY, "false"));
    }

    /**
     * @param args received data
     * @return the lobby object with the lobbyname in {@param args[2]}
     */
    private ServerLobby findLobby(String[] args) {
        return lobbys.getOrDefault(args[2], null);
    }


    /**
     * Saves user and session id in relevant lists for later retrieval
     *
     * @param user the given user
     */
    private void loginUser(User user) {
        //TODO is this the most appropiate place for this?
        if (user != null && user.getuSession() != null) {
            String id = user.getuSession().getId();
            if (sessions.containsKey(id)) {
                userSessions.put(id, user);
            }
        }
    }

    /**
     * Sends positions and health of all units of given player to rest of lobby.
     *
     * @param data received data
     */
    private void genSoldierPosArray(String[] data) {
        ServerLobby lobby = findLobby(data);
        if (lobby != null) {
            if (lobby.getGame() != null) {
                ArrayList<String> changedData = new ArrayList<>();
                changedData.add(data[3]);
                for (int i = 4; i < data.length; i += 2) {
                    changedData.add(data[i]);//x/y/health
                    changedData.add(data[i + 1]);
                }
                for (User user : lobby.getPlayers()) {
                    if (!sessions.get(data[1]).equals(user.getuSession())) {
                        //1001 error here ; Server thinks client navigates away/closes?
                        user.getuSession().getAsyncRemote().sendText(DataPacker.packData(UPDATE_SOLDIER_POS, DataPacker.stringCombiner(changedData)));
                    }
                }
            }
        }
    }

    /**
     * Sends position and health of castle to the rest of the lobby
     *
     * @param data received data
     */
    private void genCastlePosArray(String[] data) {
//MI,UID,LID,Startingpos, health,hashcode
        ServerLobby lobby = findLobby(data);
        if (lobby != null) {
            if (lobby.getGame() != null) {
                ArrayList<String> changedData = new ArrayList<>();
                changedData.add(data[3]);
                changedData.add(data[4]);
                changedData.add(data[5]);
                for (User user : lobby.getPlayers()) {
                    if (!sessions.get(data[1]).equals(user.getuSession())) {
                        //1001 error here ; Server thinks client navigates away/closes?
                        user.getuSession().getAsyncRemote().sendText(DataPacker.packData(UPDATE_CASTLE_POS, DataPacker.stringCombiner(changedData)));
                        if (Integer.parseInt(data[4]) <= 0) {
                            lobby.getGame().killPlayer(Integer.parseInt(data[3]));
                            if (lobby.getGame().getWinner() != null) {
                                endGame(lobby);
                                //send game over message and points for everyone
                            }
                            //TODO send lose msg
                            //user.getuSession().getAsyncRemote().sendText();
                        }
                    }
                }
            }
        }
    }

    /**
     * Sends an game over message to all players within the lobby and relevant info (scores and lobby information)
     *
     * @param lobby the given lobby
     */
    private void endGame(ServerLobby lobby) {
        ServerGame game = lobby.getGame();
        lobby.setGame(null);
        ArrayList<String> gameOverData = new ArrayList<>();

        //actual number of plyers,winner name, winner score, Ith player name,Ith player score,lobby max players, lobbyname, lobbymap,gamemode,own player order
        gameOverData.add(String.valueOf(game.getPlayers().length));
        gameOverData.add(game.getWinner().getUsername());
        gameOverData.add(String.valueOf(game.getPoints(game.getWinner().getuSession().getId())));
        for (User user : game.getPlayers()) {
            gameOverData.add(user.getUsername());
            gameOverData.add(String.valueOf(game.getPoints(user.getuSession().getId())));
        }
        gameOverData.add(String.valueOf(lobby.getPlayerAmount()));
        gameOverData.add(lobby.getLobbyName());
        gameOverData.add(lobby.getLobbyMap());
        gameOverData.add(lobby.getGamemode());
        //[]
        for (User user : lobby.getPlayers()) {
            gameOverData.add(String.valueOf(lobby.getPlayerOrder((user))));
            user.getuSession().getAsyncRemote().sendText(DataPacker.packData(GAME_OVER, DataPacker.stringCombiner(gameOverData)));
            gameOverData.remove(gameOverData.size() - 1);
        }
    }

    /**
     * Sends damage values and target to the relevant players
     *
     * @param data received data
     */
    private void attackUnitUpdate(String[] data) {
        //Syntax: [MI,lobbyID,starting STARTING POSITION (of enemy),UNITTYPE(Soldier or castle),enemy hashcode, DAMAGE TYPE,ATK]
        ServerLobby lobby = findLobby(data);
        if (lobby != null) {
            if (lobby.getGame() != null) {
                ArrayList<String> updateData = new ArrayList<>();
                updateData.add(data[3]);//unittype
                updateData.add(data[4]);//hashcode
                updateData.add(data[5]);//dmgtype
                updateData.add(data[6]);//dmg
                lobby.getGame().getPlayers()[Integer.parseInt(data[1])].getuSession().getAsyncRemote().sendText(DataPacker.packData(ATTACK_UNIT_UPDATE, DataPacker.stringCombiner(updateData)));
            }
        }
    }

    public Map<String, Session> getSessions() {
        return sessions;
    }


    private void broadcast(String message) {

    }
}
