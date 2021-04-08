package code.lordofwar.screens;

import code.lordofwar.backend.Constants;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen extends Screens implements Screen {

    private final Stage stage;
    private final LOW game;
    private final Skin skin;

    public MenuScreen(LOW aGame, Skin aSkin) {
        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());


        fps(stage,skin);
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
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void setupUI(){

        Label lordOfWarLabel = new Label("Lord of War", skin);
        lordOfWarLabel.setAlignment(Align.center);
        lordOfWarLabel.setFontScale(6f);
        lordOfWarLabel.setY(Gdx.graphics.getHeight()*18f/20f);
        lordOfWarLabel.setWidth(Gdx.graphics.getWidth());
        stage.addActor(lordOfWarLabel);

        TextButton lobbyCreateButton = new TextButton("Lobby erstellen",skin);
        lobbyCreateButton.getLabel().setFontScale(3f);

        TextButton lobbyJoinButton = new TextButton("Lobby beitreten",skin);
        lobbyJoinButton.getLabel().setFontScale(3f);

        TextButton optionButton = new TextButton("Optionen",skin);
        optionButton.getLabel().setFontScale(3f);

        TextButton exitButton = new TextButton("Exit",skin);
        exitButton.getLabel().setFontScale(3f);

        Window windowMenu = new Window("", skin, "border");
        windowMenu.setMovable(false);
        windowMenu.defaults().pad(20f);

        Window windowExit = new Window("Exit?",skin, "border");
        windowExit.setMovable(false);
        windowExit.defaults().pad(20f);


        lobbyJoinButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Lobby beitreten");
                game.setScreen(new LobbyBrowserScreen(game, skin));
                //Todo server muss daten von erstellten lobbys senden!

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        lobbyCreateButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("LobbyErstellen");
                game.setScreen(new LobbyCreateScreen(game, skin));
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        optionButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("OptionsScreen");
                game.setScreen(new OptionScreen(game, skin));

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {


                System.out.println("ExitWindow");
                windowMenu.setVisible(false);
                windowExit.setVisible(true);

                TextButton yesButton = new TextButton("Yes",skin);
                yesButton.getLabel().setFontScale(2f);

                yesButton.addListener(new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        System.exit(0);
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });


                TextButton noButton = new TextButton("No",skin);
                noButton.getLabel().setFontScale(2f);

                noButton.addListener(new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        windowMenu.setVisible(true);
                        windowExit.setVisible(false);
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });


                Label exitLabel = new Label("Do you realy want to Exit?",skin);
                exitLabel.setFontScale(3f);



                windowExit.add(exitLabel).row();
                windowExit.add(yesButton);
                windowExit.add(noButton);
                windowExit.pack();

                stage.addActor(windowExit);

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        windowMenu.add(lobbyCreateButton).row();
        windowMenu.add(lobbyJoinButton).row();
        windowMenu.add(optionButton).row();
        windowMenu.add(exitButton).row();
        windowMenu.pack();

        packAndSetWindow(windowMenu,stage);
        stage.addActor(windowMenu);

        stage.setDebugAll(false);
    }
}
