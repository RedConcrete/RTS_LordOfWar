package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game

/**
 * Beispielhafte Implementierung einer WebSocket-Nachricht
 *
 * Konvertierung siehe MessageConverter
 *
 */
class WSMessage {
    lateinit var action: String
    lateinit var data: String
}