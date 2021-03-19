package code.lordofwar.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;


public class Unit extends Sprite {

    private Vector2 velocity = new Vector2();

    private float speed = 60 * 2;

    private TiledMapTileLayer collisionLayer;

    public Unit(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        // System.out.println("X: " + getX() + "Y: " + getY());
        super.draw(batch);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    private void update(float deltaTime) {

        //Todo https://youtu.be/DOpqkaX9844 Fix Colloison

        if (velocity.x > speed && velocity.y > speed) {
            velocity.x = speed;
            velocity.y = speed;
        }

        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX= false, collisionY = false;

        setX(getX() + velocity.x * deltaTime);

        //Todo timer um nur für 2 sec die Unit von dem Map ende weg zu schieben
        if(getX() <= 0 ){
            Vector2 vX =  new Vector2();
            vX.x = 0;
            setVelocity(vX);
        }

        if(getY() <= 0 ){
            Vector2 vY =  new Vector2();
            vY.y = 0;
            setVelocity(vY);
        }

        if (velocity.x < 0) {

            //Top left
            collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
            //Middel left
            if(!collisionX)
            collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
            //Bottem left
            if(!collisionX)
            collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

        } else if (velocity.x > 0) {

            //Top right
            collisionX = collisionLayer.getCell((int) ((getX() + getRegionWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
            //Middel right
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            //Bottem right
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");

        }


        if(collisionX){
            setX(oldX);
            velocity.x = 0;
        }

        setY(getY() + velocity.y * deltaTime);

        if (velocity.y < 0) {

            //bottom left
            collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY()  / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
            //bottom Middel
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() / getWidth() / 2) / tileWidth ), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            //bottem right
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");


        } else if (velocity.y > 0) {

            //Top right
            collisionY = collisionLayer.getCell((int) (getX()  / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
            //Middel right
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            //Bottem right
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");

        }

        if(collisionY){
          setY(oldY);
          velocity.y = 0;
        }



    }
}
