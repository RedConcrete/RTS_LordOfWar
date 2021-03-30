package code.lordofwar.screens;

import code.lordofwar.backend.Villager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen extends Screens implements Screen {

    private Stage stage;
    private Game game;
    private Skin skin;
    private Vector2 vectorSpeed;
    public Vector3 posCameraDesired;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    TextureAtlas atlas;
    ArrayList<Villager> villagerArrayList;
    private TiledMapTileLayer collisionUnitLayer;
    int startingVillager = 5;
    TmxMapLoader loader;


    public GameScreen(Game aGame, Skin aSkin) {

        game = aGame;
        skin = aSkin;
        stage = new Stage(new ScreenViewport());
        vectorSpeed = new Vector2();
        posCameraDesired = new Vector3();
        villagerArrayList = new ArrayList<>();
        atlas = new TextureAtlas(Gdx.files.internal("maps/RTS_UNITS_TILES.txt"));
        loader = new TmxMapLoader();
        map = loader.load("maps/map_1.tmx");

        camera = new OrthographicCamera();
        collisionUnitLayer = (TiledMapTileLayer) map.getLayers().get(1);

        setupUI();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/map_1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();

        for (int i = 0; i < startingVillager; i++) {
            Villager villager = new Villager(new Sprite(atlas.findRegion("Character_Green_B")), collisionUnitLayer);
            villagerArrayList.add(villager);
            villager.setPosition(villager.getCollisionLayer().getTileWidth(), i * villager.getCollisionLayer().getTileHeight());
            vectorSpeed.x = 100;
            villager.setVelocity(new Vector2(vectorSpeed));
        }

    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        //es muss alles übermalt werden anders können aneblich die actors nicht entfernt werden :|
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        fps(stage, skin);

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();

        for (Villager v : villagerArrayList) {
            v.draw(renderer.getBatch());
        }

        renderer.getBatch().end();

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

        //Todo später inGame um tasten zu belegen!

        Window gameWindow1 = new Window("left corner top", skin);
        Window gameWindow2 = new Window("test", skin);
//        Window gameWindow3 = new Window("right corner bottom",skin);

        Button exitGameButton = new Button(skin);
        exitGameButton.setSize(exitGameButton.getWidth() * 2, exitGameButton.getHeight() * 2);
        exitGameButton.setPosition(stage.getWidth(), stage.getHeight());

        gameWindow1.setMovable(false);
        gameWindow2.setMovable(false);
//        gameWindow3.setMovable(false);

        exitGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game, skin));
                stage.dispose();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        TextButton backButton = new TextButton("Villager", skin);
        backButton.getLabel().setFontScale(3f);

        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                Villager villager = new Villager(new Sprite(atlas.findRegion("Character_Green_B")), collisionUnitLayer);
                villagerArrayList.add(villager);
                villager.setVelocity(vectorSpeed);
                villager.setPosition(10, 10);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        gameWindow1.add(backButton).row();
        gameWindow1.setPosition(0, stage.getHeight());
        gameWindow1.setSize(stage.getWidth() * 1 / 10, stage.getHeight() * 3 / 10);

        gameWindow2.add(exitGameButton);
        gameWindow2.setPosition(stage.getWidth(), stage.getHeight());
        gameWindow2.setSize(exitGameButton.getWidth() * 3, exitGameButton.getHeight() * 3);
//
//        gameWindow3.setPosition(stage.getWidth() * 2/10,0);
//        gameWindow3.setSize(stage.getWidth()* 2/10,stage.getHeight() * 2/6);

        stage.addActor(gameWindow1);
        stage.addActor(gameWindow2);
//        stage.addActor(gameWindow3);

        stage.setDebugAll(false);
    }
}
