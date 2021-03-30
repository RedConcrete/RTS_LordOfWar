package code.lordofwar.screens;

import code.lordofwar.backend.events.LobbyCreateScreenEvent;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LobbyCreateScreen extends Screens implements Screen {

    private final Stage stage;
    private final Game game;
    private final Skin skin;

    public LobbyCreateScreen(Game aGame, Skin aSkin) {

        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());

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
        //es muss alles übermalt werden anders können aneblich die actors nicht entfernt werden :|
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

        Window windowLobbyCreate = new Window("", skin, "border");
        windowLobbyCreate.defaults().pad(20f);
        windowLobbyCreate.setMovable(false);

        TextButton lobbyCreateButton = new TextButton("Lobby erstellen",skin);
        lobbyCreateButton.getLabel().setFontScale(3f);

        Label lobbyNameLabel = new Label("Lobby name",skin);
        lobbyNameLabel.setFontScale(2f);

        TextField lobbyName = new TextField("",skin);

        lobbyCreateButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("LobbyErstellen");
                //Todo Abfrage entwickeln!!!


                if(LobbyCreateScreenEvent.isGameStart()){
                    game.setScreen(new LobbyScreen(game, skin));
                }
                else{

                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        windowLobbyCreate.add(lobbyNameLabel).row();
        windowLobbyCreate.add(lobbyName).row();


        windowLobbyCreate.add(lobbyCreateButton).row();
        backButton(stage,skin,game,windowLobbyCreate);
        packAndSetWindow(windowLobbyCreate,stage);



        stage.setDebugAll(false);
    }

}
