package code.lordofwar.backend;

import code.lordofwar.backend.interfaces.AbstractCombatEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 *
 * @author Robin Hefner
 */
public class Soldier extends AbstractCombatEntity {

    public final static String UNIT_TYPE = "SOLDIER";
    private boolean selected;
    private Vector2 velocity = new Vector2();
    private List<PathCell> destination;

    private Sprite sprite;
    private TiledMapTileLayer collisionLayer;


    public Soldier(Sprite sprite, TiledMapTileLayer collisionLayer, Team team) {
        super(sprite, 50, 3, 1, 10, 1, team);
        this.sprite = sprite;
        this.collisionLayer = collisionLayer;
        destination = null;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    private void update(float deltaTime) {
        if (this.getCombatTimer() < this.getCombatTimerLimit()) {
            this.incrementCombatTimer(deltaTime);
        }
        endOfMapCollision();
        setPosition(getX() + velocity.x * deltaTime, getY() + velocity.y * deltaTime);
    }

    private void endOfMapCollision() {
        if (getX() <= 0) {
            setPosition(1, getY());
        } else if (getX() >= collisionLayer.getWidth() * 64 - 100) {
            setPosition(getX() - 10, getY());
        }

        if (getY() <= 0) {
            setPosition(getX(), 1);
        } else if (getY() >= collisionLayer.getHeight() * 64 - 100) {
            setPosition(getX(), getY() - 10);
        }

    }

    @Override
    public String toString() {
        return this.getX() + "," + this.getY();
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public List<PathCell> getDestination() {
        return destination;
    }

    public void setDestination(List<PathCell> destination) {
        this.destination = destination;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void die() {
        this.setAlive(false);
        //TODO do i need to do more here?
    }

    @Override
    public Rectangle getCombatReach() {
        return new Rectangle(getX() - getBoundingRectangle().getWidth(), getY() - getBoundingRectangle().getHeight(), getBoundingRectangle().getWidth() * 3, getBoundingRectangle().getHeight() * 3);
    }

    @Override
    public String getUnitType() {
        return UNIT_TYPE;
    }
}
