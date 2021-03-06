package code.lordofwar.screens;

import code.lordofwar.backend.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class Screens extends Constants{

    public Screens()  {


    }

    protected void clearStage(){
        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        //es muss alles übermalt werden anders können aneblich die actors nicht entfernt werden :|
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT |  GL20.GL_DEPTH_BUFFER_BIT );
    }

    protected void fps(Stage stage, Skin skin){
        if(getFPS()){

            

            //Todo fps muss richtig ausgegeben werden (überschreibt sich selbst)
            Label fps = new Label("fps: "+ Gdx.graphics.getFramesPerSecond(),skin);
            fps.setFontScale(2f);
            fps.setX(Gdx.graphics.getWidth()*19/20);
            fps.setY(Gdx.graphics.getHeight()*19/20);
            stage.addActor(fps);
            fps.setText("fps: "+ Gdx.graphics.getFramesPerSecond());

            stage.getBatch().begin();
            Gdx.graphics.setTitle(" LordOfWar fps: "+Gdx.graphics.getFramesPerSecond());
            stage.getBatch().end();
        }
    }


}


