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

        lobbyErstellenButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("LobbyErstellen");
                //Todo Abfrage entwickeln!!!

                if(true){
                    game.setScreen(new LobbyCreateScreen(game, skin));
                }
                else{

                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        TextButton lobbyBeitrettenButton = new TextButton("Lobby beitretten",skin);
        lobbyBeitrettenButton.getLabel().setFontScale(3f);

        lobbyBeitrettenButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Lobby beitretten");

                //Todo Abfrage entwickeln!!!

                if(true){
                    game.setScreen(new LobbyBeitretenScreen(game, skin));
                }
                else{

                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        TextButton optionButton = new TextButton("Optionen",skin);
        optionButton.getLabel().setFontScale(3f);

        optionButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("OptionsScreen");

                //Todo Abfrage entwickeln!!!

                if(true){
                    game.setScreen(new OptionScreen(game, skin));
                }
                else{

                }
            }
            //was macht diese Methode??
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        TextButton ExitButton = new TextButton("Exit",skin);
        ExitButton.getLabel().setFontScale(3f);

        ExitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("OptionsScreen");

                TextButton yesButton = new TextButton("Yes",skin);
                yesButton.getLabel().setFontScale(2f);

                TextButton noButton = new TextButton("No",skin);
                noButton.getLabel().setFontScale(2f);

                Label exitLabel = new Label("do you realy want to exit?",skin);
                exitLabel.setFontScale(3f);

                Window windowExit = new Window("Exit?",skin, "border");

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


        Window windowMenu = new Window("", skin, "border");
        windowMenu.defaults().pad(20f);


        windowMenu.add(lobbyErstellenButton).row();
        windowMenu.add(lobbyBeitrettenButton).row();
        windowMenu.add(optionButton).row();
        windowMenu.add(ExitButton).row();
        windowMenu.pack();

        windowMenu.setPosition(stage.getWidth() / 2f - windowMenu.getWidth() / 2f,
                stage.getHeight() / 2f - windowMenu.getHeight() / 2f);
        windowMenu.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(windowMenu);


        stage.setDebugAll(true);
    }
}
