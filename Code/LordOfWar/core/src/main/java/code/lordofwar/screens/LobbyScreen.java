package code.lordofwar.screens;

import code.lordofwar.main.LOW;
import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LobbyScreen extends Screens implements Screen {

    private final Stage stage;
    private final LOW game;
    private final Skin skin;
    private String[] playerNameArr;
    public String[] gameInfoArr;
    private final String lobbyID;

    public LobbyScreen(LOW aGame, Skin aSkin,String lobbyID) {
        this.lobbyID=lobbyID;
        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        playerNameArr = new String[]{"Username"};
        gameInfoArr = new String[]{"map \n" + "gamemode\n" + "...\n"};

        createBackground(stage);

        setupUI();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT |  GL20.GL_DEPTH_BUFFER_BIT );

        fps(stage,skin);

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

        TextButton startButton = new TextButton("Start Game",skin);
        startButton.getLabel().setFontScale(3f);

        List<String> playerList = new List<String>(skin);
        playerList.setItems(playerNameArr);

        List<String> gameInfoList = new List(skin);
        gameInfoList.setItems(gameInfoArr);


        startButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                //Todo server muss wissen das die Lobby gestartet wurde!

                game.setScreen(new GameScreen(game, skin,lobbyID));

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }

        });
        windowLobby.add(playerList);
        windowLobby.add(gameInfoList).row();

        windowLobby.add(startButton);


        backButton(stage,skin,game,windowLobby);
        packAndSetWindow(windowLobby,stage);

        stage.setDebugAll(false);
    }

}
