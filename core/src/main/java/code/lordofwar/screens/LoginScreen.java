package code.lordofwar.screens;

import code.lordofwar.backend.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoginScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;


    public LoginScreen(Game aGame, Skin aSkin) {
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

        //Todo Background hinzufügen!


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


        Label name = new Label("Username", skin);
        name.setAlignment(Align.center);
        name.setFontScale(4f);
        name.setWidth(Gdx.graphics.getWidth());
        //stage.addActor(name);

        TextField usernameTextField = new TextField("Username",skin); //Todo wie kann man die Font größe ändern???
        usernameTextField.setY(Gdx.graphics.getHeight()*12/20);
        //stage.addActor(usernameTextField);

        Label password = new Label("Passwort", skin);
        password.setAlignment(Align.center);
        password.setFontScale(4f);
        password.setWidth(Gdx.graphics.getWidth());
        //stage.addActor(password);

        TextField passwordTextField = new TextField("Password",skin); //Todo wie kann man die Font größe ändern???
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);
        //stage.addActor(passwordTextField);

        TextButton loginButton = new TextButton("Login!",skin);
        loginButton.getLabel().setFontScale(3f);
        loginButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("TEST__________");

                //Todo Abfrage entwickeln!!!

                if(true){

                    /*
                    for (Actor actor: stage.getActors()) {
                        Array<Actor> actorArrayList = new Array<>(); //Todo geht nicht warum ?
                        actorArrayList.add(actor);
                        stage.getActors().removeAll(actorArrayList,true);
                    }
                     */

                    game.setScreen(new MenuScreen(game, skin));
                    //removet alle actors von der stage
                    stage.dispose();

                }
                else{

                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });
        //stage.addActor(loginButton);

        Window window = new Window("", skin, "border");
        window.defaults().pad(10f);

        window.add(name).row();
        window.add(usernameTextField).row();
        window.add(password).row();
        window.add(passwordTextField).row();
        window.add(loginButton).row();

        window.pack();
        window.setPosition(stage.getWidth() / 2f - window.getWidth() / 2f,
                stage.getHeight() / 2f - window.getHeight() / 2f);
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(window);

        stage.setDebugAll(false);
    }
}
