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

import java.awt.geom.Point2D;
import java.util.*;

public class GameScreen extends Screens implements Screen {

    private static final ShapeRenderer debugRenderer = new ShapeRenderer();
    private static final ShapeRenderer debugMovement = new ShapeRenderer();

    private ShapeRenderer rectangleRenderer;

    private final TiledMapTileLayer collisionUnitLayer;
    private final OrthographicCamera camera;
    private final boolean cameraDebug;
    private final boolean mapDebug;

    private TextButton buttonRecruit;
    private TextButton buttonIncreaseMaxUnits;

    private boolean knowTheWay = false;
    private LinkedList<PathCell> theKnowenWay;
    private Vector3 posCameraDesired;
    private final TiledMap map;
    private final int[] mapSizes;
    private OrthogonalTiledMapRenderer renderer;

    private Label entityHp;
    private Label entityATK;
    private Label entityDEF;

    private final ArrayList<Soldier> soldierArrayList;
    private final ArrayList<Castle> castleArrayList;

    private Sprite soldierSprite;
    private Sprite castleSprite;

    private Label entityName;

    private TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("ui/skin/uiskin.atlas"));
    private TextureAtlas unitAtlas = new TextureAtlas(Gdx.files.internal("maps/RTS_UNITS_TILES.txt"));
    private TextureAtlas mapAtlas = new TextureAtlas(Gdx.files.internal("maps/RTSSimple.txt"));

    private boolean isLeftPressed;
    private boolean isRightPressed;
    private Castle castle;

    private Label soldierLabel;
    private Label goldLabel;

    private final TmxMapLoader loader;
    private Label scoreLabel;
    private final GameScreenEvent gameScreenEvent;

    //todo muss von lobbyÜbergebe werden
    private final int startingCastle = 1;
    private int goldAmount = 100;

    private float pointTimerCounter;
    private Point2D.Float rectangleStart; //used to check where the select rectangle was started
    private Point2D.Float rectangleEnd;
    private final float[] rectangleBounds;
    private HashMap<Integer, Rectangle> hitboxes;//yeah sorry couldnt come up with a better way to dynamically check than just checking rectangles
    private ArrayList<String> connectedPlayers;

    Image castleImage;
    Image soldierImage;

    public GameScreen(LOW aGame, Skin aSkin, String lobbyID, Integer startingPosition, String[] connectedPlayersArray) {
        super(aGame, aSkin);
        this.connectedPlayers = ArrayToArraylist(connectedPlayersArray);
        mapDebug = false;
        hitboxes = new HashMap<>();
        isLeftPressed = false;
        isRightPressed = false;
        entityName = new Label("", skin);
        castleSprite = new Sprite(mapAtlas.findRegion("Castle"));
        pointTimerCounter = 10;
        gameScreenEvent = new GameScreenEvent(game, lobbyID);
        posCameraDesired = new Vector3();
        cameraDebug = false;
        soldierArrayList = new ArrayList<>();
        castleArrayList = new ArrayList<>();

        soldierLabel = new Label("", skin);
        goldLabel = new Label("", skin);
        entityHp = new Label("", skin);

        rectangleRenderer = new ShapeRenderer();
        rectangleStart = null;//null bc rectangle was started
        rectangleEnd = null;
        rectangleBounds = new float[4];//0=originX1=originY2=width3=height
        soldierSprite = new Sprite(unitAtlas.findRegion("Character_Green"));
        loader = new TmxMapLoader();
        camera = new OrthographicCamera();

        //todo zeigt alle verbunden players
        //System.out.println(Arrays.toString(connectedPlayers));
        gameScreenEvent.getConnectedPlayer(connectedPlayers, startingPosition);


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
                //THIS SHOULD NEVER HAPPEN
                System.err.println("Unexpected value: " + startingPosition);//max of 4 players; thus error
        }

        collisionUnitLayer = (TiledMapTileLayer) map.getLayers().get(1);

        posCameraDesired.x = castlePosition[0];
        posCameraDesired.y = castlePosition[1];
        castle = new Castle(castleSprite, collisionUnitLayer, new Team(startingPosition));
        castle.setPosition(castlePosition[0], castlePosition[1]);

        //TODO add castles to HB map
        for (int i = 0; i < startingCastle; i++) {
            //todo Castle neu ändern!! (objekte erzeugen und dann in das Array)
            castleArrayList.add(castle);
            castle.setPosition(castlePosition[0], castlePosition[1]);
        }
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
                                for (Soldier s : soldierArrayList) {
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
                                    for (Soldier soldier : soldierArrayList) {
                                        soldier.setSelected(hitboxCheckRect(hitboxes.get(soldier.hashCode()), selectRect));
                                    }
                                    for (Castle castle : castleArrayList) {
                                        castle.setSelected(hitboxCheckRect(hitboxes.get(castle.hashCode()), selectRect));
                                        System.out.println(castle.isSelected());
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

        countPoints(delta);

        scoreLabel.setText(gameScreenEvent.getPoints());

        soldierLabel.setText(castle.getVillager());
        goldLabel.setText(castle.getGold());

        for (Castle c : castleArrayList) {
            c.draw(renderer.getBatch());

            if (c.isSelected()) {

                entityHp.setText(c.getHp());
                buttonRecruit.setVisible(true);

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
                s.setPosition(c.getX() + c.getSprite().getWidth() / 2 / 3, c.getY() + c.getSprite().getHeight() - 50);
                s.draw(renderer.getBatch());

            }
        }

        for (Soldier s : soldierArrayList) {
            s.draw(renderer.getBatch());

            gameScreenEvent.updatePos(s);

            if (s.getDestination() != null) {
                if (s.getDestination().isEmpty()) {
                    s.setDestination(null);
                } else {

                    int vX = (int) ((s.getX() + 32) / 64);
                    int vY = (int) ((s.getY() + 32) / 64);
//                    System.out.println(soldier.getX());
//                    System.out.println(soldier.getY());
//                    System.out.println(vX);
//                    System.out.println(vY);
//                    System.out.println(soldier.getDestination().get(0).coords.x);
//                    System.out.println(soldier.getDestination().get(0).coords.y);
//                    System.out.println();
                    // System.out.println(vX + " | " + vY + " | " + v.getDestination().get(0).coords.x + " | " + v.getDestination().get(0).coords.y);

//                    if(v.getDestination().size() == 1){
//                        if(vX > v.getDestination().get(0).coords.x ){
//                            v.getDestination().get(0).coords.x -= 1;
//                        }else if(vY > v.getDestination().get(0).coords.y){
//                            v.getDestination().get(0).coords.y -= 1;
//                        }
//                    }

                    if (vX != s.getDestination().get(0).coords.x || vY != s.getDestination().get(0).coords.y) {
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
                        /*better code probably but inconsistent speed sometimes
                        soldier.translateX(soldier.getDestination().get(0).coords.x - vX);
                        soldier.translateY(soldier.getDestination().get(0).coords.y - vY);
                        soldier.translate(soldier.getDestination().get(0).coords.x - vX, soldier.getDestination().get(0).coords.y - vY);
                         */
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
                entityHp.setText(s.getHp());
                buttonRecruit.setVisible(false);

                //todo braucht seine eigene forEach !!
                entityName.setText("Soldier");
                Sprite sprite = new Sprite(uiAtlas.findRegion("button-normal"));
                sprite.setColor(s.getTeam().getColor());
                sprite.setSize(s.getHp(), 10);
                sprite.setPosition(sprite.getX() + 5, sprite.getY() + 60);
                sprite.draw(renderer.getBatch());
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

        TextButton backButton = new TextButton("Back", skin);
        TextButton backButton2 = new TextButton("Back", skin);

        TextButton exitButton = new TextButton("Back", skin);
        Window windowNoVillager = new Window("NoVillager", skin, "border");
        windowNoVillager.setVisible(false);
        windowNoVillager.setMovable(false);
        windowNoVillager.add(new Label("You have not enough Villager to recruit a Soldier", skin)).padTop(20f).padRight(10f).padLeft(10f).row();
        windowNoVillager.add(backButton);

        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                windowNoVillager.setVisible(false);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Window windowNoGold = new Window("", skin, "border");
        windowNoGold.setVisible(false);
        windowNoGold.setMovable(false);
        windowNoGold.add(new Label("You have not enough Gold", skin)).padTop(20f).padRight(10f).padLeft(10f).row();
        windowNoGold.add(backButton2);

        backButton2.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                windowNoGold.setVisible(false);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Window windowExit = new Window("Surrender?", skin, "border");
        windowExit.setMovable(false);
        windowExit.defaults().pad(20f);

        TextButton exitGameButton = new TextButton("Exit", skin);
        exitGameButton.getLabel().setFontScale(3f);

        TextButton addGoldButton = new TextButton("AddGold", skin);
        TextButton takeGoldButton = new TextButton("TakeGold", skin);

        TextButton noButton = new TextButton("No", skin);
        TextButton yesButton = new TextButton("Yes", skin);

        Label exitLabel = new Label("Do you really want to Surrender?", skin);
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
        takeGoldButton.addListener(new InputListener() {
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
        resourceBarWindow.add(soldierLabel).padTop(30f).padBottom(10f).padRight(10f);

        resourceBarWindow.setPosition(0, stage.getHeight());

        exitWindow.add(exitGameButton).padTop(30f);
        exitWindow.setPosition(stage.getWidth(), stage.getHeight());

        entityWindow.add(entityName).padLeft(100f).padBottom(30f).row();

        Label hpLabel = new Label("HP", skin);
        Label atkLabel = new Label("ATK", skin);
        Label defLabel = new Label("DEF", skin);

        buttonRecruit = new TextButton("Recruit", skin);
        buttonIncreaseMaxUnits = new TextButton("Upgrade Max Units", skin);

        buttonRecruit.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (castle.getVillager() != 0 && castle.getGold() - 10 >= 0) {
                    if (soldierArrayList.size() <= castle.getMaxUnits()) {
                        castle.setVillager(castle.getVillager() - 1);
                        castle.setGold(castle.getGold() - 10); //todo Gold wert nicht hartCoded!!

                        soldierSprite.setColor(castle.getTeam().getColor());
                        Soldier soldier = new Soldier(soldierSprite, collisionUnitLayer, castle.getTeam());
                        soldierArrayList.add(soldier);

                    }
                } else {
                    windowNoVillager.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
                    windowNoVillager.setVisible(true);
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
                    windowNoGold.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
                    windowNoGold.setVisible(true);
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
        entityWindow.add(buttonRecruit).padRight(30f).row();
        entityWindow.add(defLabel);
        entityWindow.add(buttonIncreaseMaxUnits).padRight(30f).row();
        entityWindow.add(entityDEF);
        entityWindow.setPosition(stage.getWidth() / 2 - 300, 0);

        packWindow(resourceBarWindow, stage);
        packWindow(exitWindow, stage);
        packWindow(entityWindow, stage);
        packWindow(windowNoVillager, stage);
        packWindow(windowNoGold, stage);

        gameScreenEvent.StageKeyEvents(windowExit, yesButton, noButton, stage);

        stage.addActor(windowNoVillager);
        stage.addActor(entityWindow);
        stage.addActor(resourceBarWindow);
        stage.addActor(exitWindow);
        stage.addActor(windowNoGold);

        stage.setDebugAll(false);

    }

    //Todo camera movement überarbeiten !!
    private void mouseOnEdgeofCamera() {

        float xClicked, yClicked;

        xClicked = Gdx.input.getX();
        yClicked = Gdx.input.getY();

        //Todo camera movement überarbeiten !!
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

    public GameScreenEvent getGameScreenEvent() {
        return gameScreenEvent;
    }

    /**
     * Checks whether two Hitboxes overlap.
     * @param hitbox the first hitbox
     * @param hitbox2 the second hitbox
     * @return {@code true} if they overlap.
     */
    private boolean hitboxCheckRect(Rectangle hitbox, Rectangle hitbox2) {//did this to reduce copying a bit appreciated advice on how to further reduce
        return hitbox.overlaps(hitbox2);
    }

    /**
     * Checks whether a point is within a hitbox
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
        for (Soldier soldier : soldierArrayList) {
            hitbox = hitboxes.get(soldier.hashCode());
            if (hitboxCheckXY(hitbox, coords)) {//should never happen but better be sure
                soldier.setSelected(!soldier.isSelected());
            } else {
                soldier.setSelected(false);
            }
        }
        for (Castle c : castleArrayList) {
            hitbox = hitboxes.get(c.hashCode());
            if (hitboxCheckXY(hitbox, coords)) {//should never happen but better be sure
                c.setSelected(!c.isSelected());
            } else {
                c.setSelected(false);
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
        float[] mousePos = translateXYCoordinatesFromScreen(Gdx.input.getX(), Gdx.input.getY());
        int x = (int) mousePos[0], y = (int) mousePos[1];
        getPathFinding(v, x, y);
    }

    public void getPathFinding(Soldier v, int xTile, int yTile) {
        //todo pathfinding programmieren
        TiledMapTileLayer pathingCollisionMap = collisionUnitLayer;
        //pathingCollisionMap.getCell((int) (v.getX()+32)/64, (int) (v.getY()+32)/64).getTile().getProperties().clear();
        HashMap<Integer, Rectangle> tempHitboxes = hitboxes;
        tempHitboxes.remove(v.hashCode());
        PathCell p = new Pathfinding(xTile, yTile, (int) v.getX() + 32, (int) v.getY() + 32, pathingCollisionMap, tempHitboxes).algorithm();
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

    public void countPoints(float delta) {
        pointTimerCounter += delta;
        if (pointTimerCounter > 1) { //1 second update
            gameScreenEvent.sendPointRequest();//TODO move this to server???
            pointTimerCounter = 0;
        }
    }

    private ArrayList<String> ArrayToArraylist(String[] strings) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(strings));
        return arrayList;
    }

}
