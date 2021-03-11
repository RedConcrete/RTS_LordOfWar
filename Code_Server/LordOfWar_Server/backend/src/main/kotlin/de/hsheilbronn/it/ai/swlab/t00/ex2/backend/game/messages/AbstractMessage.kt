package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages

import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.entities.User

abstract class AbstractMessage {
    var sender: User? = null
}