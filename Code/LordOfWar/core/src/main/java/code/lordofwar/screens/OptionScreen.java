package code.lordofwar.screens;


import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;



public class OptionScreen extends Screens implements Screen {


    public OptionScreen(LOW aGame, Skin aSkin) {
       super(aGame,aSkin);

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
        //todo dispose alles was erstellt wurde (Texturen)
        stage.dispose();
    }

    private void setupUI() {

        Window windowOptionen = new Window("", skin, "border");
        Slider slider = new Slider(0f,100f,5f,false,skin);

        TextButton musikButton = new TextButton("Music ON / OFF", skin);
        musikButton.getLabel().setFontScale(3f);

        TextButton fpsButton = new TextButton("FPS OFF", skin);
        fpsButton.getLabel().setFontScale(3f);

        TextButton fullscreenButton = new TextButton("fullscreen", skin);
        fpsButton.getLabel().setFontScale(3f);

        musikButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                //Todo musik muss an und aus gehen können

                if(getMUSIC()){
                    System.out.println("music off");
                    setMUSIC(false);
                }
                else{
                    System.out.println("music on");
                    setMUSIC(true);
                }

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        fpsButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                if(!game.getCon().getFPS()){
                    fpsButton.setText("FPS ON");
                    game.getCon().setFPS(true);
                }
                else {
                    fpsButton.setText("FPS OFF");
                    game.getCon().setFPS(false);
                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        fullscreenButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                System.out.println(fullscreenButton.getX() + STRINGSEPERATOR + fullscreenButton.getY());

                if(!Gdx.graphics.isFullscreen()){
                    Gdx.graphics.setFullscreenMode(currentMode);
                    stage.getViewport().setScreenWidth(currentMode.width);
                    stage.getViewport().setScreenHeight(currentMode.height);
                    System.out.println(fullscreenButton.getX() + STRINGSEPERATOR + fullscreenButton.getY());
                }
                else {
                    Gdx.graphics.setWindowedMode(WORLD_WIDTH_PIXEL,WORLD_HEIGHT_PIXEL);
                    stage.getViewport().setScreenWidth(WORLD_WIDTH_PIXEL);
                    stage.getViewport().setScreenHeight(WORLD_HEIGHT_PIXEL);
                }

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {  //Todo wird das wirklich benötigt ??
                return true;
            }
        });

        windowOptionen.defaults().pad(20f);

        windowOptionen.add(slider).row();
        windowOptionen.add(musikButton).row();
        windowOptionen.add(fpsButton).row();
        windowOptionen.add(fullscreenButton).row();

        backButton(stage,skin,game,windowOptionen);
        packAndSetWindow(windowOptionen,stage);

        stage.setDebugAll(false);
    }
}
