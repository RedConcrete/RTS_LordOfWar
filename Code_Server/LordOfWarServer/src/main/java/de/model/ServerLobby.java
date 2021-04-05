package de.model;

import java.util.ArrayList;
import java.util.HashMap;

//for keeping data about a lobby
public class ServerLobby {
    private ArrayList<User> players;//players in the lobby (adaptive since max players can be switched)
    private HashMap<User,Integer> joinOrder;//lowest number is admin
    private ServerGame game;
    private int joinCounter;
    //TODO gamesettings

    public ServerLobby(User[] players){
        joinCounter =0;
        this.players=new ArrayList<>();
        this.joinOrder=new HashMap<>();
        this.game=null;
        for (int i = 0; i < players.length; i++) {//garantee order
            this.players.add(players[i]);
            joinOrder.put(players[i],joinCounter);
            joinCounter++;
        }
    }

    //TODO add methods for
    // -create (constructor or static create?)
    // -get
    // -delete
    // -player join method
    // -isInGame (return game!=null)
}
