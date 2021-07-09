package code.lordofwar.screens;

import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import javax.swing.*;

public class CreditScreen extends Screens implements Screen {

    /**
     *
     * @param aGame
     * @param aSkin
     */
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
        Label headline = new Label("Programmer",skin);
        headline.setFontScale(3f);
        Label franz = new Label("Franz Klose",skin);
        Label cem = new Label("Cem Arslan",skin);
        Label robin = new Label("Robin Hefner",skin);
        Label mehdy = new Label("Mehdy Abbas",skin);
        Label assetDesigner = new Label("Asset Designer",skin);
        assetDesigner.setFontScale(3f);
        Label glebster51 = new Label("glebster51 | OpenGameArt.org",skin);

        creditWindow.defaults().pad(20f);
        creditWindow.add(headline).row();
        creditWindow.add(franz).row();
        creditWindow.add(cem).row();
        creditWindow.add(robin).row();
        creditWindow.add(mehdy).row();
        creditWindow.add(assetDesigner).row();
        creditWindow.add(glebster51).row();


        backButton(stage, skin, game, creditWindow);
        packAndSetWindow(creditWindow, stage);

        stage.setDebugAll(false);
    }
}
