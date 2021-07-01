package code.lordofwar.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Soldier extends Sprite {

    private boolean selected;
    private Vector2 velocity = new Vector2();
    private float speed = 60 * 2;
    private int hp;
    private List<PathCell> destination;

    private Team team;
    private Sprite sprite;
    private TiledMapTileLayer collisionLayer;

    public Soldier(Sprite sprite, TiledMapTileLayer collisionLayer, Team team) {
        super(sprite);
        this.sprite = sprite;
        this.collisionLayer = collisionLayer;
        hp = 50;
        destination = null;
        this.team = team;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    private void update(float deltaTime) {
        endOfMapCollision();
        setPosition(getX() + velocity.x * deltaTime,getY()+ velocity.y * deltaTime);
        objectCollision(deltaTime);
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


    private void objectCollision(float deltaTime) {
//
//        //Todo https://youtu.be/DOpqkaX9844 Fix Colloison



//
//        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
//        boolean collisionX= false, collisionY = false;
//

//
//        if (velocity.x < 0) {
//
//            //Top left
//            collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
//                    .getTile().getProperties().containsKey("isCastle");
//
//            //Middel left
//            if(!collisionX)
//                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//
//            //Bottem left
//            if(!collisionX)
//                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//
//        } else if (velocity.x > 0) {
//
//            //rechts mitte
//            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
//
//            //Top right
//            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
//                    .getTile().getProperties().containsKey("isCastle");
//
//            //Middel right
//            if(!collisionX)
//                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//
//            //Bottem right
//            if(!collisionX)
//                collisionX = collisionLayer.getCell((int) ((getX() + getWidth())/ tileWidth), (int) (getY() / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//
//        }
//
//        //react to x coll
//        if(collisionX){
//            setX(oldX);
//            velocity.x = 0;
//        }
//
//        // moving in y

//
//
//        if (velocity.y < 0) {
//
//            //bottom left
//            collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY()  / tileHeight))
//                    .getTile().getProperties().containsKey("isCastle");
//            //bottom Middel
//            if(!collisionY)
//                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth ), (int) (getY() / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//            //bottem right
//            if(!collisionY)
//                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//
//
//        } else if (velocity.y > 0) {
//
//            //Top left
//            collisionY = collisionLayer.getCell((int) (getX()  / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
//                    .getTile().getProperties().containsKey("isCastle");
//            //Top middel
//            if(!collisionY)
//                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()/2) / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//            //Top right
//            if(!collisionY)
//                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
//                        .getTile().getProperties().containsKey("isCastle");
//
//        }
//
//        //react to y coll
//        if(collisionY){
//            setY(oldY);
//            velocity.y = 0;
//        }
//
    }

    @Override
    public String toString() {
        return this.getX() +","+  this.getY();
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public List<PathCell> getDestination() {
        return destination;
    }

    public void setDestination(List<PathCell> destination) {
        this.destination = destination;
    }

    public Team getTeam() { return team; }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() { return sprite; }
}
