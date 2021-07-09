package code.lordofwar.backend;

/**
 *
 * @author Franz Klose,Cem Arslan
 */
public enum MessageIdentifier {

    CONNECTION,
    LOGIN,
    LOGIN_VALID,
    REGISTER,
    REGISTER_VALID,
    GET_GAME_POINTS,
    GAME_START,
    CREATE_LOBBY,
    JOIN_LOBBY,
    LEAVE_LOBBY,
    GET_LOBBYS,
    LOBBY_PLAYERS,
    UPDATE_SOLDIER_POS,
    ATTACK_UNIT_UPDATE,//Syntax: [MI,starting STARTING POSITION (of enemy),UNITTYPE(Soldier or castle), DAMAGE TYPE,ATK]
    UPDATE_CASTLE_POS,
    GAME_OVER
}
