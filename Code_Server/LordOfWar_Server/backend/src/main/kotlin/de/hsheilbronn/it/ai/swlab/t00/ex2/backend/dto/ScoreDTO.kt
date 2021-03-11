package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.dto

/**
 * "Das Transferobjekt oder Datentransferobjekt (Abkürzung DTO) ist ein
 * Entwurfsmuster aus dem Bereich der Softwareentwicklung.
 * Es bündelt mehrere Daten in einem Objekt, sodass sie durch einen einzigen
 * Programmaufruf übertragen werden können. Transferobjekte werden in
 * verteilten Systemen eingesetzt, um mehrere zeitintensive Fernzugriffe
 * durch einen einzigen zu ersetzen."
 *
 * In unserem Fall eher umgekehrt: Wir möchten vermeiden, dass wir unnütze Daten austauschen
 * (z.B. immer einen ganzen User übertragen müssen)
 *
 * Deshalb erstellen wir eine Klasse, die nur die Datenfelder beinhaltet, die uns wirklich interessieren.
 * Eventuell wird für diesen konkreten Fall eher der username und der Wert benötigt.
 * In diesem Fall muss das ScoreDTO angepasst werden.
 *
 */
// todo Kommentar siehe oben: Überprüfen, ob andere Datenfelder sinnvoll sind
// falls ja, anpassen und diesen Kommentar entfernen
// falls nein, diesen Kommentar entfernen
data class ScoreDTO(val userId: Long, val value: Double) {

}