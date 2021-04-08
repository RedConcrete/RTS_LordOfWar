package code.lordofwar.backend;

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

    private CommunicationHandler communicationHandler = null;
    Constants constants = new Constants();

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String message) {

        String[] dataArray = depackData(message);
        checkDataDir(dataArray);
    }

    private String[] depackData(String message){
        System.out.println(message);
        return message.split(STRINGSEPERATOR);
    }

    private void checkDataDir(String[] strings){
        System.out.println(Arrays.toString(strings));
        for (MessageIdentifier messageIdentifier : MessageIdentifier.values()) {
            if(strings[0].equals(messageIdentifier.toString())){
                if(strings[0].equals(LOGIN_VALID.toString())){
                    if(game.getScreen() instanceof LoginScreen){
                        ((LoginScreen) game.getScreen()).getLoginScreenEvent().setLoginAnswer(strings);
                    }
                }else if(strings[0].equals(REGISTER_VALID.toString())){
                    if(game.getScreen() instanceof RegisterScreen){
                        ((RegisterScreen) game.getScreen()).getRegisterScreenEvent().setRegisterAnswer(strings);
                    }
                }else if (strings[0].equals(GET_GAME_POINTS.toString())){
                    if (game.getScreen() instanceof GameScreen){
                        ((GameScreen) game.getScreen()).getGameScreenEvent().updatePoints(strings);
                    }
                }else if (strings[0].equals(CONNECTION.toString())){
                    game.setSessionID(strings[1]);//set session id
                }else if (strings[0].equals(CREATE_LOBBY.toString())){
                    ((LobbyCreateScreen) game.getScreen()).getLobbyCreateScreenEvent().setLobbyID(strings);
                }
            }
        }
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }


    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {

        System.out.println("websocket to server is open!");
        game.getCon().setWEBSOCKET_OPEN(true);

    }

    public void setWebSocket(WebSocket websocket){
        gameWebSocket = websocket;
    }

    public void sendMessage(String message){
        gameWebSocket.send(message);
    }

    public void setCommunicationHandler(CommunicationHandler communicationHandler) {
        this.communicationHandler = communicationHandler;
    }
}
