package code.lordofwar.backend;


import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

    public Pathfinding(int xClicked, int yClicked, int xPosUnit, int yPosUnit, TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
        getStartAndEndCell(xPosUnit, yPosUnit, xClicked, yClicked);
        if (!checkPossible(xClicked, yClicked)) {

            cache = new Vector2(xStartCell, yStartCell);
        } else {

            System.out.println("impossible");
        }
    }

    private boolean checkPossible(int x, int y) {
        int dest = 0;
        int org = getCellDistancesToStart(x, y);
        int sum = dest + org;

        return traversable(new PathCell(new Vector2(x, y), new int[]{sum, org, dest}, null));
    }

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
        PathCell current = new PathCell(cache, new int[]{getCellDistancesToEnd(cache.x, cache.y), 0, getCellDistancesToEnd(cache.x, cache.y)}, null);
        HashMap<Vector2, PathCell> closed = new HashMap<>();
        HashMap<Vector2, PathCell> open = new HashMap<>();
        //HashSet<PathCell> open = new HashSet<>();

        System.out.println(xEndCell + " " + yEndCell);
        while (current != null) {
            System.out.println(current.coords);
            if (!(current.coords.x == xEndCell && current.coords.y == yEndCell)) {
                open.remove(current.coords);
                closed.put(current.coords, current);
                for (Map.Entry<Vector2, int[]> entry : getSurrounding(current.coords).entrySet()) {
                    //TODO current is not goal
                    PathCell newCell = new PathCell(entry.getKey(), entry.getValue(), current);
                    if (traversable(newCell)) {//TODO CHECK IF TRAVERSABLE BEFORE THIS
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
                int lowestSUM = Integer.MAX_VALUE;
                int lowestDST = Integer.MAX_VALUE;
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
                System.out.println("TEST SUCCESS");
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
                    if (!properties.containsKey("isCastle")) {//todo change this to generic blocked
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private HashMap<Vector2, int[]> getSurrounding(Vector2 current) {
        HashMap<Vector2, int[]> returnMap = new HashMap<>();
        if (current.x != xEndCell || current.y != yEndCell) {
            Vector2[] surrounding = new Vector2[]{
                    new Vector2(current.x + 1, current.y + 1),//rechtsOben
                    new Vector2(current.x + 1, current.y),      //rechtsMitte
                    new Vector2(current.x + 1, current.y - 1), //rechtsUnten
                    new Vector2(current.x - 1, current.y + 1),//linksOben
                    new Vector2(current.x - 1, current.y),        //linksMitte
                    new Vector2(current.x - 1, current.y - 1), //linkesUnten
                    new Vector2(current.x, current.y + 1),      //obenMitte
                    new Vector2(current.x, current.y - 1)      //untenMitte
            };
            for (Vector2 vector : surrounding) {
                int dest = getCellDistancesToEnd(vector.x, vector.y);
                int org = getCellDistancesToStart(vector.x, vector.y);
                int sum = dest + org;
                returnMap.put(vector, new int[]{sum, org, dest});
            }
        }
        return returnMap;
    }

    public int getCellDistancesToStart(float x, float y) {
        double calculatiedX = x - xStartCell;
        double calculatiedY = y - yStartCell;
        double squareSum = Math.pow(calculatiedX, 2) + Math.pow(calculatiedY, 2);
        double cellDistance = Math.sqrt(squareSum);
        return (int) cellDistance;
    }

    public int getCellDistancesToEnd(float x, float y) {
        double xCalced = x - xEndCell;
        double yCalced = y - yEndCell;
        double squareSum = Math.pow(xCalced, 2) + Math.pow(yCalced, 2);
        double cellDistance = Math.sqrt(squareSum);
        return (int) cellDistance;
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

