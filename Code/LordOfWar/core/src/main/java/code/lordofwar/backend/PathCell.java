package code.lordofwar.backend;

import com.badlogic.gdx.math.Vector2;

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
