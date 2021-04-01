package code.lordofwar.screens;

import code.lordofwar.backend.events.RegisterScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/*
The class Register Screen allows the User to create a Account, that he can use the Login the next time.
@author Robin Hefner
 */
public class RegisterScreen extends Screens implements Screen {

    private final Stage stage;
    private final LOW game;
    private final Skin skin;
    private final RegisterScreenEvent registerScreenEvent;

    public RegisterScreen(LOW aGame, Skin aSkin) {

        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        registerScreenEvent = new RegisterScreenEvent(game);
        createBackground(stage);
        setupUI();
    }

    private void setupUI() {

        Label usernameLabel = new Label("Username", skin);
        usernameLabel.setAlignment(Align.center);
        usernameLabel.setFontScale(4f);
        usernameLabel.setWidth(Gdx.graphics.getWidth());


        TextField usernameTextField = new TextField("Username", skin);

        Label passwordLabel = new Label("Password", skin);
        passwordLabel.setAlignment(Align.center);
        passwordLabel.setFontScale(4f);
        passwordLabel.setWidth(Gdx.graphics.getWidth());


        TextField passwordTextField = new TextField("Password", skin);
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);

        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(3f);
        registerButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                System.out.println("Register");
                //TODO Get rid of code copying
                ArrayList<String> registerArray = new ArrayList<>();
                registerArray.add(usernameTextField.getText());
                registerArray.add(passwordTextField.getText());

                registerScreenEvent.sendUserData(registerArray);

                try {
                    TimeUnit.SECONDS.sleep(2); // todo schauen ob delay immer ausreicht!
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (registerScreenEvent.isRegisterAnswer()) {
                    //TODO FEEDBACK GEBEN

                    game.setScreen(new LoginScreen(game, skin));//CHANGED TO loginscreen

                    stage.dispose();

                } else {
                    //TODO FEHLER ANZEIGEN
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(3f);

        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new LoginScreen(game, skin));
                stage.dispose();

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        Window registerWindow = new Window("", skin, "border");
        registerWindow.defaults().pad(10f);
        registerWindow.defaults().padLeft(40f);
        registerWindow.defaults().padRight(40f);
        registerWindow.setMovable(false);
        registerWindow.add(usernameLabel).row();
        registerWindow.add(usernameTextField).row();
        registerWindow.add(passwordLabel).row();
        registerWindow.add(passwordTextField).row();
        registerWindow.add(registerButton).row();
        registerWindow.add(backButton).row();
        packAndSetWindow(registerWindow, stage);

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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

    public RegisterScreenEvent getRegisterScreenEvent() {
        return registerScreenEvent;
    }
}
