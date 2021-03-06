package code.lordofwar.screens;

import code.lordofwar.backend.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoginScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;


    public LoginScreen(Game aGame, Skin aSkin) {
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

        //Todo Background hinzufügen!


        clearStage();



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

        TextField usernameTextField = new TextField("Username",skin); //Todo wie kann man die Font größe ändern???

        Label password = new Label("Passwort", skin);
        password.setAlignment(Align.center);
        password.setFontScale(4f);
        password.setWidth(Gdx.graphics.getWidth());

        TextField passwordTextField = new TextField("Password",skin); //Todo wie kann man die Font größe ändern???
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);

        TextButton loginButton = new TextButton("Login!",skin);
        loginButton.getLabel().setFontScale(3f);
        loginButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Login");

                //Todo Loginabfrage entwickeln!!!

                if(true){

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

        Window window = new Window("", skin, "border");
        window.defaults().pad(10f);
        window.defaults().padLeft(40f);
        window.defaults().padRight(40f);
        window.setMovable(false);

        window.add(name).row();
        window.add(usernameTextField).row();
        window.add(password).row();
        window.add(passwordTextField).row();
        window.add(loginButton).row();

        window.pack();
        window.setPosition(stage.getWidth() / 2f - window.getWidth() / 2f,
                stage.getHeight() / 2f - window.getHeight() / 2f);
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.25f)));
        stage.addActor(window);

        stage.setDebugAll(false);
    }
}
