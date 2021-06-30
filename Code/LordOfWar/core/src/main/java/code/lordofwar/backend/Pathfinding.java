package code.lordofwar.backend;


import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * A* Pathfinding
 */
public class Pathfinding {

    private static final int diagonalCostMultiplayer = 14;
    private static final int verticalCostMultiplayer = 10;

    private int xStartCell;
    private int yStartCell;

    private int xEndCell;
    private int yEndCell;

    private Vector2 cache;

    private TiledMapTileLayer collisionLayer;
    private HashMap<Integer, Rectangle> hitboxes;

    public Pathfinding(int xClicked, int yClicked, int xPosUnit, int yPosUnit, TiledMapTileLayer collisionLayer, HashMap<Integer, Rectangle> hitboxes) {
        this.collisionLayer = collisionLayer;
        this.hitboxes = hitboxes;
        getStartAndEndCell(xPosUnit, yPosUnit, xClicked, yClicked);
        //if (!checkPossible(xClicked, yClicked)) {

        cache = new Vector2(xStartCell, yStartCell);
        //} else {

        //System.out.println("impossible");
        //}
    }
/*
    private boolean checkPossible(int x, int y) {
        double dest = 0;
        double org = getCellDistancesToStart(x, y);
        double sum = dest + org;

        return traversable(new PathCell(new Vector2(x, y), new double[]{sum, org, dest}, null));
    }
*/

    /**
     * Starts the pathfinding algorithm.
     *
     * @return {@link PathCell} of the endcell. Use {@link PathCell} to get route to walk.
     * NOTE: since you begin from the last cell order has to be reversed
     * can return {@code null} if something goes wrong with the logic
     * pretty sure that should never happen thought
     */
    public PathCell algorithm() {

        //todo wenn man auf x:0 und y:0 drückt crashed es hier!!!
        PathCell current = new PathCell(cache, new double[]{getCellDistancesToEnd(cache.x, cache.y), 0, getCellDistancesToEnd(cache.x, cache.y)}, null);
        HashMap<Vector2, PathCell> closed = new HashMap<>();
        HashMap<Vector2, PathCell> open = new HashMap<>();
        //HashSet<PathCell> open = new HashSet<>();

        //System.out.println(xEndCell + " " + yEndCell);
        while (current != null) {
            //System.out.println(current.coords);
            if (!(current.coords.x == xEndCell && current.coords.y == yEndCell)) {
                open.remove(current.coords);
                closed.put(current.coords, current);
                for (Map.Entry<Vector2, double[]> entry : getSurrounding(current.coords).entrySet()) {
                    //TODO current is not goal
                    PathCell newCell = new PathCell(entry.getKey(), entry.getValue(), current);
                    if (traversable(newCell)) {
                        if (closed.containsKey(newCell.coords)) {//should return smthing since equals same vector works also
                            PathCell oldCell = closed.get(newCell.coords);
                            if (newCell.distances[0] < oldCell.distances[0] || newCell.distances[2] < oldCell.distances[2]) {
                                open.put(newCell.coords, newCell);//path is shorther; thus added to open
                            }//skip to next
                        } else {//point is not contained in closed
                            open.put(newCell.coords, newCell);  //should work since i overrode .equals
                        }
                    }
                }
                double lowestSUM = Integer.MAX_VALUE;
                double lowestDST = Integer.MAX_VALUE;
                PathCell newCurrent = null;
                for (Map.Entry<Vector2, PathCell> entry : open.entrySet()) {
                    if (lowestSUM == entry.getValue().distances[0]) {
                        if (lowestDST > entry.getValue().distances[2]) {
                            //sum already the same
                            lowestDST = entry.getValue().distances[2];
                            newCurrent = entry.getValue();
                        }
                    } else {
                        if (lowestSUM > entry.getValue().distances[0]) {
                            lowestSUM = entry.getValue().distances[0];
                            lowestDST = entry.getValue().distances[2];
                            newCurrent = entry.getValue();
                        }
                    }
                }
                current = newCurrent;
            } else {
                //System.out.println("TEST SUCCESS");
                return current;//TODO CURRENT IS GOAL
            }
        }

        return null;//something went very very wrong
    }

    /**
     * Checks if the pathCell is traversable.
     *
     * @param pathCell the pathCell to be checked
     * @return {@code true} if pathCell can be traversed
     */
    private boolean traversable(PathCell pathCell) {
        //implement code to check wether the pathCell is traversable
        if (pathCell.coords.x >= 0 && pathCell.coords.y >= 0) {
            TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) pathCell.coords.x, (int) pathCell.coords.y);
            if (cell != null) {
                TiledMapTile tile = cell.getTile();
                if (tile != null) {
                    MapProperties properties = tile.getProperties();
                    if (!properties.containsKey(Constants.BLOCK_TILE_PROPERTY)) {//todo change this to generic blocked
                        for (Rectangle hitbox : hitboxes.values()) {
                            if (hitbox.overlaps(//check wether hitbox overlaps with tile
                                    new Rectangle(((int) pathCell.coords.x) * collisionLayer.getTileWidth()-2, ((int) pathCell.coords.y) * collisionLayer.getTileHeight()-2,
                                            collisionLayer.getTileWidth() - 2, collisionLayer.getTileHeight() - 2))) {//todo put this offset into a variable?

                                //System.out.println(pathCell.coords.x + " " + pathCell.coords.y + " BLOCKED");
                                return false;
                            }
                        }

                        return true;
                    }
                }
            }
        }
        //System.out.println(pathCell.coords.x + " " + pathCell.coords.y + " BLOCKED");
        return false;
    }

    private HashMap<Vector2, double[]> getSurrounding(Vector2 current) {
        HashMap<Vector2, double[]> returnMap = new HashMap<>();

        if (current.x != xEndCell || current.y != yEndCell) {
            //alright need to edit this to disallow diagonal movement cause it doesnt look right
            Vector2[] surrounding = new Vector2[8];
            boolean up = false;
            boolean down = false;
            boolean right = false;
            boolean left = false;
            surrounding[0]=new Vector2(current.x, current.y + 1);
            PathCell tempPathCell=new PathCell(surrounding[0],null,null);
            if (traversable(tempPathCell)) {
                up=true;
            }
            surrounding[1]=new Vector2(current.x, current.y - 1);
            tempPathCell.coords=surrounding[1];
            if (traversable(tempPathCell)){
                down=true;
            }
            surrounding[2]=new Vector2(current.x + 1, current.y);
            tempPathCell.coords=surrounding[2];
            if (traversable(tempPathCell)){
                right=true;
            }
            surrounding[3]=new Vector2(current.x - 1, current.y);
            tempPathCell.coords=surrounding[3];
            if (traversable(tempPathCell)){
                left=true;
            }

           if (up){
               if (right){
                   surrounding[4]=new Vector2(current.x + 1, current.y + 1);
               }
               if (left){
                   surrounding[5]=new Vector2(current.x - 1, current.y + 1);
               }
           }
           if (down){
               if (right){
                   surrounding[6]=new Vector2(current.x + 1, current.y - 1);
               }
               if (left){
                   surrounding[7]=new Vector2(current.x - 1, current.y - 1);
               }
           }
            for (Vector2 vector : surrounding) {
                if (vector != null) {//check to filter null objects
                    double dest = getCellDistancesToEnd(vector.x, vector.y);
                    double org = getCellDistancesToStart(vector.x, vector.y);
                    double sum = dest + org;
                    returnMap.put(vector, new double[]{sum, org, dest});
                }
            }
        }
        return returnMap;
    }

    public double getCellDistancesToStart(float x, float y) {
        double calculatiedX = x - xStartCell;
        double calculatiedY = y - yStartCell;
        double squareSum = Math.pow(calculatiedX, 2) + Math.pow(calculatiedY, 2);
        return Math.sqrt(squareSum);
    }

    public double getCellDistancesToEnd(float x, float y) {
        double xCalced = x - xEndCell;
        double yCalced = y - yEndCell;
        double squareSum = Math.pow(xCalced, 2) + Math.pow(yCalced, 2);
        return Math.sqrt(squareSum);
    }

    public void getStartAndEndCell(int xPosUnit, int yPosUnit, int xClicked, int yClicked) {
        xStartCell = xPosUnit / 64;
        yStartCell = yPosUnit / 64;

        xEndCell = xClicked / 64;
        yEndCell = yClicked / 64;
    }

    /**
     * https://www.calculatorsoup.com/calculators/geometry-plane/distance-two-points.php
     */
//    public void checkMoveableCell() {
//        for (Vector2 v : cellArr) {
//
//            if (v.x >= 0 && v.y >= 0) {
//                TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) v.x, (int) v.y);
//                TiledMapTile tile = cell.getTile();
//                MapProperties properties = tile.getProperties();
//
//                if (properties.containsKey("isCastle")) {
//                    //Todo costen der cell sehr hoch machen
//                }
//            } else {
//                //todo del diese Werte aus der Arraylist
//            }
//        }
//    }
    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

}

