package code.lordofwar.screens;

import code.lordofwar.backend.Unit;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends Screens implements Screen  {

    private Stage stage;
    private Game game;
    private Skin skin;
    private Vector2 vectorSpeed;
    public Vector3 posCameraDesired;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private Unit unit;


    public GameScreen(Game aGame, Skin aSkin) {

        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        vectorSpeed = new Vector2();
        posCameraDesired = new Vector3();

        setupUI();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/map_1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        camera =  new OrthographicCamera();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("maps/RTS_UNITS_TILES.txt"));
        unit = new Unit(new Sprite(atlas.findRegion("Character_Green")),(TiledMapTileLayer) map.getLayers().get(0));

        unit.setPosition(10 * unit.getCollisionLayer().getTileWidth(),10 * unit.getCollisionLayer().getTileHeight());

        //vector2.x = -100;
        //vector2.y = -100;
        unit.setVelocity(vectorSpeed);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        //es muss alles übermalt werden anders können aneblich die actors nicht entfernt werden :|
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT |  GL20.GL_DEPTH_BUFFER_BIT );

        fps(stage,skin);

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        unit.draw(renderer.getBatch());
        renderer.getBatch().end();

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            onMouseDown();
        }


        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.position.set(width/2f, height/2f, 0);
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

    private void setupUI(){


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

        Label label = new Label("GameScreen", skin);
        label.setFontScale(4f);
        label.setX(Gdx.graphics.getWidth()*7f/16f);
        label.setY(Gdx.graphics.getHeight()/2f);
        stage.addActor(label);

        Window gameWindow1 = new Window("",skin);
        Window gameWindow2 = new Window("",skin);
        Window gameWindow3 = new Window("",skin);

        gameWindow1.setMovable(false);
        gameWindow2.setMovable(false);
        gameWindow3.setMovable(false);

        gameWindow1.setPosition(0,0);
        gameWindow1.setSize(stage.getWidth() * 1/10,stage.getHeight());
        gameWindow2.setPosition(0,0);
        gameWindow2.setSize(stage.getWidth(),stage.getHeight() * 1/6);
        gameWindow3.setPosition(stage.getWidth() * 8/10,0);
        gameWindow3.setSize(stage.getWidth()* 2/10,stage.getHeight() * 2/6);


        stage.addActor(gameWindow1);
        stage.addActor(gameWindow2);
        stage.addActor(gameWindow3);

        stage.setDebugAll(false);
    }

    private void onMouseDown() {
        float xClicked, yClicked;

        xClicked = Gdx.input.getX();
        yClicked = Gdx.input.getY();

        System.out.println(" X: " + xClicked  + " Y: " + yClicked );
        System.out.println(unit.getX());

        //Todo camera movement überarbeiten !! map bewegt sich nicht mit so wie alle anderen objekte
        //processCameraMovement(xClicked,yClicked);
        //camera.position.lerp(posCameraDesired,0.1f);//vector of the camera desired position and smoothness of the movement

    }

    private void processCameraMovement(float xClicked, float yClicked){
        if(xClicked <= camera.viewportWidth / 2 && yClicked >= camera.viewportHeight / 2){
            posCameraDesired.x -= 100.0f * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= 100.0f * Gdx.graphics.getDeltaTime();
            System.out.println("links unten");
        }
        if(xClicked >= camera.viewportWidth / 2 && yClicked <= camera.viewportHeight / 2){
            posCameraDesired.x += 100.0f * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += 100.0f * Gdx.graphics.getDeltaTime();
            System.out.println("rechts oben");
        }
        if(xClicked >= camera.viewportWidth / 2 && yClicked >= camera.viewportHeight / 2){
            posCameraDesired.x -= 100.0f * Gdx.graphics.getDeltaTime();
            posCameraDesired.y += 100.0f * Gdx.graphics.getDeltaTime();
            System.out.println("rechts unten");
        }
        if(xClicked <= camera.viewportWidth / 2 && yClicked <= camera.viewportHeight / 2){
            posCameraDesired.x += 100.0f * Gdx.graphics.getDeltaTime();
            posCameraDesired.y -= 100.0f * Gdx.graphics.getDeltaTime();
            System.out.println("links oben");
        }
    }
}
