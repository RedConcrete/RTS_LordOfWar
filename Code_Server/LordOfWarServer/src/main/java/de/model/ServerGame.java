package de.model;

import java.util.ArrayList;
import java.util.HashMap;

//For keeping data about a running game
public class ServerGame {
    private final User[] players;//static array
    private HashMap<String,User> sessionIDMap;
    private HashMap<User, Integer> pointMap;//how many points the players earned

    public ServerGame(ArrayList<User> players) {
        this.players = new User[players.size()];
        pointMap = new HashMap<>();
        sessionIDMap=new HashMap<>();
        int i = 0;
        for (User user : players) {
            this.players[i] = user;
            pointMap.put(user, 0);
            i++;
            sessionIDMap.put(user.getuSession().getId(),user);
        }
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

    public int getPoints(String sessionID){
        return pointMap.get(sessionIDMap.get(sessionID));
    }
    //TODO add score to keep track for each player
    //TODO add rest of relevant code(not part of my userstory)
}