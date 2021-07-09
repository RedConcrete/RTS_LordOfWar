package code.lordofwar.backend;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a cell. Defined by its Coordinates, its distance to start and end, its total cost and its preceeding cell.
 * @author Cem Arslan
 */
public class PathCell {
    public Vector2 coords;
    public double[] distances;
    //use this value to get most efficient path after arriving at the goal
    public PathCell parent;

    public PathCell(Vector2 coords, double[] distances, PathCell parent) {
        this.coords = coords;
        this.distances = distances;
        this.parent = parent;
    }
}
