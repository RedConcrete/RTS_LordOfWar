package code.lordofwar.screens;

import code.lordofwar.backend.Constants;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class Screens extends Constants {
    protected final Stage stage;
    protected final LOW game;
    protected final Skin skin;
    private Label fps;

    public Screens(LOW aGame, Skin aSkin) {
        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
    }

    protected void createBackground(Stage stage) {
        Texture texture = new Texture("ui\\background.jpg");
        Image image = new Image(texture);
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        if (Gdx.graphics.isFullscreen()) {
            image.setSize(currentMode.width, currentMode.height);
        } else {
            image.setSize(WORLD_WIDTH_PIXEL, WORLD_HEIGHT_PIXEL);
        }

        stage.addActor(image);
    }

    protected void clearStage() {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.getGL20().glViewport(0, 0, currentMode.width, currentMode.height);
        } else {
            Gdx.graphics.getGL20().glViewport(0, 0, WORLD_WIDTH_PIXEL, WORLD_HEIGHT_PIXEL);
        }

    }

    protected void fps(Stage stage, Skin skin) {
        if (game.getCon().getFPS()) {

            if (fps == null) {
                fps = new Label("fps: " + Gdx.graphics.getFramesPerSecond(), skin);
                fps.setFontScale(2f);
                fps.setX(Gdx.graphics.getWidth() * 19f / 20f);
                fps.setY(Gdx.graphics.getHeight() * 19f / 20f);
                stage.addActor(fps);

            } else {

                System.out.println(fps.getStage());
                fps.setText("fps: " + Gdx.graphics.getFramesPerSecond());
            }

        } else {
            if (fps != null) {
                fps.setText("");
            }
        }
    }

    /**
    Creates an backButton and adds an InputListener to it. If Button pressed Game will return to MenuScreen.
    @author Robin Hefner
     */

    protected void backButton(Stage stage, Skin skin, LOW game, Window window) {
        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(3f);

        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new MenuScreen(game, skin));
                stage.dispose();

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        window.add(backButton).row();

    }

    /**
    Packs the passed Window and sets its position.
    @author Robin Hefner
     */
    protected void packAndSetWindow(Window window, Stage stage) {
        window.pack();
        window.setPosition(stage.getWidth() / 2f - window.getWidth() / 2f,
                stage.getHeight() / 2f - window.getHeight() / 2f);
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(.25f)));
        stage.addActor(window);
    }

    protected void packWindow(Window window, Stage stage) {
        window.pack();
        stage.addActor(window);
    }
}


