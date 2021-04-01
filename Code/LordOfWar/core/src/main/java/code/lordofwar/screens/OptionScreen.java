package code.lordofwar.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class OptionScreen extends Screens implements Screen {

    private final Stage stage;
    private final Game game;
    private final Skin skin;

    public OptionScreen(Game aGame, Skin aSkin) {
        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());

        createBackground(stage);

        setupUI();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT |  GL20.GL_DEPTH_BUFFER_BIT );

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

    private void setupUI() {


        Window windowOptionen = new Window("", skin, "border");
        Slider slider = new Slider(0f,100f,5f,false,skin);


        TextButton musikButton = new TextButton("Music ON / OFF", skin);
        musikButton.getLabel().setFontScale(3f);



        TextButton fpsButton = new TextButton("FPS OFF", skin);
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

                //Todo Abfrage entwickeln!!!
                if(!getFPS()){
                    fpsButton.setText("FPS ON");
                    setFPS(true);
                }
                else {
                    fpsButton.setText("FPS OFF");
                    setFPS(false);
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
        backButton(stage,skin,game,windowOptionen);
        packAndSetWindow(windowOptionen,stage);


        stage.setDebugAll(true);
    }
}