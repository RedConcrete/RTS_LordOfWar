package code.lordofwar.screens;

import code.lordofwar.backend.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public abstract class Screens extends Constants{

    public Screens()  {


    }

    protected void createBackground(Stage stage){
        Texture texture = new Texture("assets\\ui\\background.jpg");
        Image image = new Image(texture);
        image.setSize(WORLD_WIDTH_PIXEL,WORLD_HEIGHT_PIXEL);
        stage.addActor(image);
    }

    protected void clearStage(){
        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        //es muss alles übermalt werden anders können angeblich die actors nicht entfernt werden :|
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT |  GL20.GL_DEPTH_BUFFER_BIT );
    }

    protected void fps(Stage stage, Skin skin){
        if(getFPS()){

            //Todo fps muss richtig ausgegeben werden (überschreibt sich selbst)
            Label fps = new Label("fps: "+ Gdx.graphics.getFramesPerSecond(),skin);
            fps.setFontScale(2f);
            fps.setX(Gdx.graphics.getWidth()*19f/20f);
            fps.setY(Gdx.graphics.getHeight()*19f/20f);
            stage.addActor(fps);
            fps.setText("fps: "+ Gdx.graphics.getFramesPerSecond());

            stage.getBatch().begin();
            Gdx.graphics.setTitle(" LordOfWar fps: "+Gdx.graphics.getFramesPerSecond());
            stage.getBatch().end();
        }
    }

    /*
    Creates an backButton and adds an InputListener to it. If Button pressed Game will return to MenuScreen.
    @author Robin Hefner
     */

    protected void backButton(Stage stage, Skin skin, Game game, Window window){
        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(3f);

        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                game.setScreen(new MenuScreen(game, skin));
                stage.dispose();

            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        window.add(backButton).row();

    }

    /*
    Packs the passed Window and sets its position.
    @author Robin Hefner
     */
    protected void packAndSetWindow(Window window, Stage stage){
        window.pack();

        window.setPosition(stage.getWidth() / 2f - window.getWidth() / 2f,
                stage.getHeight() / 2f - window.getHeight() / 2f);
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(.25f)));
        stage.addActor(window);
    }


}


