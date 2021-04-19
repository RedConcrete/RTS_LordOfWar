package code.lordofwar.screens;

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

public class LobbyScreen extends Screens implements Screen {


    private String[] playerNameArr;
    public String[] gameInfoArr;
    private final String lobbyID;
    private List<String> playerList;
    private LobbyScreenEvent lobbyScreenEvent;

    public LobbyScreen(LOW aGame, Skin aSkin, String lobbyID) {
        super(aGame, aSkin);
        this.lobbyID = lobbyID;

        playerNameArr = new String[]{"Username1"};
        gameInfoArr = new String[]{"map \n" + "gamemode\n" + "...\n"};
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
            game.setScreen(new GameScreen(game, skin, gameData[0], Integer.parseInt(gameData[gameData.length - 1])));//todo insert data here via lobbyScreenEvent.getData
        }

        if (!lobbyScreenEvent.isRecievedData()) {//later data requests (map etc) also go here
            lobbyScreenEvent.sendPlayerRequest(lobbyID);//possible to move this to the end of setupUi()?
        }

        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        fps(stage, skin);

        playerNameArr = lobbyScreenEvent.getPlayers();
        if (playerNameArr != null) {
            playerList.setItems(playerNameArr);
        }// for (int i = 0; i < playerNameArr.length; i++) {
        //     playerNameArr[i]=playerNameArr[i]+"\n";//TODO formatting properly
        //  }

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

    private void setupUI() {

        Window windowLobby = new Window("", skin, "border");
        windowLobby.defaults().pad(50f);

        TextButton startButton = new TextButton("Start Game", skin);
        startButton.getLabel().setFontScale(3f);

        playerList = new List<String>(skin);
        playerList.setItems(playerNameArr);//no point to this anymore right?

        List<String> gameInfoList = new List(skin);
        gameInfoList.setItems(gameInfoArr);


        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //Todo server muss wissen das die Lobby gestartet wurde!
                lobbyScreenEvent.sendGameStartRequest(lobbyID);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }

        });
        windowLobby.add(playerList);
        windowLobby.add(gameInfoList).row();

        windowLobby.add(startButton);

        backButton(stage, skin, game, windowLobby);
        packAndSetWindow(windowLobby, stage);

        stage.setDebugAll(false);
    }

    @Override
    protected void backButton(Stage stage, Skin skin, LOW game, Window window) {
        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(3f);

        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                lobbyScreenEvent.sendLeaveLobbyNotice(LobbyScreen.this.lobbyID);
                game.setScreen(new MenuScreen(game, skin));
                stage.dispose();

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        window.add(backButton).row();
    }

    public LobbyScreenEvent getLobbyScreenEvent() {
        return lobbyScreenEvent;
    }
}
