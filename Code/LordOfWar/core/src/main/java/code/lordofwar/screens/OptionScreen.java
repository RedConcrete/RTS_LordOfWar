package code.lordofwar.screens;


import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class OptionScreen extends Screens implements Screen {


    public OptionScreen(LOW aGame, Skin aSkin) {
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
        //todo dispose alles was erstellt wurde (Texturen)
        stage.dispose();
    }

    private void setupUI() {

        Window windowOptionen = new Window("", skin, "border");

        Label volumeLabel = new Label("Volume: " + game.getConstants().musicVolume + "%", skin);
        Slider volumeSlider = new Slider(0f, 100f, 1f, false, skin);
        volumeSlider.setValue(game.getConstants().musicVolume);

        String musicButtonText;
        if (game.getConstants().getMUSIC()) {
            musicButtonText = "Music off";
        } else {
            musicButtonText = "Music on";
        }
        TextButton musicButton = new TextButton(musicButtonText, skin);
        musicButton.getLabel().setFontScale(3f);

        String fpsButtonText;
        if (game.getConstants().getFPS()) {
            fpsButtonText = "FPS OFF";
        } else {
            fpsButtonText = "FPS ON";
        }
        TextButton fpsButton = new TextButton(fpsButtonText, skin);
        fpsButton.getLabel().setFontScale(3f);


        String fullscreenButtonText;
        if (Gdx.graphics.isFullscreen()) {
            fullscreenButtonText = "Fullscreen OFF";
        } else {
            fullscreenButtonText = "Fullscreen ON";
        }

        TextButton fullscreenButton = new TextButton(fullscreenButtonText, skin);
        fullscreenButton.getLabel().setFontScale(3f);

        volumeSlider.addListener(new ChangeListener() {
                                     @Override
                                     public void changed(ChangeEvent event, Actor actor) {
                                         game.getPlayer().setVolume((int) volumeSlider.getValue());
                                         volumeLabel.setText("Volume: " + (int) volumeSlider.getValue() + "%");
                                     }
                                 }
        );

        musicButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game.getConstants().getMUSIC()) {
                    musicButton.setText("Music on");
                    game.getConstants().setMUSIC(false);
                    game.getPlayer().mute();
                } else {
                    musicButton.setText("Music off");
                    game.getConstants().setMUSIC(true);
                    game.getPlayer().unmute();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        fpsButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if (!game.getConstants().getFPS()) {
                    fpsButton.setText("FPS OFF");
                    game.getConstants().setFPS(true);
                } else {
                    fpsButton.setText("FPS ON");
                    game.getConstants().setFPS(false);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        fullscreenButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                System.out.println(fullscreenButton.getX() + STRINGSEPERATOR + fullscreenButton.getY());

                if (!Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setFullscreenMode(currentMode);
                    stage.getViewport().setScreenWidth(currentMode.width);
                    stage.getViewport().setScreenHeight(currentMode.height);
                    System.out.println(fullscreenButton.getX() + STRINGSEPERATOR + fullscreenButton.getY());
                    fullscreenButton.setText("Fullscreen OFF");
                } else {
                    Gdx.graphics.setWindowedMode(WORLD_WIDTH_PIXEL, WORLD_HEIGHT_PIXEL);
                    stage.getViewport().setScreenWidth(WORLD_WIDTH_PIXEL);
                    stage.getViewport().setScreenHeight(WORLD_HEIGHT_PIXEL);
                    fullscreenButton.setText("Fullscreen ON");
                }

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        windowOptionen.defaults().pad(20f);

        windowOptionen.add(volumeLabel).row();
        windowOptionen.add(volumeSlider).row();
        windowOptionen.add(musicButton).row();
        windowOptionen.add(fpsButton).row();
        windowOptionen.add(fullscreenButton).row();

        backButton(stage, skin, game, windowOptionen);
        packAndSetWindow(windowOptionen, stage);

        stage.setDebugAll(false);
    }
}
