package code.lordofwar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LobbyBrowserScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;

    public LobbyBrowserScreen(Game aGame, Skin aSkin) {
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
        clearStage();

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
    public void hide() {stage.clear();}

    @Override
    public void dispose() {stage.dispose();}


    private void setupUI() {

        Window windowLobbyBrowser = new Window("", skin, "border");
        windowLobbyBrowser.defaults().pad(20f);
        backButton(stage,skin,game,windowLobbyBrowser);
        windowLobbyBrowser.pack();

        windowLobbyBrowser.setPosition(stage.getWidth() / 2f - windowLobbyBrowser.getWidth() / 2f,
                stage.getHeight() / 2f - windowLobbyBrowser.getHeight() / 2f);
        windowLobbyBrowser.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(windowLobbyBrowser);


        stage.setDebugAll(false);
    }
}
