package code.lordofwar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LobbyBrowserScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;


    public LobbyBrowserScreen(Game aGame, Skin aSkin) {
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
    public void hide() {stage.clear();}

    @Override
    public void dispose() {stage.dispose();}


    private void setupUI() {

        Window windowLobbyBrowser = new Window("", skin, "border");
        windowLobbyBrowser.defaults().pad(20f);

        backButton(stage,skin,game,windowLobbyBrowser);

        /*
        Array<Label> labelArray = new Array<>();
        labelArray.add(new Label("Test2",skin));

        Table table = new Table();

        final ScrollPane scroll = new ScrollPane(table, skin);
        //Todo Scrollpane funkt noch nicht ganz nach sehen wie es funkt

        scroll.setScrollingDisabled(true,false);
        scroll.setScrollBarPositions(false,true);
        table.pad(50f).defaults().space(4);

        for (int i = 1; i < 10; i++) {
            table.row();
            //Todo Lobby wird mit infos vom Server gefüllt
            Label label=new Label(i  + " 'Lobbyname' + 'Map' + '??' + 'CurrentConectedPlayers/AmountOfPlayers'  ", skin);
            //Todo buttons müssen in ein Array
            TextButton textButton = new TextButton("Beitreten",skin);

            label.setFontScale(2f);
            textButton.getLabel().setFontScale(2f);
            table.add(label).padRight(50f);
            table.add(textButton);
        }

        windowLobbyBrowser.add(scroll).row();
        windowLobbyBrowser.add(backButton).row();
        
         */
        windowLobbyBrowser.pack();


        windowLobbyBrowser.setPosition(stage.getWidth() / 2f - windowLobbyBrowser.getWidth() / 2f,
                stage.getHeight() / 2f - windowLobbyBrowser.getHeight() / 2f);
        windowLobbyBrowser.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(windowLobbyBrowser);


        stage.setDebugAll(true);
    }
}
