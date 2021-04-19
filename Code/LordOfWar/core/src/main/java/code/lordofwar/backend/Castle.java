package code.lordofwar.backend;

public class Castle {
    private boolean selected;
    private int villager;

    public Castle() {

        villager = 0;
        increaseVilligerPerMinute();
    }


    private void increaseVilligerPerMinute() {
        new Thread(() -> {
            try {
                villager = villager + 1;
                Thread.sleep(60000);
                increaseVilligerPerMinute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void setVillager(int villager) {
        this.villager = villager;
    }

    public int getVillager() {
        return villager;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
