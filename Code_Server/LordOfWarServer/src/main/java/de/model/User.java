package de.model;


import javax.websocket.Session;


public class User {
    private String username;
    private String password;
    private int score;

    private final int userID;
    private Session uSession;

    public User(String username, String password, Integer score, int userID, Session uSession) {
        if (score==null){
            score=0;
        }
        this.username = username;
        this.password = password;
        this.score = score;
        this.userID = userID;
        this.uSession = uSession;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        if (score+this.score<this.score){
            this.score=Integer.MAX_VALUE;
        }else {
            this.score += score;
        }
    }

    public void subtractScore(int score) {
        if (this.score>score) {
            this.score -= score;
        }else {
            this.score=0;
        }
    }

    public Session getuSession() {
        return uSession;
    }

    public void setuSession(Session uSession) {
        this.uSession = uSession;
    }


   public int getUserID() {
        return userID;
    }
}
