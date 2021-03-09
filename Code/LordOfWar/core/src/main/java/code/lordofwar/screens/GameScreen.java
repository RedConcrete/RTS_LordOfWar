package code.lordofwar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;

    public GameScreen(Game aGame, Skin aSkin) {

        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());

        setupUI();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        //es muss alles übermalt werden anders können aneblich die actors nicht entfernt werden :|
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

    private void setupUI(){


        //Todo https://www.youtube.com/watch?v=qik60F5I6J4&list=PLXY8okVWvwZ0qmqSBhOtqYRjzWtUCWylb <---------

        //Todo später inGame um tasten zu belegen!!
        /*
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btn1) {
             area.requestFocusInWindow();
            }
            if(e.getSource() == btn2) {
             fld.requestFocusInWindow();
            }
         }
         */

        Label label = new Label("GameScreen", skin);
        label.setFontScale(4f);
        label.setX(Gdx.graphics.getWidth()*7f/16f);
        label.setY(Gdx.graphics.getHeight()/2f);
        stage.addActor(label);

        Window gameWindow1 = new Window("",skin);
        Window gameWindow2 = new Window("",skin);
        Window gameWindow3 = new Window("",skin);

        gameWindow1.setMovable(false);
        gameWindow2.setMovable(false);
        gameWindow3.setMovable(false);

        gameWindow1.setPosition(0,0);
        gameWindow1.setSize(stage.getWidth() * 1/10,stage.getHeight());
        gameWindow2.setPosition(0,0);
        gameWindow2.setSize(stage.getWidth(),stage.getHeight() * 1/6);
        gameWindow3.setPosition(stage.getWidth() * 7/10,0);
        gameWindow3.setSize(stage.getWidth()* 2/10,stage.getHeight() * 2/6);

        stage.addActor(gameWindow1);
        stage.addActor(gameWindow2);
        stage.addActor(gameWindow3);

        stage.setDebugAll(false);
    }
}
