package code.lordofwar.backend.interfaces;

import code.lordofwar.backend.Team;
import com.badlogic.gdx.graphics.g2d.Sprite;
import kotlin.Pair;

/**
 * Reduces logic repetition for the combatUnit entities
 * @author Franz Klose,Robin Hefner,Cem Arslan
 */
public abstract class AbstractCombatEntity extends Sprite implements CombatEntity {
    private int maxHP;
    private int hp;
    private int def;
    private boolean alive;
    private Pair<Integer, Integer> damagePair;
    private CombatEntity target;
    private float combatTimer;
    private float combatTimerLimit;
    private Team team;

    public AbstractCombatEntity(Sprite sprite, int maxHP, int def, Integer dmgType, Integer atk, float combatTimerLimit,Team team) {
        super(sprite);
        this.maxHP = maxHP;
        this.combatTimerLimit = combatTimerLimit;
        this.team=team;
        this.def = def;
        hp = maxHP;
        alive = true;
        damagePair = new Pair<>(dmgType, atk);
        target = null;
        combatTimer = combatTimerLimit;
    }

    @Override
    public Pair<Integer, Integer> dealDmg() {
        combatTimer = 0;
        return damagePair;
    }

    /**
     *
     * @param incomingDMG the Damage type and amount of incoming dmg.
     */
    @Override
    public void receiveDmg(Pair<Integer, Integer> incomingDMG) {
        double dmg;
        //true dmg until changed later
        if (incomingDMG.getFirst() == 1) { //normal dmg
            dmg = incomingDMG.getSecond() - def;
        } else {
            dmg = incomingDMG.getSecond();
        }
        if (dmg > 0) {
            hp -= dmg;
        }
        if (hp <= 0) {
            die();
        }
    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    @Override
    public int getDEF() {
        return def;
    }

    @Override
    public int getATK() {
        return damagePair.getSecond();
    }

    @Override
    public int getDMGType() {
        return damagePair.getFirst();
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public CombatEntity getTarget() {
        return target;
    }

    @Override
    public boolean canAttack() {
        return combatTimer >= combatTimerLimit;
    }

    @Override
    public float getCombatTimerLimit() {
        return combatTimerLimit;
    }

    public void setTarget(CombatEntity combatEntity) {
        this.target = combatEntity;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }



    public int getDef() {
        return def;
    }

    public float getCombatTimer() {
        return combatTimer;
    }

    public void incrementCombatTimer(float delta){
        combatTimer+=delta;
    }

    public Team getTeam(){
        return team;
    }

}
