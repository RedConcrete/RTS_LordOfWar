package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.gameinstance

import javax.websocket.Session

/**
 * Beschreibt einen Spieler.
 * Ein Spieler ist ein Nutzer mit spielspezifischen Eigenschaften
 * -> Beispiel: Ein Nutzer bekommt für eine Spieleinstanz eine Farbe zugewiesen
 *
 * Auch aus Performance-Gründen sollte nicht jedes Mal im Spiel das User-Objekt geladen werden,
 * hier sollten nur Elemente gespeichert werden, die nicht zwischen mehreren Spielen "überleben" müssen
 */
data class Player(val username: String, val session: Session, var color: Color) {

}