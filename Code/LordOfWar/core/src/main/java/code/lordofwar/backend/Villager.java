package code.lordofwar.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Villager extends Sprite {

    private Vector2 velocity = new Vector2();

    private float speed = 60 * 2;

    private TiledMapTileLayer collisionLayer;

    public Villager(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }



    private void update(float deltaTime) {
        endOfMapCollision();
        objectCollision(deltaTime);
    }

    private void endOfMapCollision(){
        if(getX() <= 0 ){
            setPosition(1 ,getY());
        }else if(getX() >= collisionLayer.getWidth()*64-100){
            setPosition(getX()-10 ,getY());
        }

        if(getY() <= 0 ){
            setPosition(getX() ,1);
        }else if (getY() >= collisionLayer.getHeight()*64-100){
            setPosition(getX() ,getY()-10);
        }

    }


    private void objectCollision(float deltaTime){
        //Todo https://youtu.be/DOpqkaX9844 Fix Colloison

        if (velocity.x > speed && velocity.y > speed) {
            velocity.x = speed;
            velocity.y = speed;
        }

        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX= false, collisionY = false;

        setX(getX() + velocity.x * deltaTime);

        if (velocity.x < 0) {

            //Top left
            collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("isCastel");

            //Middel left
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");

            //Bottem left
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");

        } else if (velocity.x > 0) {

            //rechts mitte
            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");

            //Top right
            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("isCastel");

            //Middel right
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");

            //Bottem right
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth())/ tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");

        }

        //react to x coll
        if(collisionX){
            setX(oldX);
            velocity.x = 0;
            System.out.println("collision X");
        }

        // moving in y
        setY(getY() + velocity.y * deltaTime);

        if (velocity.y < 0) {

            //bottom left
            collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY()  / tileHeight))
                    .getTile().getProperties().containsKey("isCastel");
            //bottom Middel
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth ), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");
            //bottem right
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");


        } else if (velocity.y > 0) {

            //Top left
            collisionY = collisionLayer.getCell((int) (getX()  / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("isCastel");
            //Top middel
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()/2) / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");
            //Top right
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("isCastel");

        }

        //react to y coll
        if(collisionY){
            setY(oldY);
            velocity.y = 0;
            System.out.println("collision Y");
        }

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
}
