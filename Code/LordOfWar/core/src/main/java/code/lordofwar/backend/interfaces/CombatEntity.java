package code.lordofwar.backend.interfaces;

import code.lordofwar.backend.Team;
import com.badlogic.gdx.math.Rectangle;
import kotlin.Pair;

/**
 * Defines units capable of combat. Unit refers to any in-game entity capable of receiving AND/OR dealing damage.
 * To receive damage a unit should have defense and health.
 * To deal damage a unit should have damage and a damagetype.
 * If a entity is only supposed to attack, implement {@link CombatEntity#receiveDmg(Pair)} so that the entity never receives damage.
 * If a entity is only supposed to defend, implement {@link CombatEntity#dealDmg()} so that the entity never deals damage.
 * @author Cem Arslan
 */
public interface CombatEntity {

    /**
     * This method represents the dmg a unit is capable of dealing.
     *
     * @return a {@link Pair<Integer,Double>} where the Integer represents the damage type and double represents the amount of dmg dealt.
     */
    public Pair<Integer, Integer> dealDmg();

    /**
     * This method represents the dmg a unit is dealt.
     * Health and defense should be used to mitigate damage here.
     * If health falls to or below zero {@link CombatEntity#die()} should be triggered.
     * First value is dmg type and second value is atk;
     * Combat timer should be reset to 0;
     *
     * @param incomingDMG the Damage type and amount of incoming dmg.
     */
    public void receiveDmg(Pair<Integer, Integer> incomingDMG);

    /**
     * This method represents the 'death' of a combat entity.
     * Death being defined as the inability to continue combat.
     * A dead combat entity cannot receive or deal further damage.
     * All combat units targeting a dead combat entity should stop doing so.
     */
    public void die();

    /**
     * @return the health of this combat entity
     */
    public int getHP();

    /**
     * @return the maximum health of this combat entity
     */
    public int getMaxHP();

    /**
     * @return the defense of this combat entity.
     */
    public int getDEF();

    /**
     * @return the raw damage amount dealt by this combat entity
     */
    public int getATK();

    /**
     * @return the damage type of this combat entity
     */
    public int getDMGType();

    /**
     * @return if this unit is alive
     */
    public boolean isAlive();

    /**
     * @return a Rectangle centered on the {@link CombatEntity}. This Rectangle represents the range this unit can hit.
     *///aka: combat unit can hit everything in the rectangle
    public Rectangle getCombatReach();

    /**
     * @return the {@link CombatEntity} targeted by this {@link CombatEntity}. When a {@link CombatEntity} is targeted any other should be
     * ignored by this {@link CombatEntity}.
     */
    public CombatEntity getTarget();

    /**
     * @return Time in miliseconds between attack
     */
    public float getCombatTimerLimit();

    /**
     * @return {@code true} if this {@link CombatEntity} can attack; Should be false after attacking
     * (see {@link CombatEntity#dealDmg()} and {@link CombatEntity#getCombatTimerLimit()})
     */
    public boolean canAttack();

    /**
     * @return team of this combat unit
     */
    public Team getTeam();

    /**
     * @return a String symbolizing the type of unit
     *///currently only Castles and Soldiers
    public String getUnitType();
}
