package code.lordofwar.screens;

import code.lordofwar.backend.Unit;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends Screens implements Screen {

    private final Stage stage;
    private final Game game;
    private final Skin skin;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Vector3 posCameraDesired;
    private Unit unit;
    private static final ShapeRenderer debugRenderer = new ShapeRenderer();
    private boolean isCameraDebug;


    public GameScreen(Game aGame, Skin aSkin) {

        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        posCameraDesired = new Vector3();
        isCameraDebug = true;
        setupUI();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("assets/maps/map_1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/maps/RTS_UNITS_TILES.txt"));
        unit = new Unit(new Sprite(atlas.findRegion("Character_Green_B")), (TiledMapTileLayer) map.getLayers().get(0));

        unit.setPosition(unit.getCollisionLayer().getTileWidth(), 2 * unit.getCollisionLayer().getTileHeight());
        Vector2 vector2 = new Vector2();
        //vector2.x = 120;
        vector2.y = -200;
        unit.setVelocity(vector2);


    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        fps(stage, skin);

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        unit.draw(renderer.getBatch());
        renderer.getBatch().end();


        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            mouseOnEdgeofCamera();
        }

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
        Window gameWindow2 = new Window("", skin);
        Window gameWindow3 = new Window("", skin);

        gameWindow1.setMovable(false);
        gameWindow2.setMovable(false);
        gameWindow3.setMovable(false);

        gameWindow1.setPosition(0, 0);
        gameWindow1.setSize(stage.getWidth() * 1 / 10, stage.getHeight());
        gameWindow2.setPosition(0, 0);
        gameWindow2.setSize(stage.getWidth(), stage.getHeight() * 1 / 6);
        gameWindow3.setPosition(stage.getWidth() * 8 / 10, 0);
        gameWindow3.setSize(stage.getWidth() * 2 / 10, stage.getHeight() * 2 / 6);

        //stage.addActor(gameWindow1);
        //stage.addActor(gameWindow2);
        //stage.addActor(gameWindow3);

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
     *  The Method processCameraMovement moves the Camera in the direction the mouse is pointing at.
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

    public void checkCameraBorder() {
        //TODO kamera muss überprüft werden ob sie noch innerhalb der Map ist!
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
}
