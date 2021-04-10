package de.constants;

public enum MessageIdentifier {

    CONNECTION,
    LOGIN,
    LOGIN_VALID,
    REGISTER,
    REGISTER_VALID,
    GET_GAME_POINTS,//request to get current number of points (ingame) included data: UserID
    CREATE_LOBBY,
    GET_LOBBYS,
    JOIN_LOBBY
    ;

}
