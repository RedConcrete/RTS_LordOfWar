package code.lordofwar.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;


public class Castle extends Sprite {
    private boolean selected;
    private int villager;
    private int hp;
    private TiledMapTileLayer collisionLayer;

    public Castle(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
        villager = 0;
        hp = 50;
        increaseVilligerPerMinute();
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    private void update(float deltaTime) {
        setPosition(getX() ,getY());
    }

    private void increaseVilligerPerMinute() {
        new Thread(() -> {
            try {
                villager = villager + 1;
                Thread.sleep(60000);
                increaseVilligerPerMinute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void setVillager(int villager) {
        this.villager = villager;
    }

    public int getVillager() {
        return villager;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }
}
