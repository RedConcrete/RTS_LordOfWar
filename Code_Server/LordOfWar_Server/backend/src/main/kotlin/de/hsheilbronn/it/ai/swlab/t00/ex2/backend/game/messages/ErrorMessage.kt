package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages

import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages.AbstractMessage

class ErrorMessage: AbstractMessage() {
    var code: Int = 1
}