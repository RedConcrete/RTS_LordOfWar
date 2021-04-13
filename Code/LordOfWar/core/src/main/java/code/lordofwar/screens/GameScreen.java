package code.lordofwar.screens;

import code.lordofwar.backend.Soldier;
import code.lordofwar.backend.Villager;
import code.lordofwar.backend.events.GameScreenEvent;
import code.lordofwar.main.LOW;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen extends Screens implements Screen {


    private static final ShapeRenderer debugRenderer = new ShapeRenderer();

    private final Vector2 vectorSpeed;
    private final TiledMapTileLayer collisionUnitLayer;
    private final OrthographicCamera camera;
    private final boolean isCameraDebug;

    private Vector3 posCameraDesired;
    private final TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Soldier soldier;
    private final ArrayList<Villager> villagerArrayList;
    private final ArrayList<Soldier> soldierArrayList;
    private ArrayList<Object> entityArrayList;
    private final Sprite villiagerSprite;

    private final TmxMapLoader loader;
    private Label scoreLabel;
    private final GameScreenEvent gameScreenEvent;

    private final int startingVillager = 5;
    private int goldAmount = 100;
    private float pointTimerCounter;

    public GameScreen(LOW aGame, Skin aSkin, String lobbyID) {
        super(aGame,aSkin);
        pointTimerCounter = 10;
        gameScreenEvent = new GameScreenEvent(game, lobbyID);
        posCameraDesired = new Vector3();
        isCameraDebug = true;
        vectorSpeed = new Vector2();
        posCameraDesired = new Vector3();
        villagerArrayList = new ArrayList<>();
        soldierArrayList = new ArrayList<>();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("maps/RTS_UNITS_TILES.txt"));
        villiagerSprite = new Sprite(atlas.findRegion("Character_Green_B"));

        loader = new TmxMapLoader();
        map = loader.load("maps/map_1.tmx");

        camera = new OrthographicCamera();
        collisionUnitLayer = (TiledMapTileLayer) map.getLayers().get(1);

        setupUI();

    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        renderer = new OrthogonalTiledMapRenderer(map);

        for (int i = 0; i < startingVillager; i++) {
            Villager villager = new Villager(villiagerSprite, collisionUnitLayer);
            villagerArrayList.add(villager);
            villager.setPosition(villager.getCollisionLayer().getTileWidth(), i * villager.getCollisionLayer().getTileHeight());
            vectorSpeed.x = 100;
            villager.setVelocity(new Vector2(vectorSpeed));
        }

//        soldier = new Soldier(new Sprite(atlas.findRegion("Character_Green")), (TiledMapTileLayer) map.getLayers().get(0));
//        soldierArrayList.add(soldier);
//        soldier.setPosition(soldier.getCollisionLayer().getTileWidth(), 2 * soldier.getCollisionLayer().getTileHeight());
//        Vector2 vector2 = new Vector2();
//        vector2.y = -200;
//        soldier.setVelocity(vector2);
    }

    @Override
    public void render(float delta) {
        clearStage();

        fps(stage, skin);

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();

        pointTimerCounter += delta;
        if (pointTimerCounter > 1) {//1 second update
            gameScreenEvent.sendPointRequest();//TODO move this to server???
            pointTimerCounter = 0;
        }

        //debugRenderer.set(ShapeRenderer.ShapeType.Line);

        scoreLabel.setText(gameScreenEvent.getPoints());
        for (Villager v : villagerArrayList) {
            if (v.isSelected()) {
                v.setAlpha(.5f);
                v.draw(renderer.getBatch());
            } else {
                v.setAlpha(1);
                v.draw(renderer.getBatch());
            }
            v.draw(renderer.getBatch());

        }
        renderer.getBatch().end();

        // todo schau wo die maus ist und dann reagiere also x und y abfragen und dann camera moven falls passend
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            getClickedOnEntity();
            mouseOnEdgeofCamera();
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.position.set(width / 2f, height / 2f, 0);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        map.dispose();
        renderer.dispose();
    }

    private void setupUI() {

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

        Window gameWindow1 = new Window("", skin);
        gameWindow1.setMovable(false);

        Window gameWindow2 = new Window("", skin);
        gameWindow2.setMovable(false);

        Window windowExit = new Window("Exit?", skin, "border");
        windowExit.setMovable(false);
        windowExit.defaults().pad(20f);

        TextButton exitGameButton = new TextButton("Exit", skin);
        exitGameButton.getLabel().setFontScale(3f);

        TextButton addGoldButton = new TextButton("AddGold", skin);
        TextButton tackGoldButton = new TextButton("TackGold", skin);
        TextButton noButton = new TextButton("No", skin);
        TextButton yesButton = new TextButton("Yes", skin);

        Label exitLabel = new Label("Do you realy want to Exit?", skin);
        exitLabel.setFontScale(3f);

        Label yourScoreLabel = new Label(" Your Score ", skin);
        yourScoreLabel.setFontScale(2.5f);

        scoreLabel = new Label("", skin);
        scoreLabel.setFontScale(2.5f);

        Label goldLabel = new Label("0", skin);
        goldLabel.setText(goldAmount);
        Image image = new Image(new Sprite(new Texture("ui/gold_treasure_icons_16x16/gold.png")));

        addGoldButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                goldLabel.setText(goldAmount = goldAmount + 100);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        tackGoldButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                goldLabel.setText(goldAmount = goldAmount - 100);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        exitGameButton.setSize(exitGameButton.getWidth() * 5, exitGameButton.getHeight() * 5);
        exitGameButton.setPosition(stage.getWidth(), stage.getHeight());

        exitGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                windowExit.setVisible(true);


                yesButton.getLabel().setFontScale(2f);

                yesButton.addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        GameScreen.this.getGameScreenEvent().sendLeaveGameNotice();
                        game.setScreen(new MenuScreen(game, skin));
                        stage.dispose();
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

                noButton.getLabel().setFontScale(2f);

                noButton.addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        windowExit.setVisible(false);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
                windowExit.setPosition(stage.getWidth() / 2.75f, stage.getHeight() / 2f);
                windowExit.pack();
                stage.addActor(windowExit);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        windowExit.add(exitLabel).colspan(2).row();
        windowExit.add(yesButton);
        windowExit.add(noButton);

        gameWindow1.add(yourScoreLabel).padBottom(20f).padTop(20f).colspan(2).row();
        gameWindow1.add(scoreLabel).colspan(2).row();
        gameWindow1.add(addGoldButton);
        gameWindow1.add(tackGoldButton);
        gameWindow1.setPosition(0, stage.getHeight());
        gameWindow1.setSize(stage.getWidth() * 1 / 10, stage.getHeight() * 3 / 25);

        gameWindow2.add(goldLabel).padRight(20f).padLeft(20f);
        gameWindow2.add(image).padRight(20f).padLeft(20f);
        gameWindow2.add(exitGameButton);
        gameWindow2.setPosition(stage.getWidth(), stage.getHeight());

        packWindow(gameWindow1, stage);
        packWindow(gameWindow2, stage);

        stage.addActor(gameWindow1);
        stage.addActor(gameWindow2);
        stage.setDebugAll(false);
    }

    //Todo camera movement überarbeiten !!
    private void mouseOnEdgeofCamera() {

        float xClicked, yClicked;

        xClicked = Gdx.input.getX();
        yClicked = Gdx.input.getY();

        //Todo camera movement überarbeiten !!
        processCameraMovement(xClicked, yClicked);
        camera.position.lerp(posCameraDesired, 0.1f);


        if (isCameraDebug) {
            DrawDebugLine(new Vector2(camera.position.x, camera.position.y)
                    , new Vector2(posCameraDesired.x, posCameraDesired.y)
                    , 3, Color.RED, camera.combined);
        }
    }

    /**
     * The Method processCameraMovement moves the Camera in the direction the mouse is pointing at.
     *
     * @author Robin Hefner
     */
    private void processCameraMovement(float xClicked, float yClicked) {

        //oben links
        if (xClicked <= camera.viewportWidth * 3 / 16 && yClicked <= camera.viewportHeight * 2 / 9) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        //mitte links
        else if (xClicked <= camera.viewportWidth * 3 / 16 && yClicked >= camera.viewportHeight * 2 / 9 && yClicked <= camera.viewportHeight * 7 / 9) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        //unten links
        else if (xClicked <= camera.viewportWidth * 3 / 16 && yClicked >= camera.viewportHeight * 7 / 9 && yClicked <= camera.viewportHeight) {
            posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }

        //oben rechts
        else if (xClicked >= camera.viewportWidth * 13 / 16 && yClicked <= camera.viewportHeight * 2 / 9) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        //mitte rechts
        else if (xClicked >= camera.viewportWidth * 13 / 16 && yClicked >= camera.viewportHeight * 2 / 9 && yClicked <= camera.viewportHeight * 7 / 9) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        //unten rechts
        else if (xClicked >= camera.viewportWidth * 13 / 16 && yClicked >= camera.viewportHeight * 7 / 9) {
            posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }

        //mitte oben
        else if (xClicked >= camera.viewportWidth * 3 / 16 && xClicked <= camera.viewportWidth * 13 / 16 && yClicked <= camera.viewportHeight * 2 / 9) {
            posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        }
        //mitte unten
        else if (xClicked >= camera.viewportWidth * 3 / 16 && xClicked <= camera.viewportWidth * 13 / 16 && yClicked >= camera.viewportHeight * 7 / 9) {
            posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();
            camera.update();
        } else {
            camera.update();
        }

    }

    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix) {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public GameScreenEvent getGameScreenEvent() {
        return gameScreenEvent;
    }

    public void getClickedOnEntity() {

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        float x = mousePos.x, y = mousePos.y;

        for (Villager villager : villagerArrayList) {

            if (villager.getX() < x && villager.getY() < y) {
                if (villager.getX() + villager.getWidth() >= x && villager.getY() + villager.getHeight() >= y) {
                    villager.setSelected(!villager.isSelected());
                }
            }
        }
    }
}
