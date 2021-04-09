package code.lordofwar.screens;

import code.lordofwar.backend.Lobby;
import code.lordofwar.backend.events.LobbyBrowserScreenEvent;
import code.lordofwar.backend.events.LobbyCreateScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LobbyBrowserScreen extends Screens implements Screen {

    private final Stage stage;
    private final LOW game;
    private final Skin skin;
    private LobbyBrowserScreenEvent lobbyBrowserScreenEvent;

    public LobbyBrowserScreen(LOW aGame, Skin aSkin) {
        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        lobbyBrowserScreenEvent = new LobbyBrowserScreenEvent(game);

        createBackground(stage);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setupUI();

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

        lobbyBrowserScreenEvent.sendRequestGetLobbys();

        try {
            TimeUnit.SECONDS.sleep(2); // todo schauen ob delay immer ausreicht!
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Window windowLobbyBrowser = new Window("", skin, "border");
        windowLobbyBrowser.defaults().pad(20f);

        String[] strings = lobbyBrowserScreenEvent.getLobbyList();

        for (int i = 4; i < strings.length - 4; i += 4) {
            TextButton textButton = new TextButton("join",skin);
            Label l = new Label(strings[i - 3] +"  "+ strings[i - 2] +"  "+ strings[i - 1] +"  "+ strings[i] ,skin);
            l.setFontScale(2f);
            windowLobbyBrowser.add(l);
            windowLobbyBrowser.add(textButton).row();
        }

        backButton(stage,skin,game,windowLobbyBrowser);
        packAndSetWindow(windowLobbyBrowser,stage);
        stage.addActor(windowLobbyBrowser);
        stage.setDebugAll(false);
    }

    public LobbyBrowserScreenEvent getLobbyBrowserScreenEvent() {
        return lobbyBrowserScreenEvent;
    }
}
