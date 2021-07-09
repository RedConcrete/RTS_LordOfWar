package code.lordofwar.screens;

import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * shows the score of all players after a finished game
 * @author Franz Klose
 */
public class GameScoreScreen extends Screens implements Screen {

    private String[] scoreData;


    public GameScoreScreen(LOW aGame, Skin aSkin, String[] data) {
        super(aGame, aSkin);
        //actual number of plyers,winner name, winner score, Ith player name,Ith player score,lobby max players, lobbyname, lobbymap,gamemode,own player order
        //this saved in score data
        //use the lobby data to create the new lobby screen

        scoreData = data;
        createBackground(stage);
        setupUI();

    }
    /**
     * builds the Ui for the Screen
     */
    private void setupUI() {
        Window windowGameScore = new Window("", skin, "border");
        Label label = new Label("Score",skin);
        windowGameScore.defaults().padLeft(20f).padRight(20f);
        windowGameScore.add(label).padBottom(10f).row();
        windowGameScore.add(new Label("Winner: " + scoreData[1] + "   |   Score: " + scoreData[2],skin)).padBottom(10f).row();
        for (int i = 3; i < Integer.parseInt(scoreData[0]) * 2 + 1; i+=2) {
            windowGameScore.add(new Label("Name: " + scoreData[i] + "   |   Score: " + scoreData[i+1],skin)).padBottom(10f).row();
        }
        backButton(stage,skin,game, windowGameScore,true);
        packAndSetWindow(windowGameScore,stage);

    }

    @Override
    public void show() {

    }

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
    public void hide() {

    }

    @Override
    public void dispose() { stage.dispose(); }
}
