package code.lordofwar.screens;

import code.lordofwar.main.LOW;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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

    private void setupUI() {
        //TODO SHOW Score
        //syntax for data: i=username i+1=score; [0] and [1] are winner name and winner score
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
    public void dispose() {

    }
}
