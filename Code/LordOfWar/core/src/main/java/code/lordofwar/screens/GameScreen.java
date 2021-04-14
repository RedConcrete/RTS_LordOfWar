package code.lordofwar.screens;

import code.lordofwar.backend.Castle;
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
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

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
    private Castle castle;
    private Image castleImage;
    private Label entityName;
    private TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("ui/skin/uiskin.atlas"));
    private TextureAtlas unitAtlas = new TextureAtlas(Gdx.files.internal("maps/RTS_UNITS_TILES.txt"));
    private boolean isPressed;

    private final TmxMapLoader loader;
    private Label scoreLabel;
    private final GameScreenEvent gameScreenEvent;

    private final int startingVillager = 5;
    private int goldAmount = 100;
    private float pointTimerCounter;

    public GameScreen(LOW aGame, Skin aSkin, String lobbyID) {
        super(aGame,aSkin);
        isPressed = false;
        entityName = new Label("", skin);
        castleImage = new Image(new Sprite(new Texture("maps/RTS_CASTEL_TILES.png")));
        castleImage.setVisible(false);
        pointTimerCounter = 10;
        gameScreenEvent = new GameScreenEvent(game, lobbyID);
        posCameraDesired = new Vector3();
        isCameraDebug = false;
        vectorSpeed = new Vector2();
        posCameraDesired = new Vector3();
        villagerArrayList = new ArrayList<>();
        soldierArrayList = new ArrayList<>();
        castle = new Castle();


        villiagerSprite = new Sprite(unitAtlas.findRegion("Character_Green_B"));

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

    }

    @Override
    public void render(float delta) {
        clearStage();

        fps(stage, skin);

        renderer.setView(camera);
        renderer.render();

        debugRenderer.setAutoShapeType(true);

        renderer.getBatch().begin();
        debugRenderer.begin();

        pointTimerCounter += delta;
        if (pointTimerCounter > 1) {//1 second update
            gameScreenEvent.sendPointRequest();//TODO move this to server???
            pointTimerCounter = 0;
        }

        scoreLabel.setText(gameScreenEvent.getPoints());
        for (Villager v : villagerArrayList) {
            if (v.isSelected()) {

                v.draw(renderer.getBatch());
            } else {

                v.draw(renderer.getBatch());
            }
            v.draw(renderer.getBatch());

            if(v.isSelected()){
                entityName.setText("Villager");
                castleImage.setVisible(false);
                Sprite s = new Sprite(uiAtlas.findRegion("button-normal"));
                s.setColor(Color.RED);
                s.setSize(v.getHp(),10);
                s.setPosition(v.getX() + 5,v.getY() + 60);
                s.draw(renderer.getBatch());
            }

        }

        renderer.getBatch().end();
        debugRenderer.end();

        if(castle.isSelected()){
            entityName.setText("Castle");
            castleImage.setVisible(true);
        }

        // todo schau wo die maus ist und dann reagiere also x und y abfragen und dann camera moven falls passend
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if(!isPressed){
                getClickedOnEntity();
                isPressed = true;
            }
            else {
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    isPressed = false;
                }
            }
        }

        mouseOnEdgeofCamera();
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

        Window scoreAndVilliagerWindow = new Window("", skin);
        scoreAndVilliagerWindow.setMovable(false);

        Window goldAndExitWindow = new Window("", skin);
        goldAndExitWindow.setMovable(false);

        Window entityWindow = new Window("",skin);
        entityWindow.setMovable(false);

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

        Image goldImage = new Image(new Sprite(new Texture("ui/gold_treasure_icons_16x16/gold.png")));

        scoreAndVilliagerWindow.setMovable(false);
        goldAndExitWindow.setMovable(false);

        windowExit.setMovable(false);
        windowExit.defaults().pad(20f);

        exitGameButton.setSize(exitGameButton.getWidth() * 5, exitGameButton.getHeight() * 5);
        exitGameButton.setPosition(stage.getWidth(), stage.getHeight());

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

        scoreAndVilliagerWindow.add(yourScoreLabel).padBottom(20f).padTop(30f).colspan(2).row();
        scoreAndVilliagerWindow.add(scoreLabel).colspan(2).row();
        scoreAndVilliagerWindow.add(addGoldButton);
        scoreAndVilliagerWindow.add(tackGoldButton);
        scoreAndVilliagerWindow.setPosition(0, stage.getHeight());
        scoreAndVilliagerWindow.setSize(stage.getWidth() * 1 / 10, stage.getHeight() * 3 / 25);

        goldAndExitWindow.add(goldLabel).padRight(20f).padLeft(20f).padTop(30f);
        goldAndExitWindow.add(goldImage).padRight(20f).padLeft(20f).padTop(30f);
        goldAndExitWindow.add(exitGameButton).padTop(30f);
        goldAndExitWindow.setPosition(stage.getWidth(), stage.getHeight());

        entityWindow.add(entityName).row();
        entityWindow.add(castleImage);
        entityWindow.setPosition(stage.getWidth() / 2,0);

        packWindow(scoreAndVilliagerWindow, stage);
        packWindow(goldAndExitWindow, stage);
        packWindow(entityWindow, stage);

        stage.addActor(entityWindow);
        stage.addActor(scoreAndVilliagerWindow);
        stage.addActor(goldAndExitWindow);

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

        int x = (int) mousePos.x, y = (int) mousePos.y;

        for (Villager villager : villagerArrayList) {

            if (villager.getX() < x && villager.getY() < y) {
                if (villager.getX() + villager.getWidth() >= x && villager.getY() + villager.getHeight() >= y) {
                    villager.setSelected(!villager.isSelected());
                }
            }

        }

        try {
            if(collisionUnitLayer.getCell(x / collisionUnitLayer.getTileWidth(), y / collisionUnitLayer.getTileHeight())
                    .getTile().getProperties().containsKey("isCastel")){
                castle.setSelected(!castle.isSelected());
            }
        } catch (Exception e) {

        }
    }
}
