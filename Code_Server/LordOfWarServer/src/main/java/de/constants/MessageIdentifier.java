package de.constants;

public enum MessageIdentifier {

    CONNECTION,
    LOGIN,
    LOGIN_VALID,
    REGISTER,
    REGISTER_VALID,
    GET_GAME_POINTS,
    CREATE_LOBBY,
    GET_LOBBYS,
    JOIN_LOBBY,
    LEAVE_LOBBY,
    LOBBY_PLAYERS,
    GAME_START,
    GET_LOBBY_INFO,
    UPDATE_SOLDIER_POS,
    UPDATE_UNIT_HEALTH,//Syntax: [MI,starting STARTING POSITION (of enemy),UNITTYPE(Soldier or castle),enemy hashcode, DAMAGE TYPE,ATK]
    UPDATE_CASTLE_POS
}
