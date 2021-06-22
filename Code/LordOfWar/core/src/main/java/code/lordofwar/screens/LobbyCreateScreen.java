package code.lordofwar.screens;


import code.lordofwar.backend.Lobby;
import code.lordofwar.backend.Rumble;
import code.lordofwar.backend.events.LobbyCreateScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class LobbyCreateScreen extends Screens implements Screen {


    private final LobbyCreateScreenEvent lobbyCreateScreenEvent;

    public LobbyCreateScreen(LOW aGame, Skin aSkin) {
        super(aGame, aSkin);

        lobbyCreateScreenEvent = new LobbyCreateScreenEvent(game);

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

        if (Rumble.getRumbleTimeLeft() > 0) {
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


        TextButton lobbyCreateButton = new TextButton("Create Lobby", skin);
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
        gameModeSelectBox.setItems(" Normal ", " Expert ");

        SelectBox<String> mapSelctBox = new SelectBox(skin);
        //todo maps müsseten noch überabretet werden
        mapSelctBox.setItems(" map_1 ", " map_2 ");


        lobbyCreateButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //Todo Abfrage entwickeln!!!

                if (lobbyName.getText().isEmpty()) {

                    Rumble.rumble(1f, .2f);

                    printErrorWindow(windowLobbyCreate, "Lobbys has no Name");


                } else {
                    Lobby lobby = new Lobby(lobbyName.getText(), mapSelctBox.getSelected(), playerAmountSelectBox.getSelected(), gameModeSelectBox.getSelected());
                    lobbyCreateScreenEvent.sendLobbyCreateRequest(lobby);

                    new Thread(() -> {
                        try {
                            Thread.sleep(game.getConstants().STANDARD_TIME_WAIT);//2 sec

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Gdx.app.postRunnable(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (lobbyCreateScreenEvent.isCreated()) {
                                            game.setScreen(new LobbyScreen(game, skin, new String[]{lobbyName.getText(), mapSelctBox.getSelected(), String.valueOf(playerAmountSelectBox.getSelected()), gameModeSelectBox.getSelected()}));

                                        } else {

                                            Rumble.rumble(1f, .2f);

                                            printErrorWindow(windowLobbyCreate, "Can´t create Lobby");
                                        }
                                    }
                                });
                    }).start();

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

    private void printErrorWindow(Window windowLobbyCreate, String text) {

        Window errorWindow = new Window("", skin, "border");
        errorWindow.setMovable(false);
        errorWindow.defaults().pad(20f);

        TextButton okButton = new TextButton("OK", skin);

        errorWindow.setVisible(true);
        windowLobbyCreate.setVisible(false);

        Label errorLabel = new Label(text, skin);
        errorLabel.setFontScale(3f);

        okButton.addListener(new InputListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                errorWindow.setVisible(false);
                windowLobbyCreate.setVisible(true);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        errorWindow.add(errorLabel).row();
        errorWindow.add(okButton).row();
        errorWindow.setPosition(stage.getWidth() / 2.75f, stage.getHeight() / 2f);
        errorWindow.pack();

        stage.addActor(errorWindow);

    }

    public LobbyCreateScreenEvent getLobbyCreateScreenEvent() {
        return lobbyCreateScreenEvent;
    }
}
