package code.lordofwar.backend;

import com.badlogic.gdx.graphics.Color;

/**
 * {@link Team} defines a team. A Team is defined by its color and starting Postion.
 *
 * @author Robin Hefner,Franz Klose
 */
public class Team {

    private int color;
    private int unitCounter;
    private int startingPos;

    public Team(int color) {
        this.color = color;
        startingPos = color;
    }

    /**
     * gives the color for a specified a startigpos back
     *
     * @return
     */
    public Color getColor() {
        Color defineColor = new Color();
        switch (color) {
            case 0:
                defineColor.set(Color.RED);
                break;
            case 1:
                defineColor.set(Color.BLUE);
                break;
            case 2:
                defineColor.set(Color.GREEN);
                break;
            case 3:
                defineColor.set(Color.YELLOW);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + color);
        }
        return defineColor;
    }

    public int getUnitCounter() {
        return unitCounter;
    }

    public void setUnitCounter(int unitCounter) {
        this.unitCounter = unitCounter;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStartingPos() {
        return startingPos;
    }

    public void setStartingPos(int startingPos) {
        this.startingPos = startingPos;
    }
}
