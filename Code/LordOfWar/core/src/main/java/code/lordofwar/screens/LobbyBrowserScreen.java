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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * The LobbyBrowserScreen that shows the lobbybrowser and defines it
 *
 * @author Franz Klose,Cem Arslan
 */
public class LobbyBrowserScreen extends Screens implements Screen {


    private LobbyBrowserScreenEvent lobbyBrowserScreenEvent;

    public LobbyBrowserScreen(LOW aGame, Skin aSkin) {
        super(aGame, aSkin);
        lobbyBrowserScreenEvent = new LobbyBrowserScreenEvent(game);

        createBackground(stage);
        lobbyBrowserScreenEvent.sendRequestGetLobbys();

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

    /**
     * builds the Ui for the Screen
     */
    private void setupUI() {

        Window windowLobbyBrowser = new Window("", skin, "border");
        windowLobbyBrowser.defaults().pad(20f);

        Window errorWindow = new Window("", skin, "border");
        errorWindow.setMovable(false);
        errorWindow.defaults().pad(20f);

        TextButton okButton = new TextButton("OK", skin);

        new Thread(() -> {
            try {
                Thread.sleep(STANDARD_TIME_WAIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Gdx.app.postRunnable(
                    () -> {
                        String[] strings = lobbyBrowserScreenEvent.getLobbyList();
                        for (int i = 4; i < strings.length; i += 4) {
                            TextButton joinButton = new TextButton("join", skin);
                            Label l = new Label("Lobbyname: " +strings[i - 3] + " Map: " + strings[i - 2] + " Mode: " + strings[i - 1] + " Player: " + strings[i], skin);
                            joinButton.setName(strings[i - 3]);
                            joinButton.padLeft(20f).padRight(100f);

                            joinButton.addListener(new InputListener() {
                                @Override
                                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                    lobbyBrowserScreenEvent.sendRequestJoinLobby(joinButton.getName());
                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(STANDARD_TIME_WAIT);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        Gdx.app.postRunnable(
                                                () -> {
                                                    if (lobbyBrowserScreenEvent.getLobbyInfo() != null) {
                                                        game.setScreen(new LobbyScreen(game, skin, lobbyBrowserScreenEvent.getLobbyInfo(),false));
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
                                                        errorWindow.setPosition(stage.getWidth() * 1 / 4, stage.getHeight() * 1 / 3);
                                                        errorWindow.pack();

                                                        stage.addActor(errorWindow);

                                                    }
                                                });
                                    }).start();
                                }

                                @Override
                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                    return true;
                                }
                            });
                            l.setFontScale(2f);
                            windowLobbyBrowser.add(l);
                            windowLobbyBrowser.add(joinButton).row();
                        }
                    });
        }).start();

        windowLobbyBrowser.setSize(1000, 600);
        windowLobbyBrowser.setMovable(false);
        windowLobbyBrowser.setPosition(stage.getWidth() * 1 / 4, stage.getHeight() * 1 / 3);
        backButton(stage, skin, game, windowLobbyBrowser,true);
        //packAndSetWindow(windowLobbyBrowser, stage);
        stage.addActor(windowLobbyBrowser);
        stage.setDebugAll(false);
    }


    public LobbyBrowserScreenEvent getLobbyBrowserScreenEvent() {
        return lobbyBrowserScreenEvent;
    }
}
