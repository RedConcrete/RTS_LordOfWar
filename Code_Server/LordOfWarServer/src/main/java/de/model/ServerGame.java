package de.model;

import java.util.ArrayList;
import java.util.HashMap;


public class ServerGame {
    private final User[] players;
    private final User[] livingPlayers;
    private HashMap<String, User> sessionIDMap;
    private HashMap<User, Integer> pointMap;
    private User winner;

    public ServerGame(ArrayList<User> players) {
        this.players = new User[players.size()];
        pointMap = new HashMap<>();
        sessionIDMap = new HashMap<>();
        int i = 0;
        for (User user : players) {
            this.players[i] = user;
            pointMap.put(user, 0);
            i++;
            sessionIDMap.put(user.getuSession().getId(), user);
        }
        winner=null;
        livingPlayers = this.players;
    }

    public User[] getLivingPlayers() {
        return livingPlayers;
    }

    public void killPlayer(int startingPos) {
        livingPlayers[startingPos] = null;
        int i = 0;
        for (User user : livingPlayers) {
            if (user != null) {
                i++;
            }
        }
        if (i==1){
            setWinner();
        }
    }
    private void setWinner(){
        for (User user:livingPlayers) {
            if (user!=null){
                winner=user;
            }
        }
    }

    public User getWinner(){
        return winner;
    }


    private void addPoints(User user, int points) {
        int score = pointMap.get(user);
        if (score + points < score) {
            pointMap.put(user, Integer.MAX_VALUE);
        } else {
            pointMap.put(user, pointMap.get(user) + points);
        }
    }

    private void subtractPoints(User user, int points) {
        int score = pointMap.get(user);
        if (score > points) {
            pointMap.put(user, pointMap.get(user) - points);
        } else {
            pointMap.put(user, 0);
            //no minus score
        }
    }

    //TODO kill all relevant units of a player if he leaves

    public int getPoints(String sessionID) {
        return pointMap.get(sessionIDMap.get(sessionID));
    }
    //TODO add score to keep track for each player
    //TODO add rest of relevant code(not part of my userstory)

    public User[] getPlayers() {
        return players;
    }
}
