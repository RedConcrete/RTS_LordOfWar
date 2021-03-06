package code.lordofwar.screens;

import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

/**
 * The MenuScreen that shows the MenuScreen
 *
 * @author Franz Klose,Cem Arslan
 */
public class MenuScreen extends Screens implements Screen {

    public MenuScreen(LOW aGame, Skin aSkin) {
        super(aGame, aSkin);

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

        Label lordOfWarLabel = new Label("Lord of War", skin);
        lordOfWarLabel.setAlignment(Align.center);
        lordOfWarLabel.setFontScale(6f);
        lordOfWarLabel.setY(Gdx.graphics.getHeight() * 18f / 20f);
        lordOfWarLabel.setWidth(Gdx.graphics.getWidth());
        stage.addActor(lordOfWarLabel);

        TextButton lobbyCreateButton = new TextButton("Create Lobby", skin);
        lobbyCreateButton.getLabel().setFontScale(3f);

        TextButton lobbyJoinButton = new TextButton("Join Lobby", skin);
        lobbyJoinButton.getLabel().setFontScale(3f);

        TextButton optionButton = new TextButton("Options", skin);
        optionButton.getLabel().setFontScale(3f);

        TextButton creditsButton = new TextButton("Credits",skin);
        creditsButton.getLabel().setFontScale(3f);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(3f);

        Window windowMenu = new Window("", skin, "border");
        windowMenu.setMovable(false);
        windowMenu.defaults().pad(20f);
        TextButton noButton = new TextButton("No", skin);
        noButton.getLabel().setFontScale(2f);

        Window windowExit = new Window("Exit?", skin, "border");
        windowExit.setMovable(false);
        windowExit.defaults().pad(20f);

        TextButton yesButton = new TextButton("Yes", skin);
        yesButton.getLabel().setFontScale(2f);

        Label exitLabel = new Label("Do you really want to Exit?", skin);
        exitLabel.setFontScale(3f);

        lobbyJoinButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LobbyBrowserScreen(game, skin));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        lobbyCreateButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LobbyCreateScreen(game, skin));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        optionButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new OptionScreen(game, skin));

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        creditsButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new CreditScreen(game,skin));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });



        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addActor(windowExit);
                windowMenu.setVisible(false);
                windowExit.setVisible(true);

                yesButton.addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        System.exit(0);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
                noButton.addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        windowMenu.setVisible(true);
                        windowExit.setVisible(false);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        windowExit.add(exitLabel).colspan(2).row();
        windowExit.add(yesButton);
        windowExit.add(noButton);
        windowExit.setPosition(stage.getWidth() / 2.75f, stage.getHeight() / 2f);
        windowExit.pack();



        windowMenu.add(lobbyCreateButton).row();
        windowMenu.add(lobbyJoinButton).row();
        windowMenu.add(optionButton).row();
        windowMenu.add(creditsButton).row();
        windowMenu.add(exitButton).row();
        windowMenu.pack();

        packAndSetWindow(windowMenu, stage);
        stage.addActor(windowMenu);

        stage.setDebugAll(false);
    }
}
