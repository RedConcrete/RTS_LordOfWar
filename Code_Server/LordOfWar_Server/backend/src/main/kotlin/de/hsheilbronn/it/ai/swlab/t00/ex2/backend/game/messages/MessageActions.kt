package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.game.messages

/**
 * Stellt Aktionen in Nachrichten dar. In diesem Fall werden 3 Aktionen beschrieben.
 * Es gibt aber noch mehr (siehe auch messages-package) - bei euch werden noch einige mehr hinzukommen.
 *
 * Hier findet ein Mapping zwischen der Aktion als String / Enum-Wert und der entsprechenden Java-Klasse statt,
 * in der die Daten repräsentiert werden.
 *
 * Die Inhalte dieser Enum werden im MessageConverter verwendet.
 */
enum class MessageActions(val clazz: Class<out AbstractMessage>) {
    LIST_INSTANCE(InstancesListMessage::class.java),
    JOIN_INSTANCE(JoinInstanceMessage::class.java),
    CREATE_INSTANCE(CreateInstanceMessage::class.java);

}