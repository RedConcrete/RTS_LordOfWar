package code.lordofwar.screens;


import code.lordofwar.backend.events.LoginScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginScreen extends Screens implements Screen {

    private final Stage stage;
    private final LOW game;
    private final Skin skin;
    LoginScreenEvent loginScreenEvent;


    public LoginScreen(LOW aGame, Skin aSkin) {
        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        loginScreenEvent = new LoginScreenEvent(game);

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
                ArrayList<String> loginArray= new ArrayList<>();
                loginArray.add(usernameTextField.getText());
                loginArray.add(passwordTextField.getText());

                loginScreenEvent.sendUserData(loginArray);

                try {
                    TimeUnit.SECONDS.sleep(1); // todo schauen ob delay immer ausreicht!
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(loginScreenEvent.isLoginAnswer()){

                    game.setScreen(new MenuScreen(game, skin));
                    //removet alle actors von der stage
                    stage.dispose();

                }
                else{
                    //todo zeige fehler an!!! in cooler Arte und Weise
                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        Label noAccountLabel = new Label("No Account ? Register here",skin);
        noAccountLabel.setAlignment(Align.center);
        noAccountLabel.setFontScale(1f);
        noAccountLabel.setWidth(Gdx.graphics.getWidth());



        TextButton registerButton = new TextButton("Register",skin);
        registerButton.getLabel().setFontScale(3f);
        registerButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Register");
                    game.setScreen(new RegisterScreen(game, skin));
                    stage.dispose();
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
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
        window.add(noAccountLabel).row();
        window.add(registerButton).row();
        packAndSetWindow(window,stage);

        stage.setDebugAll(false);
    }

    public LoginScreenEvent getLoginScreenEvent() {
        return loginScreenEvent;
    }
}