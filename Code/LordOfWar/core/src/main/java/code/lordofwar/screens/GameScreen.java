package code.lordofwar.screens;

import code.lordofwar.backend.Unit;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends Screens implements Screen  {

    private Stage stage;
    private Game game;
    private Skin skin;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private Unit unit;


    public GameScreen(Game aGame, Skin aSkin) {

        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());

        setupUI();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("assets/maps/map_1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        camera =  new OrthographicCamera();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/maps/RTS_UNITS_TILES.txt"));
        unit = new Unit(new Sprite(atlas.findRegion("Character_Green")),(TiledMapTileLayer) map.getLayers().get(0));

        unit.setPosition(unit.getCollisionLayer().getTileWidth(), 2 * unit.getCollisionLayer().getTileHeight());
        Vector2 vector2 = new Vector2();
        vector2.x = 150;
        unit.setVelocity(vector2);
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
}
