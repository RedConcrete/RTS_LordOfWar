package de.constants;

public enum PointRewards {
    KILL_VILLAGER(50), KILL_SOLDIER(100), DESTROY_CASTLE(1000),
    WIN_GAME(10000), LOSE_GAME(0);
    private final int pointReward;

    PointRewards(int reward) {
        this.pointReward = reward;
    }

    public int getPointReward() {
        return pointReward;
    }
}
