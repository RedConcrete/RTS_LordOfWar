package code.lordofwar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
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

    }

    @Override
    public void render(float delta) {

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

    }

    @Override
    public void dispose() {

    }

    private void setupUI() {


        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(3f);


        Window windowLobby = new Window("", skin, "border");
        windowLobby.defaults().pad(20f);

        windowLobby.pack();

        windowLobby.setPosition(stage.getWidth() / 2f - windowLobby.getWidth() / 2f,
                stage.getHeight() / 2f - windowLobby.getHeight() / 2f);
        windowLobby.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(windowLobby);

        stage.setDebugAll(false);
    }

}
