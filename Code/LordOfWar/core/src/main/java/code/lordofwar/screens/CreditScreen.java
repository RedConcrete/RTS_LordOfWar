package code.lordofwar.screens;

import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class CreditScreen extends Screens implements Screen {

    public CreditScreen(LOW aGame, Skin aSkin) {
        super(aGame, aSkin);

        createBackground(stage);

        setupUI();
    }


    @Override
    public void show() { Gdx.input.setInputProcessor(stage); }

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
    public void hide() { stage.clear(); }

    @Override
    public void dispose() { stage.dispose(); }

    private void setupUI() {

        Window creditWindow = new Window("",skin,"border");

        creditWindow.defaults().pad(20f);

        backButton(stage, skin, game, creditWindow);
        packAndSetWindow(creditWindow, stage);

        stage.setDebugAll(false);



    }
}
