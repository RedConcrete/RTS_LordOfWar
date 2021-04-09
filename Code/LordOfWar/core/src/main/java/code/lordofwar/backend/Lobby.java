package code.lordofwar.backend;

public class Lobby {

    private String lobbyname;
    private String map;
    private int playerAmount;
    private String gamemode;

    public Lobby(String lobbyname, String map, int playerAmount, String gamemode) {
        this.lobbyname = lobbyname;
        this.map = map;
        this.playerAmount = playerAmount;
        this.gamemode = gamemode;
    }

    public String getLobbyname() {
        return lobbyname;
    }

    public void setLobbyname(String lobbyname) {
        this.lobbyname = lobbyname;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
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
}
