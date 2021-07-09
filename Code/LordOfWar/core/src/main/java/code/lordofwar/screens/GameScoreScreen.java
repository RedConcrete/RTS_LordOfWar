package code.lordofwar.screens;

import code.lordofwar.main.LOW;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScoreScreen extends Screens {

    private String[] scoreData;

    public GameScoreScreen(LOW aGame, Skin aSkin, String[] data) {
        super(aGame, aSkin);
        scoreData = data;
        createBackground(stage);
        setupUI();

    }

    private void setupUI() {
        //TODO SHOW Score
        //syntax for data: i=username i+1=score; [0] and [1] are winner name and winner score
    }
}
