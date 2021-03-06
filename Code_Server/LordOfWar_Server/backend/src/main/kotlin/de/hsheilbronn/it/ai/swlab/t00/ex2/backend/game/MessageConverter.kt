package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages.AbstractMessage
import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages.MessageActions

/**
 * Bei diesem Aufbau findet eine doppelte Serialisierung und Deserialisierung statt.
 *
 * In Schritt 1 wird die innere Nachricht serialisiert.
 * Schritt 2: Diese als String serialisierte Nachricht wird zusammen mit der Bezeichnung für die Aktion erneut serialisiert.
 *
 * Beim Deserialisieren sieht das dann so aus:
 * Die WSMessage als String wird als WSMessage-Objekt deserialisiert.
 * Dadurch kann die Aktion ausgelesen werden - damit kann der Typ der Nachricht und damit die Zielklasse bestimmt werden.
 * Die innere Nachricht wird dann als Instanz dieser Zielklasse deserialisiert.
 *
 * Beispiel: action: SHOOT, Nutzdaten sind x,y des Spielers, Schusswinkel von pos. X-Achse
 * Im ersten Schritt werden die Nutzdaten serialisiert -> { "x": 1, "y": 2, "degree": 175.3 }
 * Im zweiten Schritt wird eine WSMessage serialisiert
 * -> message = { "action": "SHOOT", "data": "{ \"x\": 1, \"y\": 2, \"degree\": 175.3 }" }
 * Zum Deserialisieren wird die Nachricht message als WSMessage deserialisiert (geht immer).
 * Dort kann die Action ausgelesen werden, die innere Nachricht kann als "ShootAction" deserialisiert werden.
 */
object MessageConverter {
    val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun convertMessage(msgStr: String): AbstractMessage {
        val wsMessage: WSMessage = objectMapper.readValue(msgStr, WSMessage::class.java)
        var message: AbstractMessage? = null
        // todo catch exception if enum value does not exist
        val action = MessageActions.valueOf(wsMessage.action)
        return objectMapper.readValue(wsMessage.data, action.clazz)
    }

    fun convertMessage(msg: AbstractMessage): String {
        // todo implement :-)
        return "{}"
    }
}