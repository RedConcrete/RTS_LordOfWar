package code.lordofwar.screens;

import code.lordofwar.backend.DataPacker;
import code.lordofwar.backend.Lobby;
import code.lordofwar.backend.MessageIdentifier;
import code.lordofwar.backend.Rumble;
import code.lordofwar.backend.events.LobbyCreateScreenEvent;
import code.lordofwar.backend.events.LobbyScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Select;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import okhttp3.WebSocket;

import java.util.ArrayList;
import java.util.Collections;

import java.util.concurrent.TimeUnit;

public class LobbyCreateScreen extends Screens implements Screen {

    private final Stage stage;
    private final LOW game;
    private final Skin skin;
    private final LobbyCreateScreenEvent lobbyCreateScreenEvent;

    public LobbyCreateScreen(LOW aGame, Skin aSkin) {
        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        lobbyCreateScreenEvent=new LobbyCreateScreenEvent(game);

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

        if (Rumble.getRumbleTimeLeft() > 0){
            Rumble.tick(Gdx.graphics.getDeltaTime());
            stage.getCamera().translate(Rumble.getPos());
        }

        fps(stage, skin);

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

        Window windowLobbyCreate = new Window("", skin, "border");
        windowLobbyCreate.defaults().pad(20f);
        windowLobbyCreate.setMovable(false);

        TextButton lobbyCreateButton = new TextButton("create Lobby", skin);
        lobbyCreateButton.getLabel().setFontScale(3f);

        TextField lobbyName = new TextField("", skin);

        Label lobbyNameLabel = new Label("Lobby name", skin);
        lobbyNameLabel.setFontScale(2f);

        Label lobbyPlayerAmountLabel = new Label("Player amount", skin);
        lobbyPlayerAmountLabel.setFontScale(2f);

        Label gameModeLabel = new Label("Gamemode", skin);
        gameModeLabel.setFontScale(2f);

        Label mapLabel = new Label("Map", skin);
        mapLabel.setFontScale(2f);

        SelectBox<Integer> playerAmountSelectBox = new SelectBox(skin);
        playerAmountSelectBox.setItems(2, 4, 6);

        SelectBox<String> gameModeSelectBox = new SelectBox(skin);
        gameModeSelectBox.setItems("Normal", "Expert");

        SelectBox<String> mapSelctBox = new SelectBox(skin);
        //todo maps müsseten noch überabretet werden
        mapSelctBox.setItems("map_1", "map_2");


        lobbyCreateButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //Todo Abfrage entwickeln!!!
                Lobby lobby = new Lobby(lobbyName.getText(), mapSelctBox.getSelected(), playerAmountSelectBox.getSelected(), gameModeSelectBox.getSelected());
//                ArrayList<String> lobbyArr = new ArrayList(Collections.singleton(lobby.getLobbyname() + lobby.getMap() + lobby.getPlayerAmount() + lobby.getGamemode()));
//                lobbyCreateScreenEvent.sendLobbyData(lobbyArr);


                lobbyCreateScreenEvent.sendLobbyCreateRequest(lobby);
                try {
                    TimeUnit.SECONDS.sleep(2); // todo schauen ob delay immer ausreicht!
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(lobbyCreateScreenEvent.isCreated()){
                    game.setScreen(new LobbyScreen(game, skin,lobbyCreateScreenEvent.getLobbyID()));
                }
                else{

                    Window w = new Window("", skin ,"border");
                    TextArea textArea = new TextArea("lobby konnte nicht erstellt werden!",skin);
                    w.add(textArea);
                    stage.addActor(w);

                    Rumble.rumble(1f, .2f);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        windowLobbyCreate.add(lobbyNameLabel);
        windowLobbyCreate.add(gameModeLabel).row();

        windowLobbyCreate.add(lobbyName);
        windowLobbyCreate.add(gameModeSelectBox).row();

        windowLobbyCreate.add(lobbyPlayerAmountLabel);
        windowLobbyCreate.add(mapLabel).row();

        windowLobbyCreate.add(playerAmountSelectBox);
        windowLobbyCreate.add(mapSelctBox).row();

        windowLobbyCreate.add(lobbyCreateButton);

        backButton(stage, skin, game, windowLobbyCreate);
        packAndSetWindow(windowLobbyCreate, stage);



        stage.setDebugAll(false);
    }

    public LobbyCreateScreenEvent getLobbyCreateScreenEvent() {
        return lobbyCreateScreenEvent;
    }
}
