package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game;

import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.gameinstance.SingleGameInstance
import io.quarkus.security.Authenticated
import java.util.concurrent.CopyOnWriteArrayList
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*
import javax.websocket.server.ServerEndpoint

// gibt es einmal in der Anwendung
// sonst gibt es z.B. RequestScoped
@ServerEndpoint("/api/v1/ws")
@ApplicationScoped
/**
 * Verwaltet die verschiedenen Spiele und sorgt dafür,
 * dass Nachrichten von Endgeräten in die richtigen Spiele kommen.
 */
class GameManagerService {
    var sessions = CopyOnWriteArrayList<Session>()
    var gameInstances = CopyOnWriteArrayList<SingleGameInstance>()

    @OnOpen
    fun onOpen(session: Session) {
        if (session.userPrincipal == null) {
            session.close(CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "AUTH_FAILED"))
        }
        sessions.add(session)
    }

    @OnClose
    fun onClose(session: Session) {
        sessions.remove(session)
    }

    @OnError
    fun onError(session: Session, throwable: Throwable?) {
        sessions.remove(session)
    }

    @OnMessage
    fun onMessage(msgString: String, session: Session) {
        //broadcast(message)
        // todo session.userPrincipal
        val message = MessageConverter.convertMessage(msgString)
        if (isInGameInstance(session)) {
            // send message to room
        } else {
            // allow other actions
        }
    }

    private fun isInGameInstance(session: Session): Boolean {
        return session.userProperties.containsKey("instanceId");
    }

    fun sendMessageToSession(recipient: String, message: String) {
        // korrekten Empfänger finden
        // Nachricht umwandeln
        // -> MessageConverter entsprechend anpassen

    }

    /*private fun broadcast(message: String) {
        sessions.forEach(Consumer { s: Session ->
            s.asyncRemote.sendObject(message) { result: SendResult ->
                if (result.exception != null) {
                    println("Unable to send message: " + result.exception)
                }
            }
        })
    }*/

}
