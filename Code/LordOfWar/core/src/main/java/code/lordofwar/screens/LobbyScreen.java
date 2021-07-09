package code.lordofwar.screens;

import code.lordofwar.backend.Lobby;
import code.lordofwar.backend.events.LobbyScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The LobbyScreen that shows the LobbyScreen a player is connected with
 *
 * @author Franz Klose,Cem Arslan
 */
public class LobbyScreen extends Screens implements Screen {


    private String[] playerNameArr;
    public String[] gameInfoArr;
    private List<String> playerList;
    private LobbyScreenEvent lobbyScreenEvent;
    private boolean lobbyAdmin;

    public LobbyScreen(LOW aGame, Skin aSkin, String[] lobbyInfo, Boolean admin) {
        super(aGame, aSkin);
        gameInfoArr = new String[4];
        System.arraycopy(lobbyInfo, 0, gameInfoArr, 0, 4);
        playerNameArr = new String[]{""};
        lobbyAdmin = admin;
        lobbyScreenEvent = new LobbyScreenEvent(game);
        createBackground(stage);
        setupUI();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        if (lobbyScreenEvent.isStartedGame()) {
            String[] gameData = lobbyScreenEvent.getGameData();
            //[1]=lobbyname[2]=gamemode[3]=map
            //System.out.println(Arrays.toString(gameData));
            game.setScreen(new GameScreen(game, skin, gameData[0], lobbyScreenEvent.getPosition(), lobbyScreenEvent.getPlayers()));
        }
        if (!lobbyScreenEvent.isRecievedData()) {//later data requests (map etc) also go here
            lobbyScreenEvent.sendPlayerRequest(gameInfoArr[0]);//possible to move this to the end of setupUi()?
        }

        playerNameArr = lobbyScreenEvent.getPlayers();
        if (playerNameArr != null) {
            String[] playerString = new String[playerNameArr.length + 1];
            playerString[0] = "Users: \n";
            System.arraycopy(playerNameArr, 0, playerString, 1, playerNameArr.length);
            playerList.setItems(playerString);
        }

        clearStage();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * builds the Ui for the Screen
     */
    private void setupUI() {

        Window windowLobby = new Window("", skin, "border");
        windowLobby.defaults().pad(50f);

        TextButton startButton = new TextButton("Start Game", skin);
        startButton.setVisible(lobbyAdmin);
        startButton.getLabel().setFontScale(3f);

        playerList = new List<>(skin);
        playerList.setItems(playerNameArr);//no point to this anymore right?

        List<String> gameInfoList = new List<>(skin);

        gameInfoList.setItems("Lobby Information",
                "Lobbyname: " + gameInfoArr[0],
                "Map: " + gameInfoArr[1],
                "Max Players: " + gameInfoArr[2],
                "Mode: " + gameInfoArr[3]);
        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                lobbyScreenEvent.sendGameStartRequest(gameInfoArr[0]);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

        });
        windowLobby.add(playerList);
        windowLobby.add(gameInfoList).padBottom(30f).row();

        windowLobby.add(startButton).padTop(30f);

        backButton(stage, skin, game, windowLobby,true);
        packAndSetWindow(windowLobby, stage);

        stage.setDebugAll(false);
    }

    /**
     * A special defined back button
     * @param stage
     * @param skin
     * @param game
     * @param window
     * @param isBackToMenu
     */
    @Override
    protected void backButton(Stage stage, Skin skin, LOW game, Window window,boolean isBackToMenu) {
        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(3f);

        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isBackToMenu) {
                    lobbyScreenEvent.sendLeaveLobbyNotice(LobbyScreen.this.gameInfoArr[0]);
                    game.setScreen(new MenuScreen(game, skin));
                    stage.dispose();
                } else {
                    window.setVisible(false);
                }
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        window.add(backButton).padTop(30f).row();
    }

    public LobbyScreenEvent getLobbyScreenEvent() {
        return lobbyScreenEvent;
    }
}
