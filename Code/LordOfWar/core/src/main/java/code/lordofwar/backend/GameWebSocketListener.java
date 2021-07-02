package code.lordofwar.backend;

import code.lordofwar.backend.events.GameScreenEvent;
import code.lordofwar.main.LOW;
import code.lordofwar.screens.*;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static code.lordofwar.backend.Constants.STRINGSEPERATOR;
import static code.lordofwar.backend.MessageIdentifier.*;

public class GameWebSocketListener extends WebSocketListener {

    LOW game;

    public GameWebSocketListener(LOW aGame) {
        game = aGame;
    }

    private WebSocket gameWebSocket = null;

    Constants constants = new Constants();

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        System.out.println("closing " + reason);
        super.onClosed(webSocket, code, reason);

    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        System.out.println("failure " + response);
        t.printStackTrace();
        super.onFailure(webSocket, t, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String message) {
        String[] dataArray = depackData(message);
        checkDataDir(dataArray);
    }

    private String[] depackData(String message) {
        return message.split(STRINGSEPERATOR);
    }

    private void checkDataDir(String[] strings) {
        if (strings[0].equals(LOGIN_VALID.toString())) {
            if (game.getScreen() instanceof LoginScreen) {
                ((LoginScreen) game.getScreen()).getLoginScreenEvent().setLoginAnswer(strings);
            }
        } else if (strings[0].equals(REGISTER_VALID.toString())) {
            if (game.getScreen() instanceof RegisterScreen) {
                ((RegisterScreen) game.getScreen()).getRegisterScreenEvent().setRegisterAnswer(strings);
            }
        } else if (strings[0].equals(GET_GAME_POINTS.toString())) {
            if (game.getScreen() instanceof GameScreen) {
                ((GameScreen) game.getScreen()).getGameScreenEvent().updatePoints(strings);
            }
        } else if (strings[0].equals(CONNECTION.toString())) {
            game.setSessionID(strings[1]);
        } else if (strings[0].equals(CREATE_LOBBY.toString())) {
            if (game.getScreen() instanceof LobbyCreateScreen) {
                ((LobbyCreateScreen) game.getScreen()).getLobbyCreateScreenEvent().setLobbyID(strings);
            }
        } else if (strings[0].equals(GET_LOBBYS.toString())) {
            if (game.getScreen() instanceof LobbyBrowserScreen) {
                ((LobbyBrowserScreen) game.getScreen()).getLobbyBrowserScreenEvent().setLobbyList(strings);
            }
        } else if (strings[0].equals(JOIN_LOBBY.toString())) {
            if (game.getScreen() instanceof LobbyBrowserScreen) {
                ((LobbyBrowserScreen) game.getScreen()).getLobbyBrowserScreenEvent().setJoined(strings);
            }
        } else if (strings[0].equals(LOBBY_PLAYERS.toString())) {
            if (game.getScreen() instanceof LobbyScreen) {//very important! this can be triggered in the browser or create screen and needs to be discarded inn that case
                ((LobbyScreen) game.getScreen()).getLobbyScreenEvent().setPlayers(strings);
            }
        } else if (strings[0].equals(GAME_START.toString())) {
            if (game.getScreen() instanceof LobbyScreen) {
                ((LobbyScreen) game.getScreen()).getLobbyScreenEvent().setGameData(strings);
            }
        } else if (strings[0].equals(UPDATE_SOLDIER_POS.toString())) {
            if (game.getScreen() instanceof GameScreen) {
                ((GameScreen) game.getScreen()).getGameScreenEvent().processSoilders(strings);
            }
        }
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {

        game.getConstants().setWEBSOCKET_OPEN(true);

    }

    public void setWebSocket(WebSocket websocket) {
        gameWebSocket = websocket;
    }
}
