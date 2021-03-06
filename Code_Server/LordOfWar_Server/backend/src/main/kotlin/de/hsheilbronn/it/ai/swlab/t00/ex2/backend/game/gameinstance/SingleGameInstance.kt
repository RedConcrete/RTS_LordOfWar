package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.gameinstance


import java.util.concurrent.CopyOnWriteArrayList

/**
 * Repräsentiert eine Instanz eines Spieles
 *
 * Beispiel: SingleGameInstance = Das erste Uno-Spiel von Max und Moritz am 30.01.2011 um 11:11 Uhr
 *
 * Hier werden alle notwendigen Details des Spiels gespeichert, z.B.
 * - die Spieler
 * - der Status des Spiels (läuft, beendet, wartet auf Mitspieler)
 * - die Karten aller Spieler
 * - der Spieler, der aktuell dran ist
 *
 * Hier wird auf alle Nachrichten der Clients reagiert (siehe Beispiele weiter unten)
 *
 */
class SingleGameInstance {
    var gameStatus: GameStatus = GameStatus.WAITING_FOR_PLAYERS
    // hier wird eine CopyOnWriteArrayList verwendet
    // dies ist wichtig, da von mehreren Nutzern in unterschiedlichen Threads Zugriffe auf
    // die Liste erfolgen könn(t)en
    var players: List<Player> = CopyOnWriteArrayList()


}