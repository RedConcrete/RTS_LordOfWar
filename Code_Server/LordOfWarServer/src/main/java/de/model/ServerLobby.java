package de.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//for keeping data about a lobby
public class ServerLobby {

    private ArrayList<User> players;//players in the lobby (adaptive since max players can be switched)
    private HashMap<User,Integer> joinOrder;//lowest number is admin

    private String lobbyName;
    private String lobbyMap;
    private int playerAmount;
    private String gamemode;

    private ServerGame game;
    private int joinCounter;

    //TODO gamesettings
    //todo das server lobby objekt muss wie folgt erstellt werden (name der lobby , lobby map, player amount, gamemode, etc..)
    public ServerLobby(User[] players, String lobbyName, String lobbyMap, int playerAmount, String gamemode) {
        joinCounter =0;
        this.players=new ArrayList<>();
        this.joinOrder=new HashMap<>();

        for (int i = 0; i < players.length; i++) {//garantee order
            if(players[i] != null){
                this.players.add(players[i]);
                joinOrder.put(players[i],joinCounter);
                joinCounter++;
            }
        }
        this.game = null;

        this.lobbyName = lobbyName;
        this.lobbyMap = lobbyMap;
        this.playerAmount = playerAmount;
        this.gamemode = gamemode;
    }

    public ServerGame getGame() {
        return game;
    }

    public ArrayList<User> getPlayers() {
        return players;
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

    //TODO add methods for
    // -create (constructor or static create?)
    // -get
    // -delete
    // -player join method
    // -isInGame (return game!=null)
}
