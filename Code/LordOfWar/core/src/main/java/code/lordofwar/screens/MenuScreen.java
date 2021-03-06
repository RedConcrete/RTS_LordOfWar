package code.lordofwar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;

    public MenuScreen(Game aGame, Skin aSkin) {
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
        lordOfWarLabel.setY(Gdx.graphics.getHeight()*18/20);
        lordOfWarLabel.setWidth(Gdx.graphics.getWidth());
        stage.addActor(lordOfWarLabel);

        TextButton lobbyErstellenButton = new TextButton("Lobby erstellen",skin);
        lobbyErstellenButton.getLabel().setFontScale(3f);

        TextButton lobbyBeitrettenButton = new TextButton("Lobby beitretten",skin);
        lobbyBeitrettenButton.getLabel().setFontScale(3f);

        TextButton optionButton = new TextButton("Optionen",skin);
        optionButton.getLabel().setFontScale(3f);

        TextButton ExitButton = new TextButton("Exit",skin);
        ExitButton.getLabel().setFontScale(3f);

        Window windowMenu = new Window("", skin, "border");
        windowMenu.setMovable(false);
        windowMenu.defaults().pad(20f);

        Window windowExit = new Window("Exit?",skin, "border");
        windowExit.setMovable(false);
        windowExit.defaults().pad(20f);


        lobbyBeitrettenButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Lobby beitretten");
                game.setScreen(new LobbyBrowserScreen(game, skin));
                //Todo server muss daten von erstellten lobbys senden!

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        lobbyErstellenButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("LobbyErstellen");
                game.setScreen(new LobbyCreateScreen(game, skin));
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        optionButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("OptionsScreen");
                game.setScreen(new OptionScreen(game, skin));

            }
            //was macht diese Methode??
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        ExitButton.addListener(new InputListener(){
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
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
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
                    //was macht diese Methode??
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
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
            //was macht diese Methode??
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });


        windowMenu.add(lobbyErstellenButton).row();
        windowMenu.add(lobbyBeitrettenButton).row();
        windowMenu.add(optionButton).row();
        windowMenu.add(ExitButton).row();
        windowMenu.pack();

        windowMenu.setPosition(stage.getWidth() / 2f - windowMenu.getWidth() / 2f,
                stage.getHeight() / 2f - windowMenu.getHeight() / 2f);
        windowMenu.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.25f)));

        windowExit.setPosition(stage.getWidth() / 2f - windowExit.getWidth() / 2f,
                stage.getHeight() / 2f - windowExit.getHeight() / 2f);
        windowExit.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.25f)));

        stage.addActor(windowMenu);

        stage.setDebugAll(false);
    }
}
