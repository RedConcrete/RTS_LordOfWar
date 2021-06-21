package code.lordofwar.screens;

import code.lordofwar.backend.Rumble;
import code.lordofwar.backend.events.LobbyBrowserScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.concurrent.TimeUnit;

public class LobbyBrowserScreen extends Screens implements Screen {


    private LobbyBrowserScreenEvent lobbyBrowserScreenEvent;

    public LobbyBrowserScreen(LOW aGame, Skin aSkin) {
        super(aGame, aSkin);
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

        fps(stage, skin);

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
        lobbyBrowserScreenEvent.sendRequestGetLobbys();

        Window windowLobbyBrowser = new Window("", skin, "border");
        windowLobbyBrowser.defaults().pad(20f);

        Window errorWindow = new Window("", skin, "border");
        errorWindow.setMovable(false);
        errorWindow.defaults().pad(20f);

        TextButton okButton = new TextButton("OK", skin);


        new Thread(() -> {
            try {
                Thread.sleep(game.getConstants().STANDARD_TIME_WAIT);//2 sec

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Gdx.app.postRunnable(
                    new Runnable() {
                        @Override
                        public void run() {
                            String[] strings = lobbyBrowserScreenEvent.getLobbyList();
                            for (int i = 4; i < strings.length; i += 4) {
                                TextButton textButton = new TextButton("join", skin);
                                Label l = new Label(strings[i - 3] + "  " + strings[i - 2] + "  " + strings[i - 1] + "  " + strings[i], skin);
                                textButton.setName(strings[i - 3]);
                                textButton.addListener(new InputListener() {
                                    @Override
                                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                        lobbyBrowserScreenEvent.sendRequestJoinLobby(textButton.getName());
                                        try {
                                            TimeUnit.SECONDS.sleep(2); // TODO mit Thread realisieren
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if (lobbyBrowserScreenEvent.getLobbyInfo() != null) {
                                            game.setScreen(new LobbyScreen(game, skin, lobbyBrowserScreenEvent.getLobbyInfo()));//TODO does this work?
                                            stage.dispose();

                                        } else {
                                            Rumble.rumble(1f, .2f);

                                            errorWindow.setVisible(true);
                                            windowLobbyBrowser.setVisible(false);

                                            Label errorLabel = new Label("Can´t join lobby", skin);
                                            errorLabel.setFontScale(3f);


                                            okButton.addListener(new InputListener() {

                                                @Override
                                                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                                    errorWindow.setVisible(false);
                                                    windowLobbyBrowser.setVisible(true);
                                                }

                                                @Override
                                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                    return true;
                                                }
                                            });

                                            errorWindow.add(errorLabel);
                                            errorWindow.add(okButton);
                                            errorWindow.setPosition(stage.getWidth() / 2.75f, stage.getHeight() / 2f);
                                            errorWindow.pack();

                                            stage.addActor(errorWindow);

                                        }
                                    }

                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                                        return true;
                                    }
                                });
                                l.setFontScale(2f);
                                windowLobbyBrowser.add(l);
                                windowLobbyBrowser.add(textButton).row();
                            }
                        }
                    });
        });


        backButton(stage, skin, game, windowLobbyBrowser);
        packAndSetWindow(windowLobbyBrowser, stage);
        stage.addActor(windowLobbyBrowser);
        stage.setDebugAll(false);
    }

    public LobbyBrowserScreenEvent getLobbyBrowserScreenEvent() {
        return lobbyBrowserScreenEvent;
    }
}
