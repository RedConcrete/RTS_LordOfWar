package code.lordofwar.screens;

import code.lordofwar.backend.*;
import code.lordofwar.backend.events.GameScreenEvent;
import code.lordofwar.backend.interfaces.CombatEntity;
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
import com.badlogic.gdx.maps.MapProperties;
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
import kotlin.Pair;

import java.awt.geom.Point2D;
import java.util.*;


public class GameScreen extends Screens implements Screen {
    //TODO remove this from finished version
    private static final ShapeRenderer debugRenderer = new ShapeRenderer();
    private static final ShapeRenderer debugMovement = new ShapeRenderer();

    private final GameScreenEvent gameScreenEvent;
    private final ShapeRenderer rectangleRenderer;
    private final Castle castle;
    private final TiledMapTileLayer collisionUnitLayer;
    private final OrthographicCamera camera;

    private final HashMap<String, Rectangle> enemyHitboxes;//only for enemy units/castles
    private final ArrayList<Castle> enemyCastleArrayList;

    private final HashMap<Integer, Rectangle> hitboxes;

    private final boolean cameraDebug;
    private final boolean mapDebug;
    private boolean isRightPressed;

    private final Vector3 posCameraDesired;
    private final TiledMap map;
    private final int[] mapSizes;
    private OrthogonalTiledMapRenderer renderer;

    private HashMap<Integer, Soldier> ownSoldierHashMap;
    private HashMap<String, Soldier> enemySoldierHashMap = new HashMap<>();
    private HashMap<String, Castle> enemyCastleMap;


    private TextButton buttonRecruit;

    private final Label entityHp;
    private final Label entityName;
    private final Label soldierLabel;
    private final Label goldLabel;
    private Label scoreLabel;
    private Label entityATK;


    //todo muss von lobbyÜbergebe werden
    private final int startingCastle = 1;
    private int goldAmount = 100;
    private static final int SOLDIER_COST = 10;

    private final Sprite soldierSprite;
    private final Sprite enemySprite;
    private final Sprite castleSprite;

    private final TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("ui/skin/uiskin.atlas"));

    private float pointTimerCounter;
    private Point2D.Float rectangleStart;
    private Point2D.Float rectangleEnd;
    private final float[] rectangleBounds;


    public GameScreen(LOW aGame, Skin aSkin, String lobbyID, Integer startingPosition, String[] connectedPlayersArray) {
        super(aGame, aSkin);
        mapDebug = false;
        hitboxes = new HashMap<>();
        enemyHitboxes = new HashMap<>();
        isRightPressed = false;
        entityName = new Label("", skin);
        TextureAtlas mapAtlas = new TextureAtlas(Gdx.files.internal("maps/RTSSimple.txt"));
        castleSprite = new Sprite(mapAtlas.findRegion("Castle"));
        pointTimerCounter = 10;
        gameScreenEvent = new GameScreenEvent(game, lobbyID, this);
        posCameraDesired = new Vector3();
        cameraDebug = false;

        ownSoldierHashMap = new HashMap<>();

        enemyCastleArrayList = new ArrayList<>();
        enemyCastleMap = new HashMap<>();

        soldierLabel = new Label("", skin);
        goldLabel = new Label("", skin);
        entityHp = new Label("", skin);

        rectangleRenderer = new ShapeRenderer();
        rectangleStart = null;
        rectangleEnd = null;
        rectangleBounds = new float[4];//0=originX1=originY2=width3=height
        TextureAtlas unitAtlas = new TextureAtlas(Gdx.files.internal("maps/RTS_UNITS_TILES.txt"));
        soldierSprite = new Sprite(unitAtlas.findRegion("Character_Green"));
        enemySprite = new Sprite(unitAtlas.findRegion("Character_Green"));
        TmxMapLoader loader = new TmxMapLoader();
        camera = new OrthographicCamera();

        //todo zeigt alle verbunden players
        gameScreenEvent.getConnectedPlayer(ArrayToArraylist(connectedPlayersArray), startingPosition);


        String mapPath = "maps/map_1.tmx";
        map = loader.load(mapPath);
        MapProperties mapProperties = map.getProperties();
        mapSizes = new int[]{
                mapProperties.get("width", Integer.class),//tiles
                mapProperties.get("height", Integer.class),//tiles
                mapProperties.get("tilewidth", Integer.class),//tile
                mapProperties.get("tileheight", Integer.class),//tile
                //tile*tiles
                mapProperties.get("width", Integer.class) * mapProperties.get("tilewidth", Integer.class),
                mapProperties.get("height", Integer.class) * mapProperties.get("tileheight", Integer.class)
        };
        //TODO way to tell maps apart
        float[] castlePosition = {};
        System.out.println(startingPosition);
        switch (startingPosition) {
            case 0:
                castlePosition = Constants.MAP1CC1;
                break;
            case 1:
                castlePosition = Constants.MAP1CC2;
                break;
            case 2:
                castlePosition = Constants.MAP1CC3;
                break;
            case 3:
                castlePosition = Constants.MAP1CC4;
                break;
            default:
                System.err.println("Unexpected value: " + startingPosition);//max of 4 players; thus error
        }

        collisionUnitLayer = (TiledMapTileLayer) map.getLayers().get(1);

        posCameraDesired.x = castlePosition[0];
        posCameraDesired.y = castlePosition[1];
        castle = new Castle(castleSprite, collisionUnitLayer, new Team(startingPosition));
        castle.setPosition(castlePosition[0], castlePosition[1]);

        //TODO get enemy castles and add them to enemyCastleMap
        //TODO add enemy castles to hitboxes  ()

        Rectangle myCastleHB = new Rectangle(castle.getBoundingRectangle());
        myCastleHB.setWidth(myCastleHB.getWidth());
        myCastleHB.setHeight(myCastleHB.getHeight() - 64);
        hitboxes.put(castle.hashCode(), myCastleHB);

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
                                for (Soldier s : ownSoldierHashMap.values()) {
                                    if (s.isSelected()) {
                                        getPathFinding(s);
                                    }
                                }
                                break;
                            case Input.Buttons.LEFT:
                                if (rectangleStart == null && rectangleEnd == null) {
                                    getClickedOnEntity();
                                } else {
                                    Rectangle selectRect = new Rectangle(rectangleBounds[0], rectangleBounds[1], rectangleBounds[2], rectangleBounds[3]);
                                    for (Soldier soldier : ownSoldierHashMap.values()) {
                                        if (hitboxes.containsKey(soldier.hashCode())) {
                                            soldier.setSelected(hitboxCheckRect(hitboxes.get(soldier.hashCode()), selectRect));
                                        }
                                    }
                                    if (hitboxes.containsKey(castle.hashCode())) {
                                        castle.setSelected(hitboxCheckRect(hitboxes.get(castle.hashCode()), selectRect));
                                    }
                                    for (Castle castle : enemyCastleArrayList) {
                                        if (hitboxes.containsKey(castle.hashCode())) {
                                            castle.setSelected(hitboxCheckRect(hitboxes.get(castle.hashCode()), selectRect));
                                        }
                                    }
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
    }

    private boolean isColliding(Sprite sprite) {
        for (Map.Entry<Integer, Rectangle> hitbox : hitboxes.entrySet()) {
            if (hitbox.getKey() != sprite.hashCode() && sprite.getBoundingRectangle().overlaps(hitbox.getValue())) {
                return true;
            }
        }
        for (Rectangle hitbox : enemyHitboxes.values()) {//hashcode doesnt need to be checked
            return sprite.getBoundingRectangle().overlaps(hitbox);
        }
        return false;
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

        gameScreenEvent.CameraKeyEvents(camera, CAMERASPEED, posCameraDesired);
        gameScreenEvent.setLabelText(scoreLabel, soldierLabel, goldLabel, castle);

        countPoints(delta);


        ArrayList<Castle> castles = enemyCastleArrayList;
        castles.add(castle);
        for (Castle c : castles) {
            c.draw(renderer.getBatch());

            if (c.isSelected()) {
                entityHp.setText(c.getHp());
                if (c == castle) {
                    buttonRecruit.setVisible(true);
                }
                //todo Progresbar benutzen
                entityName.setText("Castle");
                Sprite s = new Sprite(uiAtlas.findRegion("button-normal"));
                s.setColor(Color.RED);
                s.setSize(c.getHp(), 10);
                s.setPosition(c.getX() + c.getSprite().getWidth() / 2 / 3, c.getY() + c.getSprite().getHeight() - 50);
                s.draw(renderer.getBatch());

            }
        }

        for (Soldier s : enemySoldierHashMap.values()) {
            s.draw(renderer.getBatch());
        }

        ArrayList<String> a = new ArrayList<>();
        a.add(game.getSessionID());
        a.add(gameScreenEvent.getLobbyID());
        a.add(String.valueOf(castle.getTeam().getStartingPos()));
        for (Soldier s : ownSoldierHashMap.values()) {
            s.draw(renderer.getBatch());
            a.add(s.toString());
            a.add(String.valueOf(s.hashCode()));

            if (s.getDestination() != null) {
                if (s.getDestination().isEmpty()) {
                    s.setDestination(null);
                } else {

                    int vX = (int) ((s.getX() + 32) / 64);
                    int vY = (int) ((s.getY() + 32) / 64);

                    if (vX != s.getDestination().get(0).coords.x || vY != s.getDestination().get(0).coords.y) {

                        if (s.getDestination().get(0).coords.x < vX) {
                            s.translateX(-1);
                        } else if (s.getDestination().get(0).coords.x > vX) {
                            s.translateX(1);
                        }
                        if (s.getDestination().get(0).coords.y < vY) {
                            s.translateY(-1);
                        } else if (s.getDestination().get(0).coords.y > vY) {
                            s.translateY(1);
                        }

                        //TODO maybe do a isColliding method in soldier? idk discuss
                        if (isColliding(s)) {
                            //reverse direction
                            if (s.getDestination().get(0).coords.x < vX) {
                                s.setX(s.getX() + s.getWidth() / 2);
                            } else if (s.getDestination().get(0).coords.x > vX) {
                                s.setX(s.getX() - s.getWidth() / 2);
                            }
                            if (s.getDestination().get(0).coords.y < vY) {
                                s.setY(s.getY() - s.getHeight() / 2);

                            } else if (s.getDestination().get(0).coords.y > vY) {
                                s.setY(s.getY() - s.getHeight() / 2);
                            }
                            getPathFinding(s, (int) s.getDestination().get(s.getDestination().size() - 1).coords.x * collisionUnitLayer.getTileWidth(), (int) (s.getDestination().get(s.getDestination().size() - 1).coords.y * collisionUnitLayer.getTileHeight()));
                        }

                    } else if (vX == s.getDestination().get(0).coords.x && vY == s.getDestination().get(0).coords.y) {
                        if (s.getDestination().size() >= 1) {
                            System.out.println(s.getDestination().get(0).coords.x + " " + s.getDestination().get(0).coords.y + " reached");
                            s.getDestination().remove(0);
                            hitboxes.put(s.hashCode(), s.getBoundingRectangle());//set hitbox when having reached a tile
                        }
                    }
                }
            } else {
                hitboxes.put(s.hashCode(), s.getBoundingRectangle());//set hitbox when having stopped moving
            }

            if (s.isSelected()) {
                entityHp.setText(s.getHP());
                buttonRecruit.setVisible(false);

                //todo braucht seine eigene forEach !!
                entityName.setText("Soldier");
                Sprite sprite = new Sprite(uiAtlas.findRegion("button-normal"));
                sprite.setColor(s.getTeam().getColor());
                sprite.setSize(s.getHP(), 10);
                sprite.setPosition(sprite.getX() + 5, sprite.getY() + 60);
                sprite.draw(renderer.getBatch());
            }
            //combat checks
            if (s.isAlive()) {
                if (s.canAttack()) {
                    for (Map.Entry<String, Rectangle> hitbox : enemyHitboxes.entrySet()) {
                        HashMap<String, CombatEntity> enemyMap = new HashMap<>(enemySoldierHashMap);
                        enemyMap.putAll(enemyCastleMap);
                        if (enemyMap.get(hitbox.getKey()).isAlive()) {
                            if (s.getTarget() == null || enemyMap.get(hitbox.getKey()) == s.getTarget()) {
                                if (s.getCombatReach().overlaps(hitbox.getValue())) {
                                    //  enemySoldierHashMap.get(hitbox.getKey()).receiveDmg(s.dealDmg());//transmit dmg to appropiate color via msg instead of calculating here?
                                    gameScreenEvent.sendAtkRequest(s.dealDmg(), hitbox.getKey(), enemyMap.get(hitbox.getKey()));//send atk data
                                }
                            }
                        }
                    }
                }
                //TODO after enemy hitbox
            }
        }

        gameScreenEvent.updateSoldierPos(a);

        //send a here
        if (mapDebug) {
            Sprite lineH = new Sprite(uiAtlas.findRegion("line-h"));
            Sprite lineV = new Sprite(uiAtlas.findRegion("line-v"));
            lineV.setColor(Color.GREEN);
            lineH.setColor(Color.GREEN);
            lineV.setSize(collisionUnitLayer.getWidth() * 64, 1);
            lineH.setSize(1, collisionUnitLayer.getHeight() * 64);

            for (int i = 1; i < 76; i++) {
                lineV.setPosition(0, (i * 64) + 32);
                lineH.setPosition((i * 64) + 32, 0);
                lineV.draw(renderer.getBatch());
                lineH.draw(renderer.getBatch());
            }
        }
        debugRenderer.end();
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
        }


        mouseOnEdgeofCamera();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        createResourceBarWindow();
        createEntityWindow();
        createExitWindow();
        stage.setDebugAll(false);
    }

    private void createResourceBarWindow() {

        Window resourceBarWindow = new Window("", skin);
        resourceBarWindow.setMovable(false);

        Label scoreTextLabel = new Label(" Your Score:", skin);

        scoreLabel = new Label("", skin);

        Image goldImage = new Image(new Sprite(new Texture("ui/gold_treasure_icons_16x16/gold.png")));

        Image soldierImage = new Image(soldierSprite);

        resourceBarWindow.add(scoreTextLabel).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
        resourceBarWindow.add(scoreLabel).padTop(30f).padBottom(10f).padRight(10f);
        resourceBarWindow.add(goldImage).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
        resourceBarWindow.add(goldLabel).padTop(30f).padBottom(10f).padRight(10f);
        resourceBarWindow.add(soldierImage).padTop(30f).padBottom(10f).padLeft(10f).padRight(10f);
        resourceBarWindow.add(soldierLabel).padTop(30f).padBottom(10f).padRight(10f);

        resourceBarWindow.setPosition(0, stage.getHeight());
        packWindow(resourceBarWindow, stage);
        stage.addActor(resourceBarWindow);

    }

    private void createEntityWindow() {

        // No Villager Window
        Window windowNoVillager = new Window("NoVillager", skin, "border");
        windowNoVillager.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        windowNoVillager.setVisible(false);
        windowNoVillager.setMovable(false);
        windowNoVillager.add(new Label("You have not enough Villager to recruit a Soldier", skin)).padTop(20f).padRight(10f).padLeft(10f).row();
        backButton(stage, skin, game, windowNoVillager);

        // No Gold Window
        Window windowNoGold = new Window("", skin, "border");
        windowNoGold.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        windowNoGold.setVisible(false);
        windowNoGold.setMovable(false);
        windowNoGold.add(new Label("You have not enough Gold", skin)).padTop(20f).padRight(10f).padLeft(10f).row();
        backButton(stage, skin, game, windowNoGold);

        // Entity Window
        Window entityWindow = new Window("", skin);
        entityWindow.setMovable(false);

        Label hpLabel = new Label("HP", skin);
        Label atkLabel = new Label("ATK", skin);

        buttonRecruit = new TextButton("Recruit", skin);
        TextButton buttonIncreaseMaxUnits = new TextButton("Upgrade Max Units", skin);

        buttonRecruit.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (castle.getVillager() != 0 && castle.getGold() - SOLDIER_COST >= 0) {
                    if (ownSoldierHashMap.size() <= castle.getMaxUnits()) {
                        castle.setVillager(castle.getVillager() - 1);
                        castle.setGold(castle.getGold() - SOLDIER_COST);

                        soldierSprite.setColor(castle.getTeam().getColor());
                        Soldier soldier = new Soldier(soldierSprite, collisionUnitLayer, castle.getTeam());
                        ownSoldierHashMap.put(soldier.hashCode(), soldier);

                    }
                } else {
                    if (castle.getGold() - SOLDIER_COST <= 0) {
                        windowNoGold.setVisible(true);
                    } else {
                        windowNoVillager.setVisible(true);
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

        });


        buttonIncreaseMaxUnits.addListener(new InputListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if (castle.getGold() - castle.getMaxUnits() >= 0) {
                    castle.setGold(castle.getGold() - castle.getMaxUnits());
                    castle.setMaxUnits(castle.getMaxUnits() + 10);
                } else {
                    windowNoGold.setVisible(true);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }


        });

        entityWindow.add(entityName).padLeft(100f).padBottom(30f).row();
        entityWindow.add(hpLabel);
        entityWindow.add(entityHp).padRight(30f).row();
        entityWindow.add(atkLabel);
        entityWindow.add(entityATK);
        entityWindow.add(buttonRecruit).padRight(30f).row();
        entityWindow.add(buttonIncreaseMaxUnits).padRight(30f).row();
        entityWindow.setPosition(stage.getWidth() / 2 - 300, 0);

        packWindow(windowNoVillager, stage);
        packWindow(entityWindow, stage);
        packWindow(windowNoGold, stage);

        stage.addActor(windowNoVillager);
        stage.addActor(windowNoGold);
        stage.addActor(entityWindow);
    }

    private void createExitWindow() {
        Window exitWindow = new Window("", skin);
        exitWindow.setMovable(false);


        Window windowExit = new Window("Surrender?", skin, "border");
        windowExit.setMovable(false);
        TextButton exitGameButton = new TextButton("Exit", skin);
        exitGameButton.getLabel().setFontScale(3f);

        TextButton noButton = new TextButton("No", skin);
        TextButton yesButton = new TextButton("Yes", skin);

        Label exitLabel = new Label("Do you really want to Surrender?", skin);
        exitLabel.setFontScale(3f);

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

        gameScreenEvent.StageKeyEvents(windowExit, yesButton, noButton, stage);

        windowExit.defaults().pad(20f);

        exitWindow.add(exitGameButton).padTop(30f);
        exitWindow.setPosition(stage.getWidth(), stage.getHeight());

        packWindow(exitWindow, stage);
        stage.addActor(exitWindow);

    }

    private void mouseOnEdgeofCamera() {
        float xClicked, yClicked;
        xClicked = Gdx.input.getX();
        yClicked = Gdx.input.getY();
        gameScreenEvent.processCameraMovement(xClicked, yClicked, camera, CAMERASPEED, debugMovement, cameraDebug, posCameraDesired);
        camera.position.lerp(posCameraDesired, 0.1f);
        keepCameraInBounds();
        if (cameraDebug) {
            DrawDebugLine(new Vector2(camera.position.x, camera.position.y)
                    , new Vector2(posCameraDesired.x, posCameraDesired.y)
                    , 3, Color.RED, camera.combined);
        }
    }

    /**
     * Keeps the camera from going out of the tiledmap
     */
    private void keepCameraInBounds() {
        //horizontal
        if (camera.position.x < camera.viewportWidth / 2) {//height and width /2 because camera goes to right and left by half the width
            //need to change both actual camera value AND desired camera value to prevent lockup
            posCameraDesired.x = camera.viewportWidth / 2;
            camera.position.x = camera.viewportWidth / 2;
        } else if (camera.position.x > mapSizes[4] - camera.viewportWidth / 2) {
            posCameraDesired.x = mapSizes[4] - camera.viewportWidth / 2;
            camera.position.x = mapSizes[4] - camera.viewportWidth / 2;
        }
        // Vertical
        if (camera.position.y < camera.viewportHeight / 2) {
            posCameraDesired.y = camera.viewportHeight / 2;
            camera.position.y = camera.viewportHeight / 2;
        } else if (camera.position.y > mapSizes[5] - camera.viewportHeight / 2) {
            posCameraDesired.y = mapSizes[5] - camera.viewportHeight / 2;
            camera.position.y = mapSizes[5] - camera.viewportHeight / 2;
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


    /**
     * Checks whether two Hitboxes overlap.
     *
     * @param hitbox  the first hitbox
     * @param hitbox2 the second hitbox
     * @return {@code true} if they overlap.
     */
    private boolean hitboxCheckRect(Rectangle hitbox, Rectangle hitbox2) {//did this to reduce copying a bit appreciated advice on how to further reduce
        return hitbox.overlaps(hitbox2);
    }

    /**
     * Checks whether a point is within a hitbox
     *
     * @param hitbox the hitbox
     * @param coords a float array where index 0 are the x coordinates and index 1 are the y coordinates
     * @return {@code true} if the hitbox contains the given coordinates.
     *///NOTICE: DO not use Rectangle#contains it doesnt work correctly.
    private boolean hitboxCheckXY(Rectangle hitbox, float[] coords) {//did this to reduce copying a bit appreciated advice on how to further reduce
        if (hitbox != null) {//should never happen but better be sure
            return hitbox.getX() <= coords[0] && hitbox.getX() + hitbox.getWidth() >= coords[0] && hitbox.getY() <= coords[1] && hitbox.getY() + hitbox.getHeight() >= coords[1];
        }
        return false;
    }

    public void getClickedOnEntity() {

        float[] coords = translateXYCoordinatesFromScreen(Gdx.input.getX(), Gdx.input.getY());
        Rectangle hitbox;
        for (Soldier soldier : ownSoldierHashMap.values()) {
            hitbox = hitboxes.get(soldier.hashCode());
            if (hitboxCheckXY(hitbox, coords)) {
                soldier.setSelected(!soldier.isSelected());
            } else {
                soldier.setSelected(false);
            }
        }

        hitbox = hitboxes.get(castle.hashCode());
        if (hitboxCheckXY(hitbox, coords)) {//should never happen but better be sure
            castle.setSelected(!castle.isSelected());
        } else {
            castle.setSelected(false);
        }

        //todo wenn enemyCastle gesendent wird verwerdne um hp anzu zeigen!

//        for (Castle c : enemyCastleArrayList) {
//            hitbox = hitboxes.get(c.hashCode());
//
//            if (hitboxCheckXY(hitbox, coords)) {//should never happen but better be sure
//                c.setSelected(!c.isSelected());
//            } else {
//                c.setSelected(false);
//            }
//        }

//        try {
//            if (collisionUnitLayer.getCell((int) coords[0] / collisionUnitLayer.getTileWidth(), (int) coords[1] / collisionUnitLayer.getTileHeight())
//                    .getTile().getProperties().containsKey("isCastel")) {
//                myCastle.setSelected(!myCastle.isSelected());
//            }
//        } catch (Exception e) {
//
//        }
    }


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
        float[] mousePos = translateXYCoordinatesFromScreen(Gdx.input.getX(), Gdx.input.getY());
        int x = (int) mousePos[0], y = (int) mousePos[1];
        getPathFinding(v, x, y);
    }

    public void getPathFinding(Soldier v, int xTile, int yTile) {
        //pathingCollisionMap.getCell((int) (v.getX()+32)/64, (int) (v.getY()+32)/64).getTile().getProperties().clear();
        HashMap<Integer, Rectangle> tempHitboxes = hitboxes;
        tempHitboxes.remove(v.hashCode());
        HashSet<Rectangle> tempHitboxesColl = new HashSet<>(tempHitboxes.values());
        tempHitboxesColl.addAll(enemyHitboxes.values());
        PathCell p = new Pathfinding(xTile, yTile, (int) v.getX() + 32, (int) v.getY() + 32, collisionUnitLayer, tempHitboxesColl).algorithm();
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


        //end=start

    }

    public void countPoints(float delta) {
        pointTimerCounter += delta;
        if (pointTimerCounter >= 1) { //1 second update
            gameScreenEvent.sendPointRequest();//TODO move this to server???
            pointTimerCounter = 0;
        }
    }

    private ArrayList<String> ArrayToArraylist(String[] strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }

    public void createSoldiers(ArrayList<String> enemyArrList) {
        int startPos = Integer.parseInt(enemyArrList.get(0));
        Team team = new Team(startPos);
        Set<String> hashCompareSet = new HashSet<>();
        if (enemySoldierHashMap != null) {
            for (int i = 1; i < enemyArrList.size(); i += 2) {
                String[] pos = enemyArrList.get(i).split(",");//x and y pos
                hashCompareSet.add(enemyArrList.get(i + 1));//add hashcode to list no matter what
                if (!enemySoldierHashMap.containsKey(enemyArrList.get(i + 1))) {
                    //POsition [x anzahl von koordinaten und hashcodes]
                    enemySprite.setColor(team.getColor());
                    Soldier soldier = new Soldier(enemySprite, collisionUnitLayer, team);
                    soldier.setPosition(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
                    enemySoldierHashMap.put(enemyArrList.get(i + 1), soldier);
                    enemyHitboxes.put(enemyArrList.get(i + 1), soldier.getBoundingRectangle());
                } else {
                    enemySoldierHashMap.get(enemyArrList.get(i + 1)).setPosition(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
                    enemyHitboxes.put(enemyArrList.get(i + 1), enemySoldierHashMap.get(enemyArrList.get(i + 1)).getBoundingRectangle());
                }
            }
            //how to handle a dead soldier?
            for (Map.Entry<String, Soldier> entry : enemySoldierHashMap.entrySet()) {
                if (entry.getValue().getTeam().getStartingPos() == team.getStartingPos()) {//check if current team
                    if (!hashCompareSet.contains(entry.getKey())) {//check if entry does not contain key
                        enemySoldierHashMap.remove(entry.getKey());//remove soldier if he wasnt contained in the message
                        enemyHitboxes.remove(entry.getKey());
                    }
                }
            }
        }
    }

    public void processDmg(String unitType, Integer unitHash, Integer dmgType, Integer dmg) {

        if (unitType != null && unitHash != null && dmgType != null && dmg != null) {
            if (unitType.equals(Soldier.UNIT_TYPE)) {
                Soldier soldier = ownSoldierHashMap.get(unitHash);
                if (soldier != null) {
                    soldier.receiveDmg(new Pair<>(dmgType, dmg));
                    System.out.println(soldier.getHP());
                    if (!soldier.isAlive()) {
                        ownSoldierHashMap.remove(unitHash);
                        hitboxes.remove(soldier.hashCode());
                        //soldier is no longer anywhere and should now be trashed by java
                        //its also not gonna be sent to the server either after this render
                    }
                }

            } else if (unitType.equals(Castle.UNIT_TYPE)) {
                if (castle.hashCode() == unitHash) {
                    castle.receiveDmg(new Pair<>(dmgType, dmg));
                    if (!castle.isAlive()) {
                        //todo kill castle and lose game here
                    }
                }

            }
        }
    }

    public GameScreenEvent getGameScreenEvent() {
        return gameScreenEvent;
    }

}
