package code.lordofwar.screens;

import code.lordofwar.backend.Rumble;
import code.lordofwar.backend.events.LoginScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class LoginScreen extends Screens implements Screen {


    LoginScreenEvent loginScreenEvent;
    Image img;
    Image img2;
    String swordTexturePath = "ui/swords/sword_1.png";


    public LoginScreen(LOW aGame, Skin aSkin) {
        super(aGame, aSkin);
        loginScreenEvent = new LoginScreenEvent(game);

        img = new Image(new Texture(swordTexturePath));

        img2 = new Image(new Texture(swordTexturePath));

        fps(stage, skin);
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

        if (game.getConstants().getWEBSOCKET_OPEN()) {
            img.setColor(Color.GREEN);
            img2.setColor(Color.GREEN);
        } else {
            img.setColor(Color.RED);
            img2.setColor(Color.RED);
        }

        if (Rumble.getRumbleTimeLeft() > 0) {
            Rumble.tick(Gdx.graphics.getDeltaTime());
            stage.getCamera().translate(Rumble.getPos());
        }

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

        Label name = new Label("Username", skin);
        name.setAlignment(Align.center);
        name.setFontScale(4f);
        name.setWidth(Gdx.graphics.getWidth());

        TextField usernameTextField = new TextField("Username", skin); //Todo wie kann man die Font größe ändern???

        Label password = new Label("Passwort", skin);
        password.setAlignment(Align.center);
        password.setFontScale(4f);
        password.setWidth(Gdx.graphics.getWidth());

        TextField passwordTextField = new TextField("Password", skin); //Todo wie kann man die Font größe ändern???
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);

        TextButton loginButton = new TextButton("Login!", skin);
        loginButton.getLabel().setFontScale(3f);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(3f);

        Window windowLogin = new Window("", skin, "border");
        windowLogin.defaults().pad(10f);
        windowLogin.defaults().padLeft(40f);
        windowLogin.defaults().padRight(40f);
        windowLogin.setMovable(false);

        Window errorWindow = new Window("", skin, "border");
        errorWindow.setMovable(false);
        errorWindow.defaults().pad(20f);
        Label errorLabel = new Label("Failed to login. Try again", skin);
        TextButton okButton = new TextButton("OK", skin);
        errorWindow.add(errorLabel).row();
        errorWindow.add(okButton).row();
        errorWindow.setPosition(stage.getWidth() / 2.75f, stage.getHeight() / 2f);
        loginButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ArrayList<String> loginArray = new ArrayList<>();
                loginArray.add(game.getSessionID());
                loginArray.add(usernameTextField.getText());
                loginArray.add(passwordTextField.getText());

                loginScreenEvent.sendUserData(loginArray);

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
                                    if (loginScreenEvent.isLoginAnswer()) {
                                        game.setScreen(new MenuScreen(game, skin));
                                        stage.dispose();

                                    } else {
                                        Rumble.rumble(1f, .2f);

                                        errorWindow.setVisible(true);
                                        windowLogin.setVisible(false);
                                        errorLabel.setFontScale(3f);


                                        okButton.addListener(new InputListener() {

                                            @Override
                                            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                                errorWindow.setVisible(false);
                                                windowLogin.setVisible(true);
                                            }

                                            @Override
                                            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                return true;
                                            }
                                        });
                                        errorWindow.pack();
                                        stage.addActor(errorWindow);
                                    }
                                }
                            }
                    );

                }).start();

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        Label noAccountLabel = new Label("No Account ? Register here", skin);
        noAccountLabel.setAlignment(Align.center);
        noAccountLabel.setFontScale(1f);
        noAccountLabel.setWidth(Gdx.graphics.getWidth());


        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(3f);
        registerButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new RegisterScreen(game, skin));
                stage.dispose();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.exit(0);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        /**
         * checks if the Websocket-connection is open and shows it with a red / green sword
         */
        windowLogin.add(name).colspan(3).row();
        windowLogin.add(usernameTextField).colspan(3).row();
        windowLogin.add(password).colspan(3).row();
        windowLogin.add(passwordTextField).colspan(3).row();
        windowLogin.add(img);
        windowLogin.add(loginButton);
        windowLogin.add(img2).row();
        windowLogin.add(noAccountLabel).colspan(3).row();
        windowLogin.add(registerButton).colspan(3).row();
        windowLogin.add(exitButton).colspan(3).row();
        packAndSetWindow(windowLogin, stage);
        stage.setDebugAll(false);
    }

    public LoginScreenEvent getLoginScreenEvent() {
        return loginScreenEvent;
    }
}
