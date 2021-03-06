package code.lordofwar.backend;

import code.lordofwar.backend.interfaces.AbstractCombatEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

/**
 * The Class Backgroundmusic is supposed to play Backgroundmusik in the menu as well as in the Game itself.
 * @author Franz Klose,Robin Hefner,Cem Arslan
 */
public class Castle extends AbstractCombatEntity {
    public final static String UNIT_TYPE = "CASTLE";
    private boolean selected;
    private int villager;
    private int gold;
    private int maxUnits;
    private TiledMapTileLayer collisionLayer;
    private Sprite sprite;

    public Castle(Sprite sprite, TiledMapTileLayer collisionLayer, Team team) {
        super(sprite, 100, 6, 1, 0, 0, team);
        this.collisionLayer = collisionLayer;
        this.sprite = sprite;
        villager = 1;
        gold = 99;
        maxUnits = 50;
        increaseVillagerPerMinute();
        increaseGoldEveryTenSeconds();
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    private void update(float deltaTime) {
        setPosition(getX(), getY());
    }

    /**
     * increases the Villager count every min
     */
    private void increaseVillagerPerMinute() {
        new Thread(() -> {
            try {
                villager = villager + 1;
                Thread.sleep(60000);
                increaseVillagerPerMinute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * increases the Gold every 10 secs
     */
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

    @Override
    public String toString() {
        return this.getX() + "," + this.getY();
    }

    public void setVillager(int villager) {
        this.villager = villager;
    }

    public int getVillager() {
        return villager;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return gold;
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

    public Sprite getSprite() {
        return sprite;
    }

    public int getMaxUnits() {
        return maxUnits;
    }

    public void setMaxUnits(int maxUnits) {
        this.maxUnits = maxUnits;
    }

    @Override
    public void die() {
        this.setAlive(false);
    }

    @Override
    public Rectangle getCombatReach() {
        return null;
    }//this isnt a combat unit so it shouldnt be capable of attacking

    @Override
    public String getUnitType() {
        return UNIT_TYPE;
    }
}
