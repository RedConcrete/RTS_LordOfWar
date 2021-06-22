package code.lordofwar.screens;

import code.lordofwar.backend.*;
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
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GameScreen extends Screens implements Screen {

    private static final ShapeRenderer debugRenderer = new ShapeRenderer();
    private static final ShapeRenderer debugMovement = new ShapeRenderer();

    private ShapeRenderer rectangleRenderer;

    private final Vector2 vectorSpeed;
    private final TiledMapTileLayer collisionUnitLayer;
    private final OrthographicCamera camera;
    private final boolean cameraDebug;
    private final boolean mapDebug;

    private TextButton buttonRekrut;

    private boolean knowTheWay = false;
    private LinkedList<PathCell> theKnowenWay;
    private Vector3 posCameraDesired;
    private final TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Soldier soldier;

    private Label entityHp;
    private Label entityATK;
    private Label entityDEF;


    private final ArrayList<Soldier> soldierArrayList;
    private final ArrayList<Castle> castleArrayList;
    private ArrayList<Object> entityArrayList;

    private Sprite soldierSprite;
    private Sprite castleSprite;

    private Label entityName;

    private TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("ui/skin/uiskin.atlas"));
    private TextureAtlas unitAtlas = new TextureAtlas(Gdx.files.internal("maps/RTS_UNITS_TILES.txt"));
    private TextureAtlas mapAtlas = new TextureAtlas(Gdx.files.internal("maps/RTSSimple.txt"));

    private boolean isLeftPressed;
    private boolean isRightPressed;
    private Castle myCastle;

    private Label villagerLabel;
    private Label goldLabel;
    private final TmxMapLoader loader;
    private Label scoreLabel;
    private final GameScreenEvent gameScreenEvent;

    //todo muss von lobbyÜbergebe werden
    private final int startingCastle = 1;
    private int goldAmount = 100;

    private float pointTimerCounter;
    private Point2D.Float rectangleStart;//used to check where the select rectangle was started
    private Point2D.Float rectangleEnd;
    private float[] rectangleBounds;

    Image castleImage;
    Image soldierImage;

    public GameScreen(LOW aGame, Skin aSkin, String lobbyID, int startingPosition) {
        super(aGame, aSkin);
        mapDebug = false;
        isLeftPressed = false;
        isRightPressed = false;
        entityName = new Label("", skin);
        castleSprite = new Sprite(mapAtlas.findRegion("Castle"));
        pointTimerCounter = 10;
        gameScreenEvent = new GameScreenEvent(game, lobbyID);
        posCameraDesired = new Vector3();
        cameraDebug = true;
        vectorSpeed = new Vector2();
        posCameraDesired = new Vector3();
        soldierArrayList = new ArrayList<>();
        castleArrayList = new ArrayList<>();

        villagerLabel = new Label("", skin);
        goldLabel = new Label("",skin);
        entityHp = new Label("", skin);

        rectangleRenderer = new ShapeRenderer();
        rectangleStart = null;//null bc rectangle was started
        rectangleEnd = null;
        rectangleBounds = new float[4];//0=originX1=originY2=width3=height
        soldierSprite = new Sprite(unitAtlas.findRegion("Character_Green_B"));
        loader = new TmxMapLoader();

        String mapPath = "maps/map_1.tmx";
        map = loader.load(mapPath);
        //TODO way to tell maps apart
        float[] castlePosition;
        switch (startingPosition) {
            case 1:
                castlePosition = Constants.MAP1CC1;
                break;
            case 2:
                castlePosition = Constants.MAP1CC2;
                break;
            case 3:
                castlePosition = Constants.MAP1CC3;
                break;
            case 4:
                castlePosition = Constants.MAP1CC4;
                break;
            case 5:
                castlePosition = Constants.MAP1CC5;
                break;
            case 6:
                castlePosition = Constants.MAP1CC6;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + startingPosition);//max of 6 players; thus error
        }
        camera = new OrthographicCamera();
        //TODO why doesnt this work
        posCameraDesired.x = castlePosition[0];
        posCameraDesired.y = castlePosition[1];
        collisionUnitLayer = (TiledMapTileLayer) map.getLayers().get(1);
        myCastle = new Castle(castleSprite, collisionUnitLayer);
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
                        switch (event.getButton()) {
                            case Input.Buttons.RIGHT:
                                for (Soldier s : soldierArrayList) {
                                    if (s.isSelected()) {
                                        getPathFinding(s);
                                    }
                                }
                                break;
                            case Input.Buttons.LEFT:
                                if (rectangleStart != null && rectangleEnd != null) {
                                    float[] recCoords = new float[]{rectangleBounds[0], rectangleBounds[1]};
                                    float[] vilCoords;
                                    for (Soldier soldier : soldierArrayList) {
                                        vilCoords = translateXYCoordinatesToScreen(soldier.getX() + soldier.getWidth() / 2, soldier.getY() + soldier.getHeight() / 2);
                                        if (vilCoords[0] >= recCoords[0] && vilCoords[1] >= recCoords[1]) {
                                            if (vilCoords[0] <= recCoords[0] + rectangleBounds[2] && vilCoords[1] <= recCoords[1] + rectangleBounds[3]) {
                                                soldier.setSelected(true);
                                            }
                                        }
                                    }
                                } else {
                                    getClickedOnEntity();
                                }
                                break;
                            default:
                                break;
                        }


                        //rectange should be reset no matter what
                        rectangleEnd = null;
                        rectangleStart = null;
                    }
                }
        );
        renderer = new OrthogonalTiledMapRenderer(map);

        for (int i = 0; i < startingCastle; i++) {
            //todo Castle neu ändern!! (objekte erzeugen und dann in das Array)
            castleArrayList.add(myCastle);
            myCastle.setPosition(Constants.MAP1CC1[0], Constants.MAP1CC1[1]);
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

        debugRenderer.begin();
        renderer.getBatch().begin();

        pointTimerCounter += delta;
        if (pointTimerCounter > 1) { //1 second update
            gameScreenEvent.sendPointRequest();//TODO move this to server???
            pointTimerCounter = 0;
        }

        scoreLabel.setText(gameScreenEvent.getPoints());

        villagerLabel.setText(myCastle.getVillager());
        goldLabel.setText(myCastle.getGold());
        for (Castle c : castleArrayList) {
            c.draw(renderer.getBatch());

            if (c.isSelected()) {

                entityHp.setText(c.getHp());
                buttonRekrut.setVisible(true);


                //todo braucht seine eigene forEach !!
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    if (!isRightPressed) {
                        isRightPressed = true;
                    } else {
                        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                            isRightPressed = false;
                        }
                    }
                }
                //todo Progresbar benutzen
                entityName.setText("Castle");
                Sprite s = new Sprite(uiAtlas.findRegion("button-normal"));
                s.setColor(Color.RED);
                s.setSize(c.getHp(), 10);
                s.setPosition(c.getX() + c.getSprite().getWidth() / 2/3, c.getY() + c.getSprite().getHeight() - 50);
                s.draw(renderer.getBatch());

            }

        }


        for (Soldier soldier : soldierArrayList) {
            soldier.draw(renderer.getBatch());

            if (soldier.getDestination() != null) {
                if (soldier.getDestination().isEmpty()) {
                    soldier.setDestination(null);
                } else {

                    int vX = (int) (soldier.getX() / 64);
                    int vY = (int) (soldier.getY() / 64);
                    System.out.println(soldier.getX());
                    System.out.println(soldier.getY());
                    System.out.println(vX);
                    System.out.println(vY);
                    System.out.println(soldier.getDestination().get(0).coords.x);
                    System.out.println(soldier.getDestination().get(0).coords.y);
                    System.out.println();
                    // System.out.println(vX + " | " + vY + " | " + v.getDestination().get(0).coords.x + " | " + v.getDestination().get(0).coords.y);

//                    if(v.getDestination().size() == 1){
//                        if(vX > v.getDestination().get(0).coords.x ){
//                            v.getDestination().get(0).coords.x -= 1;
//                        }else if(vY > v.getDestination().get(0).coords.y){
//                            v.getDestination().get(0).coords.y -= 1;
//                        }
//                    }

                    if (vX != soldier.getDestination().get(0).coords.x || vY != soldier.getDestination().get(0).coords.y) {
                        /*
                             if (vX != v.getDestination().get(0).coords.x || vY != v.getDestination().get(0).coords.y) {
                        if (v.getDestination().get(0).coords.x>vX){
                            v.translateX(1);
                        }else if (v.getDestination().get(0).coords.x<vX){
                            v.translateX(-1);
                        }
                        if (v.getDestination().get(0).coords.y>vY){
                            v.translateY(1);
                        }else if (v.getDestination().get(0).coords.y<vY){
                            v.translateY(-1);
                        }
                          //  v.translateX(v.getDestination().get(0).coords.x - vX);
                        //v.translateY(v.getDestination().get(0).coords.y - vY);

                        */
                        soldier.translateX(soldier.getDestination().get(0).coords.x - vX);
                        soldier.translateY(soldier.getDestination().get(0).coords.y - vY);


                    } else if (vX == soldier.getDestination().get(0).coords.x && vY == soldier.getDestination().get(0).coords.y) {
                        if (soldier.getDestination().size() >= 1) {
                            soldier.getDestination().remove(0);
                        }
                    }

                }
            }

            if (soldier.isSelected()) {
                entityHp.setText(soldier.getHp());
                buttonRekrut.setVisible(false);

                //todo braucht seine eigene forEach !!
                entityName.setText("Soldier");
                Sprite s = new Sprite(uiAtlas.findRegion("button-normal"));
                s.setColor(Color.RED);
                s.setSize(soldier.getHp(), 10);
                s.setPosition(soldier.getX() + 5, soldier.getY() + 60);
                s.draw(renderer.getBatch());
            }
        }


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

        if (myCastle.isSelected()) {
            entityName.setText("Castle");
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
            Sprite lineH = new Sprite(uiAtlas.findRegion("line-h"));
            Sprite lineV = new Sprite(uiAtlas.findRegion("line-v"));
            lineV.setColor(Color.GREEN);
            lineH.setColor(Color.GREEN);
            lineV.setSize(collisionUnitLayer.getWidth() * 64, 1);
            lineH.setSize(1, collisionUnitLayer.getHeight() * 64);

            for (int i = 1; i < 76; i++) {
                lineV.setPosition(0, (i * 64));
                lineH.setPosition((i * 64), 0);
                lineV.draw(renderer.getBatch());
                lineH.draw(renderer.getBatch());
            }
        }
        debugRenderer.end();
        renderer.getBatch().end();

        mouseOnEdgeofCamera();

        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        camera.position.set(stage.getWidth() / 2f, stage.getHeight() / 2f, 0);
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

        castleImage = new Image(castleSprite);
        soldierImage = new Image(soldierSprite);

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
        TextButton exitButton = new TextButton("Back", skin);
        Window windowNoVillager = new Window("NoVillager", skin, "border");
        windowNoVillager.setVisible(false);
        windowNoVillager.setMovable(false);
        windowNoVillager.add(new Label("not enough Villlager",skin)).row();
        windowNoVillager.add(exitButton);

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




        Image goldImage = new Image(new Sprite(new Texture("ui/gold_treasure_icons_16x16/gold.png")));

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


                noButton.getLabel().setFontScale(2f);


                stage.addActor(windowExit);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        yesButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameScreen.this.getGameScreenEvent().sendLeaveGameNotice();
                game.setScreen(new MenuScreen(game, skin));
                dispose();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });
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

        windowExit.add(exitLabel).colspan(2).row();
        windowExit.add(yesButton);
        windowExit.add(noButton);
        windowExit.setPosition(stage.getWidth() / 2.75f, stage.getHeight() / 2f);
        windowExit.pack();

        resourceBarWindow.add(scoreTextLabel).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
        resourceBarWindow.add(scoreLabel).padTop(30f).padBottom(10f).padRight(10f);
        resourceBarWindow.add(goldImage).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
        resourceBarWindow.add(goldLabel).padTop(30f).padBottom(10f).padRight(10f);
        resourceBarWindow.add(soldierImage).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
        resourceBarWindow.add(villagerLabel).padTop(30f).padBottom(10f).padRight(10f);

        resourceBarWindow.setPosition(0, stage.getHeight());

        exitWindow.add(exitGameButton).padTop(30f);
        exitWindow.setPosition(stage.getWidth(), stage.getHeight());

        entityWindow.add(entityName).padLeft(100f).padBottom(30f).row();

        Label hpLabel = new Label("HP", skin);
        Label atkLabel = new Label("ATK", skin);
        Label defLabel = new Label("DEF", skin);

        buttonRekrut = new TextButton("Rekrutieren",skin);

        buttonRekrut.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Villiger rekrut");
                if(myCastle.getVillager() != 0){
                    myCastle.setVillager(myCastle.getVillager() - 1);
                    Soldier soldier = new Soldier(soldierSprite,collisionUnitLayer);
                    soldierArrayList.add(soldier);
                }
                else {
                    windowNoVillager.setPosition(stage.getWidth() / 2,stage.getHeight() / 2);
                    windowNoVillager.setVisible(true);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        entityWindow.add(hpLabel);
        entityWindow.add(entityHp).padRight(30f).row();
        entityWindow.add(atkLabel);
        entityWindow.add(entityATK);
        entityWindow.add(buttonRekrut).padRight(30f).row();
        entityWindow.add(defLabel);
        entityWindow.add(entityDEF);
        entityWindow.setPosition(stage.getWidth() / 2 - 300, 0);


        packWindow(resourceBarWindow, stage);
        packWindow(exitWindow, stage);
        packWindow(entityWindow, stage);
        packWindow(windowNoVillager,stage);

        stage.addActor(windowNoVillager);
        stage.addActor(entityWindow);
        stage.addActor(resourceBarWindow);
        stage.addActor(exitWindow);

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
    private void processCameraMovement(float xClicked, float yClicked) {

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

    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4
            projectionMatrix) {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public GameScreenEvent getGameScreenEvent() {
        return gameScreenEvent;
    }

    public void getClickedOnEntity() {

        float[] coords = translateXYCoordinatesFromScreen(Gdx.input.getX(), Gdx.input.getY());
        for (Soldier soldier : soldierArrayList) {

            if (soldier.getX() < (int) coords[0] && soldier.getY() < (int) coords[1]) {
                if (soldier.getX() + soldier.getWidth() >= (int) coords[0] && soldier.getY() + soldier.getHeight() >= (int) coords[1]) {
                    soldier.setSelected(!soldier.isSelected());
                } else {
                    soldier.setSelected(false);
                }
            }
        }
        for (Castle c : castleArrayList) {

            if (c.getX() < (int) coords[0] && c.getY() < (int) coords[1]) {
                if (c.getX() + c.getWidth() >= (int) coords[0] && c.getY() + c.getHeight() >= (int) coords[1]) {
                    c.setSelected(!c.isSelected());
                } else {
                    c.setSelected(false);
                }
            }
        }

//        try {
//            if (collisionUnitLayer.getCell((int) coords[0] / collisionUnitLayer.getTileWidth(), (int) coords[1] / collisionUnitLayer.getTileHeight())
//                    .getTile().getProperties().containsKey("isCastel")) {
//                myCastle.setSelected(!myCastle.isSelected());
//            }
//        } catch (Exception e) {
//
//        }
    }

    //[0]=x[1]=y
    private float[] translateXYCoordinatesFromScreen(float x, float y) {
        Vector3 mousePos = new Vector3(x, y, 0);
        camera.unproject(mousePos);

        return new float[]{mousePos.x, mousePos.y};
    }

    private float[] translateXYCoordinatesToScreen(float x, float y) {
        Vector3 mousePos = new Vector3(x, y, 0);
        camera.project(mousePos);

        return new float[]{mousePos.x, mousePos.y};
    }

    public void getPathFinding(Soldier v) {
        //todo pathfinding programmieren
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        int x = (int) mousePos.x, y = (int) mousePos.y;
        PathCell p = new Pathfinding(x, y, (int) v.getX() + 32, (int) v.getY() + 32, collisionUnitLayer).algorithm();
        LinkedList<PathCell> cellList = new LinkedList<>();

        while (p != null) {
            cellList.add(p);
            p = p.parent;
        }
        if (cellList.size() >= 2) {
            if ((cellList.get(0).coords.x < cellList.get(1).coords.x || cellList.get(0).coords.y < cellList.get(1).coords.y)) {
                // Vector2 newVector = new Vector2(cellList.get(0).coords.x, cellList.get(0).coords.y);
                if (cellList.get(0).coords.x < cellList.get(1).coords.x) {
                    cellList.get(0).coords.x -= 1;
                }
                if (cellList.get(0).coords.y < cellList.get(1).coords.y) {
                    cellList.get(0).coords.y -= 1;
                }

                // PathCell pNew = new PathCell(newVector, null, null);
                // cellList.get(0).parent = pNew;
                // cellList.addFirst(pNew);
            }
        }


        Collections.reverse(cellList);
        v.setDestination(cellList);
        theKnowenWay = cellList;
        for (PathCell t : cellList) {
            System.out.println(t.coords);

        }

        //end=start

    }

}

