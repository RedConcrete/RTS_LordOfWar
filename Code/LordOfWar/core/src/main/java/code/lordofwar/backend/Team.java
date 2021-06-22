package code.lordofwar.backend;

import com.badlogic.gdx.graphics.Color;

public class Team {

    private int color;
    private int unitCounter;


    public Team(int color) {
        this.color = color;
    }

    public Color getColor() {
        Color defineColor= new Color();
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


}
