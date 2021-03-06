package code.lordofwar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LobbyScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;

    public LobbyScreen(Game aGame, Skin aSkin) {

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


        TextButton backButton = backButton(stage,skin,game);
        backButton.getLabel().setFontScale(3f);

        TextButton startButton = new TextButton("Start Game",skin);
        startButton.getLabel().setFontScale(3f);

        Window windowLobby = new Window("", skin, "border");
        windowLobby.defaults().pad(20f);

        startButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("LobbyErstellen");
                //Todo server muss wissen das die Lobby gestartet wurde!

                game.setScreen(new GameScreen(game, skin));

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }

        });


        windowLobby.add(backButton);
        windowLobby.add(startButton);
        windowLobby.pack();

        windowLobby.setPosition(stage.getWidth() / 2f - windowLobby.getWidth() / 2f,
                stage.getHeight() / 2f - windowLobby.getHeight() / 2f);
        windowLobby.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.25f)));
        stage.addActor(windowLobby);

        stage.setDebugAll(true);
    }

}
