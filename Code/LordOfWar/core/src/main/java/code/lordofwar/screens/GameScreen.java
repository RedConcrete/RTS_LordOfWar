package code.lordofwar.screens;

import code.lordofwar.backend.Castle;
import code.lordofwar.backend.Pathfinding;
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameScreen extends Screens implements Screen {


    private static final ShapeRenderer debugRenderer = new ShapeRenderer();
    private ShapeRenderer rectangleRenderer;
    private static final ShapeRenderer debugMovement = new ShapeRenderer();

    private final Vector2 vectorSpeed;
    private final TiledMapTileLayer collisionUnitLayer;
    private final OrthographicCamera camera;
    private final boolean cameraDebug;
    private final boolean mapDebug;

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
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private Label villagerLabel;


    private final TmxMapLoader loader;
    private Label scoreLabel;
    private final GameScreenEvent gameScreenEvent;

    private final int startingVillager = 5;
    private int goldAmount = 100;
    private float pointTimerCounter;
    private Point2D.Float rectangleStart;//used to check where the select rectangle was started
    private Point2D.Float rectangleEnd;
    private float[] rectangleBounds;

    public GameScreen(LOW aGame, Skin aSkin, String lobbyID) {
        super(aGame, aSkin);
        mapDebug = true;
        isLeftPressed = false;
        isRightPressed = false;
        entityName = new Label("", skin);
        castleImage = new Image(new Sprite(new Texture("maps/RTS_CASTEL_TILES.png")));
        castleImage.setVisible(false);
        pointTimerCounter = 10;
        gameScreenEvent = new GameScreenEvent(game, lobbyID);
        posCameraDesired = new Vector3();
        cameraDebug = true;
        vectorSpeed = new Vector2();
        posCameraDesired = new Vector3();
        villagerArrayList = new ArrayList<>();
        soldierArrayList = new ArrayList<>();
        castle = new Castle();
        villagerLabel = new Label("", skin);
        rectangleRenderer = new ShapeRenderer();
        rectangleStart = null;//null bc rectangle was started
        rectangleEnd = null;
        rectangleBounds = new float[4];//0=originX1=originY2=width3=height
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
        stage.addListener(
                new InputListener() {
                    @Override
                    public void touchDragged(InputEvent event, float x, float y, int pointer) {
                        float[] coords = new float[]{x, y};
                        if (rectangleStart == null) {
                            rectangleStart = new Point2D.Float(coords[0], coords[1]);
                        }
                        rectangleEnd = new Point2D.Float(coords[0], coords[1]);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (rectangleStart != null && rectangleEnd != null) {
                            float[] recCoords = new float[]{rectangleBounds[0], rectangleBounds[1]};
                            float[] vilCoords;
                            for (Villager villager : villagerArrayList) {
                                vilCoords = translateXYCoordinatesToScreen(villager.getX() + villager.getWidth() / 2, villager.getY() + villager.getHeight() / 2);
                                if (vilCoords[0] >= recCoords[0] && vilCoords[1] >= recCoords[1]) {
                                    if (vilCoords[0] <= recCoords[0] + rectangleBounds[2] && vilCoords[1] <= recCoords[1] + rectangleBounds[3]) {
                                        villager.setSelected(true);
                                    }
                                }
                            }
                        }
                        rectangleEnd = null;
                        rectangleStart = null;
                    }
                }
        );
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
        debugMovement.setAutoShapeType(true);

        renderer.getBatch().begin();
        debugRenderer.begin();

        pointTimerCounter += delta;
        if (pointTimerCounter > 1) { //1 second update
            gameScreenEvent.sendPointRequest();//TODO move this to server???
            pointTimerCounter = 0;
        }

        scoreLabel.setText(gameScreenEvent.getPoints());
        villagerLabel.setText(castle.getVillager());

        for (Villager v : villagerArrayList) {
            v.draw(renderer.getBatch());

            if (v.isSelected()) {

                //todo braucht seine eigene forEach !!
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    if (!isRightPressed) {
                        getPathFinding(v);
                        isRightPressed = true;
                    } else {
                        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                            isRightPressed = false;
                        }
                    }
                }

                if (v.isSelected()) {
                    entityName.setText("Villager");
                    castleImage.setVisible(false);
                    Sprite s = new Sprite(uiAtlas.findRegion("button-normal"));
                    s.setColor(Color.RED);
                    s.setSize(v.getHp(), 10);
                    s.setPosition(v.getX() + 5, v.getY() + 60);
                    s.draw(renderer.getBatch());
                }

            }

            renderer.getBatch().end();
            if (rectangleStart != null && rectangleEnd != null) {
                //calc Rectangle
                //todo find a more efficient way to do this?
                rectangleBounds[0] = Math.min(rectangleStart.x, rectangleEnd.x);
                rectangleBounds[1] = Math.min(rectangleStart.y, rectangleEnd.y);
                rectangleBounds[2] = Math.max(rectangleStart.x, rectangleEnd.x) - rectangleBounds[0];
                rectangleBounds[3] = Math.max(rectangleStart.y, rectangleEnd.y) - rectangleBounds[1];
                //draw rectangle
                rectangleRenderer.setAutoShapeType(true);
                rectangleRenderer.begin();
                rectangleRenderer.set(ShapeRenderer.ShapeType.Line);
                rectangleRenderer.setColor(Color.WHITE);//white color for rect
                rectangleRenderer.rect(rectangleBounds[0], rectangleBounds[1], rectangleBounds[2], rectangleBounds[3]);//origin=lower left (screen)
                rectangleRenderer.end();
                //draw rectangle here
            }

            if (castle.isSelected()) {
                entityName.setText("Castle");
                castleImage.setVisible(true);
            }

            // todo schau wo die maus ist und dann reagiere also x und y abfragen und dann camera moven falls passend
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (!isLeftPressed) {
                    getClickedOnEntity();
                    isLeftPressed = true;
                } else {
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        isLeftPressed = false;
                    }
                }
            }

            if (mapDebug) {
                //todo zeigt zwar ein Grid aber ist nicht ganz richtig :D glaube ich !!
                for (int i = 0; i < 74; i++) {
                    Sprite lineH = new Sprite(uiAtlas.findRegion("line-h"));
                    Sprite lineV = new Sprite(uiAtlas.findRegion("line-v"));
                    lineV.setColor(Color.GREEN);
                    lineV.setSize(collisionUnitLayer.getHeight() * 64, 1);
                    lineV.setPosition(0, (i * 66) + (-1 * i));
                    lineV.draw(renderer.getBatch());
                    lineH.setColor(Color.GREEN);
                    lineH.setSize(1, collisionUnitLayer.getHeight() * 64);
                    lineH.setPosition((i * 66) + (-1 * i), 0);
                    lineH.draw(renderer.getBatch());
                }
            }

            renderer.getBatch().end();
            debugRenderer.end();
            mouseOnEdgeofCamera();
            stage.act();
            stage.draw();
        }

        @Override
        public void resize ( int width, int height){
            camera.position.set(stage.getWidth() / 2f, stage.getHeight() / 2f, 0);
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }

        @Override
        public void pause () {

        }

        @Override
        public void resume () {

        }

        @Override
        public void hide () {
            stage.clear();
        }

        @Override
        public void dispose () {
            stage.dispose();
            map.dispose();
            renderer.dispose();
        }

        private void setupUI () {

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

            Window resourceBarWindow = new Window("", skin);
            resourceBarWindow.setMovable(false);

            Window resourceWindow = new Window("", skin);
            resourceWindow.setMovable(false);
            resourceWindow.setSize(stage.getWidth(), stage.getHeight());

            Window exitWindow = new Window("", skin);
            exitWindow.setMovable(false);

            Window entityWindow = new Window("", skin);
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

            Label scoreTextLabel = new Label(" Your Score:", skin);


            scoreLabel = new Label("", skin);


            Label villagerTextLabel = new Label("Villager :", skin);

            Label goldLabel = new Label("0", skin);
            goldLabel.setText(goldAmount);

            Image goldImage = new Image(new Sprite(new Texture("ui/gold_treasure_icons_16x16/gold.png")));

            Image villagerImage = new Image(new Sprite(unitAtlas.findRegion("Character_Green_B")));

            resourceBarWindow.setMovable(false);

            exitWindow.setMovable(false);

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
                            stage.dispose(); //already disposed wenn einmal abgelehnt ??
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

            resourceBarWindow.add(scoreTextLabel).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
            resourceBarWindow.add(scoreLabel).padTop(30f).padBottom(10f).padRight(10f);
            resourceBarWindow.add(goldImage).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
            resourceBarWindow.add(goldLabel).padTop(30f).padBottom(10f).padRight(10f);
            resourceBarWindow.add(villagerImage).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
            resourceBarWindow.add(villagerLabel).padTop(30f).padBottom(10f).padRight(10f);

            resourceBarWindow.setPosition(0, stage.getHeight());

            exitWindow.add(exitGameButton).padTop(30f);
            exitWindow.setPosition(stage.getWidth(), stage.getHeight());

            entityWindow.add(entityName).row();
            entityWindow.add(castleImage);
            entityWindow.setPosition(0, 0);

            packWindow(resourceBarWindow, stage);
            packWindow(exitWindow, stage);
            packWindow(entityWindow, stage);

            stage.addActor(entityWindow);
            stage.addActor(resourceBarWindow);
            stage.addActor(exitWindow);

            stage.setDebugAll(false);
        }

        //Todo camera movement überarbeiten !!
        private void mouseOnEdgeofCamera () {

            float xClicked, yClicked;

            xClicked = Gdx.input.getX();
            yClicked = Gdx.input.getY();

            //Todo camera movement überarbeiten !!
            processCameraMovement(xClicked, yClicked);

            camera.position.lerp(posCameraDesired, 0.1f);

            if (cameraDebug) {
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
        private void processCameraMovement ( float xClicked, float yClicked){

            //oben links
            if (xClicked <= camera.viewportWidth * 2 / 32 && yClicked <= camera.viewportHeight * 1 / 18) {
                posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
                posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(0, camera.viewportHeight - camera.viewportHeight * 1 / 18, camera.viewportWidth * 2 / 32, camera.viewportHeight * 1 / 18);
                    debugMovement.end();
                }

                camera.update();
            }
            //mitte links
            else if (xClicked <= camera.viewportWidth * 2 / 32 && yClicked >= camera.viewportHeight * 1 / 18 && yClicked <= camera.viewportHeight * 17 / 18) {
                posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(0, camera.viewportHeight - camera.viewportHeight * 17 / 18, camera.viewportWidth * 2 / 32, camera.viewportHeight * 16 / 18);
                    debugMovement.end();
                }

                camera.update();
            }
            //unten links
            else if (xClicked <= camera.viewportWidth * 2 / 32 && yClicked >= camera.viewportHeight * 17 / 18 && yClicked <= camera.viewportHeight) {
                posCameraDesired.x -= CAMERASPEED * Gdx.graphics.getDeltaTime();
                posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(0, 0, camera.viewportWidth * 2 / 32, camera.viewportHeight * 1 / 18);
                    debugMovement.end();
                }

                camera.update();
            }

            //oben rechts
            else if (xClicked >= camera.viewportWidth * 30 / 32 && yClicked <= camera.viewportHeight * 1 / 18) {
                posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
                posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(camera.viewportWidth - camera.viewportWidth * 2 / 32, camera.viewportHeight - camera.viewportHeight * 1 / 18, camera.viewportWidth * 2 / 32, camera.viewportHeight * 1 / 18);
                    debugMovement.end();
                }

                camera.update();
            }
            //mitte rechts
            else if (xClicked >= camera.viewportWidth * 30 / 32 && yClicked >= camera.viewportHeight * 1 / 18 && yClicked <= camera.viewportHeight * 17 / 18) {
                posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(camera.viewportWidth - camera.viewportWidth * 2 / 32, camera.viewportHeight - camera.viewportHeight * 17 / 18, camera.viewportWidth * 2 / 32, camera.viewportHeight * 16 / 18);
                    debugMovement.end();
                }

                camera.update();
            }
            //unten rechts
        else if (xClicked >= camera.viewportWidth * 30 / 32 && yClicked >= camera.viewportHeight * 17 / 18) {
                posCameraDesired.x += CAMERASPEED * Gdx.graphics.getDeltaTime();
                posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(camera.viewportWidth - camera.viewportWidth * 2 / 32, 0, camera.viewportWidth * 2 / 32, camera.viewportHeight * 1 / 18);
                    debugMovement.end();
                }

                camera.update();
            }

            //mitte oben
            else if (xClicked >= camera.viewportWidth * 2 / 32 && xClicked <= camera.viewportWidth * 30 / 32 && yClicked <= camera.viewportHeight * 1 / 18) {
                posCameraDesired.y += CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(camera.viewportWidth - camera.viewportWidth * 30 / 32, camera.viewportHeight - camera.viewportHeight * 1 / 18, camera.viewportWidth * 28 / 32, camera.viewportHeight * 1 / 18);
                    debugMovement.end();
                }

                camera.update();
            }
            //mitte unten
            else if (xClicked >= camera.viewportWidth * 2 / 32 && xClicked <= camera.viewportWidth * 30 / 32 && yClicked >= camera.viewportHeight * 17 / 18) {
                posCameraDesired.y -= CAMERASPEED * Gdx.graphics.getDeltaTime();

                if (cameraDebug) {
                    debugMovement.begin();
                    debugMovement.rect(camera.viewportWidth - camera.viewportWidth * 30 / 32, 0, camera.viewportWidth * 28 / 32, camera.viewportHeight * 1 / 18);
                    debugMovement.end();
                }

                camera.update();
            } else {
                camera.update();
            }

        }

        public static void DrawDebugLine (Vector2 start, Vector2 end,int lineWidth, Color color, Matrix4
        projectionMatrix){
            Gdx.gl.glLineWidth(lineWidth);
            debugRenderer.setProjectionMatrix(projectionMatrix);
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.setColor(color);
            debugRenderer.line(start, end);
            debugRenderer.end();
            Gdx.gl.glLineWidth(1);
        }

        public GameScreenEvent getGameScreenEvent () {
            return gameScreenEvent;
        }

        public void getClickedOnEntity () {

            float[] coords = translateXYCoordinatesFromScreen(Gdx.input.getX(), Gdx.input.getY());
            for (Villager villager : villagerArrayList) {

                if (villager.getX() < (int) coords[0] && villager.getY() < (int) coords[1]) {
                    if (villager.getX() + villager.getWidth() >= (int) coords[0] && villager.getY() + villager.getHeight() >= (int) coords[1]) {
                        villager.setSelected(!villager.isSelected());
                    } else {
                        villager.setSelected(false);
                    }
                }
            }

            try {
                if (collisionUnitLayer.getCell((int) coords[0] / collisionUnitLayer.getTileWidth(), (int) coords[1] / collisionUnitLayer.getTileHeight())
                        .getTile().getProperties().containsKey("isCastel")) {
                    castle.setSelected(!castle.isSelected());
                }
            } catch (Exception e) {

            }
        }


        //[0]=x[1]=y
        private float[] translateXYCoordinatesFromScreen ( float x, float y){
            Vector3 mousePos = new Vector3(x, y, 0);
            camera.unproject(mousePos);

            return new float[]{mousePos.x, mousePos.y};
        }

        private float[] translateXYCoordinatesToScreen ( float x, float y){
            Vector3 mousePos = new Vector3(x, y, 0);
            camera.project(mousePos);

            return new float[]{mousePos.x, mousePos.y};
        }

        public void getPathFinding(Villager v){
            //todo pathfinding programmieren
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);

            int x = (int) mousePos.x, y = (int) mousePos.y;
            Pathfinding p = new Pathfinding(x,y,(int) v.getX() + 32,(int) v.getY() + 32, collisionUnitLayer);

        }
    }
