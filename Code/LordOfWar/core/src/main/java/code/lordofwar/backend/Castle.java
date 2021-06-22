package code.lordofwar.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;


public class Castle extends Sprite {
    private boolean selected;
    private int villager;
    private int gold;
    private int hp;
    private int maxUnits;
    private TiledMapTileLayer collisionLayer;
    private Sprite sprite;

    public Castle(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
        this.sprite = sprite;
        villager = 0;
        gold = 99;
        hp = 100;
        maxUnits = 50;
        increaseVilligerPerMinute();
        increaseGoldEveryTenSeconds();
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

    private void increaseGoldEveryTenSeconds() {
        new Thread(() -> {
            try {
                gold = gold + 1;
                Thread.sleep(10000);
                increaseGoldEveryTenSeconds();
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

    public void setGold(int gold) { this.gold = gold; }

    public int getGold() { return gold; }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getMaxUnits() {
        return maxUnits;
    }

    public void setMaxUnits(int maxUnits) {
        this.maxUnits = maxUnits;
    }
}
