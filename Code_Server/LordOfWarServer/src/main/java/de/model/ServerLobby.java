package de.model;

import de.communication.DataPacker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static de.constants.MessageIdentifier.JOIN_LOBBY;
import static de.constants.MessageIdentifier.LOBBY_PLAYERS;


public class ServerLobby {

    private ArrayList<User> players;
    private ArrayList<String> playerNames;
    private HashMap<User, Integer> joinOrder;

    private String lobbyName;
    private String lobbyMap;
    private int playerAmount;
    private String gamemode;

    private ServerGame game;
    private int joinCounter;
    private User admin;

    //TODO gamesettings
    //todo das server lobby objekt muss wie folgt erstellt werden (name der lobby , lobby map, player amount, gamemode, etc..)
    public ServerLobby(User[] players, String lobbyName, String lobbyMap, int playerAmount, String gamemode) {
        joinCounter = 0;
        this.players = new ArrayList<>();
        this.playerNames = new ArrayList<>();
        this.joinOrder = new HashMap<>();

        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                if (joinOrder.isEmpty()) {
                    admin = players[i];
                }
                this.players.add(players[i]);
                this.playerNames.add(players[i].getUsername());
                joinOrder.put(players[i], joinCounter);
                joinCounter++;
            }
        }
        this.game = null;

        this.lobbyName = lobbyName;
        this.lobbyMap = lobbyMap;
        this.playerAmount = playerAmount;
        this.gamemode = gamemode;
    }

    public boolean joinLobby(User user) {
        if (players.size() < playerAmount) {
            if (!joinOrder.containsKey(user)) {
                players.add(user);
                playerNames.add(user.getUsername());
                joinOrder.put(user, joinCounter);
                joinCounter++;
                return true;
            }
        }
        return false;
    }

    public boolean leaveLobby(User user) {
        if (players.contains(user)) {
            players.remove(user);
            playerNames.remove(user.getUsername());
            joinOrder.remove(user);
            if (admin == user) {
                User newAdmin = null;
                int lowest = Integer.MAX_VALUE;
                for (Map.Entry<User, Integer> entry : joinOrder.entrySet()) {
                    if (entry.getValue() < lowest) {
                        newAdmin = entry.getKey();
                        lowest = entry.getValue();
                    }
                }
                admin = newAdmin;
            }
            return true;
        }

        return false;
    }

    public ServerGame getGame() {
        return game;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public HashMap<User, Integer> getJoinOrder() {
        return joinOrder;
    }

    public void setJoinOrder(HashMap<User, Integer> joinOrder) {
        this.joinOrder = joinOrder;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyMap() {
        return lobbyMap;
    }

    public void setLobbyMap(String lobbyMap) {
        this.lobbyMap = lobbyMap;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public void setGame(ServerGame game) {
        this.game = game;
    }

    public int getJoinCounter() {
        return joinCounter;
    }

    public void setJoinCounter(int joinCounter) {
        this.joinCounter = joinCounter;
    }

    public User getAdmin() {
        return admin;
    }
    //TODO add methods for
    // -create (constructor or static create?)
    // -get
    // -delete
    // -player join method
    // -isInGame (return game!=null)
}
