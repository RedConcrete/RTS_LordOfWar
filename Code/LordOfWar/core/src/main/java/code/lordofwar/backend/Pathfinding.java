package code.lordofwar.backend;


import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A* Pathfinding
 */
public class Pathfinding {

    private int diagonalCostMultiplayer;
    private int verticalCostMultiplayer;

    private int xClicked;
    private int yClicked;

    private int xPosUnit;
    private int yPosUnit;

    private int xStartCell;
    private int yStartCell;

    private int xEndCell;
    private int yEndCell;

    private int xCache;
    private int yCache;

    double cellDistance;

    private TiledMapTileLayer collisionLayer;

    private ArrayList<Vector2> cellArr = new ArrayList<>();
    HashMap<Vector2, Integer> cellCostMapSum = new HashMap<>();
    HashMap<Vector2, Integer> cellCostMapHome = new HashMap<>();
    HashMap<Vector2, Integer> cellCostMapDest = new HashMap<>();

    public Pathfinding(int xClicked, int yClicked, int xPosUnit, int yPosUnit, TiledMapTileLayer collisionLayer) {
        diagonalCostMultiplayer = 14;
        verticalCostMultiplayer = 10;

        this.xClicked = xClicked;
        this.yClicked = yClicked;
        this.xPosUnit = xPosUnit;
        this.yPosUnit = yPosUnit;
        this.collisionLayer = collisionLayer;

        getStartAndEndCell();
        xCache = xStartCell;
        yCache = yStartCell;
        running();
    }

    private void running() {
        while (!(xCache == xEndCell && yCache == yEndCell)) {
            fillCellArr();
            calcMoveCost();
            selectNewCacheCell();
        }
        System.out.println("2");
    }

    public void getStartAndEndCell() {
        xStartCell = xPosUnit / 64;
        yStartCell = yPosUnit / 64;

        xEndCell = xClicked / 64;
        yEndCell = yClicked / 64;
    }

    /**
     * https://www.calculatorsoup.com/calculators/geometry-plane/distance-two-points.php
     */
    public int getCellDistancesToStart(int x, int y) {
        double calculatiedX = xStartCell - x;
        double calculatiedY = yStartCell - y;
        double squareSum = Math.pow(calculatiedX, 2) + Math.pow(calculatiedY, 2);
        cellDistance = Math.sqrt(squareSum);
        return (int) cellDistance;
    }

    public int getCellDistancesToEnd(int x, int y) {
        double xCalced = xEndCell - Math.round(x);
        double yCalced = yEndCell - Math.round(y);
        double squareSum = Math.pow(xCalced, 2) + Math.pow(yCalced, 2);
        cellDistance = Math.sqrt(squareSum);
        return (int) cellDistance;
    }


    public void checkMoveableCell() {
        for (Vector2 v : cellArr) {

            if (v.x >= 0 && v.y >= 0) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) v.x, (int) v.y);
                TiledMapTile tile = cell.getTile();
                MapProperties properties = tile.getProperties();

                if (properties.containsKey("isCastle")) {
                    //Todo costen der cell sehr hoch machen
                }
            } else {
                //todo del diese Werte aus der Arraylist
            }
        }
    }

    public void fillCellArr() {
        if (xCache != xEndCell || yCache != yEndCell) {
            cellArr.clear();
            cellArr.add(new Vector2(xCache + 1, yCache + 1));  //rechtsOben
            cellArr.add(new Vector2(xCache + 1, yCache));        //rechtsMitte
            cellArr.add(new Vector2(xCache + 1, yCache - 1));  //rechtsUnten

            cellArr.add(new Vector2(xCache - 1, yCache + 1));  //linksOben
            cellArr.add(new Vector2(xCache - 1, yCache));        //linksMitte
            cellArr.add(new Vector2(xCache - 1, yCache - 1));  //linkesUnten

            cellArr.add(new Vector2(xCache, yCache + 1));        //obenMitte
            cellArr.add(new Vector2(xCache, yCache - 1));        //untenMitte
            System.out.println("X " + xCache + "| Y " + yCache);

        } else {
            System.out.println("ALLA ich bin am ZIel!!!!!!!!!!!!!!!!!!!!");
        }

    }

    public void calcMoveCost() {
        cellCostMapSum.clear();
        int homeCost = 0;
        int destCost = 0;
        int sumCost;

        checkMoveableCell();

        for (Vector2 v : cellArr) {
            if (xStartCell != (int) v.x && yStartCell != (int) v.y) {
                homeCost = getCellDistancesToStart((int) v.x, (int) v.y) * diagonalCostMultiplayer;

            } else {
                homeCost = getCellDistancesToStart((int) v.x, (int) v.y) * verticalCostMultiplayer;

            }

            if (xEndCell != (int) v.x && yEndCell != (int) v.y) {
                destCost = getCellDistancesToEnd((int) v.x, (int) v.y) * diagonalCostMultiplayer;
            } else {
                destCost = getCellDistancesToEnd((int) v.x, (int) v.y) * verticalCostMultiplayer;
            }

            sumCost = homeCost + destCost;

            cellCostMapSum.put(v, sumCost);
            cellCostMapHome.put(v, homeCost);
            cellCostMapDest.put(v, destCost);
        }

    }

    public void selectNewCacheCell() {
        Vector2 vectorCache = null;
        int lowest = Integer.MAX_VALUE;
        int homeCost = Integer.MAX_VALUE;
        int destCost = Integer.MAX_VALUE;
        boolean lower;

        for (Map.Entry<Vector2, Integer> entry : cellCostMapSum.entrySet()) {

            if (lowest == entry.getValue()) {
                if (destCost > cellCostMapDest.get(entry.getKey())) {
                    lower = true;
                } else {
                    if (destCost == cellCostMapDest.get(entry.getKey())) {
                        if (homeCost > cellCostMapHome.get(entry.getKey())) {
                            lower = true;
                        }
                        else {
                            lower = false;
                        }
                    } else {
                        lower = false;
                    }
                }
            } else {
                lower = lowest > entry.getValue();
            }

            if (lower) {
                lowest = entry.getValue();
                homeCost = cellCostMapHome.get(entry.getKey());
                destCost = cellCostMapDest.get(entry.getKey());
                vectorCache = entry.getKey();
            }
        }

        if (vectorCache != null) {
            xCache = (int) vectorCache.x;
            yCache = (int) vectorCache.y;
        }
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    public ArrayList getCellArr() {
        return cellArr;
    }

    public void setCellArr(ArrayList cellArr) {
        this.cellArr = cellArr;
    }
}
