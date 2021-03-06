package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages

import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages.data.ShortInstanceInformation

class InstancesListMessage: AbstractMessage() {
    val instances: List<ShortInstanceInformation> = ArrayList()
}