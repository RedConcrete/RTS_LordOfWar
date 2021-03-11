package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.entities

import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.security.jpa.Password
import io.quarkus.security.jpa.Roles
import io.quarkus.security.jpa.UserDefinition
import io.quarkus.security.jpa.Username
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

/*
Dies ist eine Entity, d.h. eine Klasse, deren Instanzen in einer Datenbank persistiert werden sollen.

https://quarkus.io/guides/hibernate-orm-panache
https://quarkus.io/guides/hibernate-orm-panache-kotlin

Diese Klasse erbt von PanacheEntity, es soll der Default-Konstruktur der Superklasse verwendet werden
-> : PanacheEntity()

"open" gibt an, dass von der Klasse geerbt werden kann. Wird das nicht angegeben,
dann ist die Klasse standardmäßig "final".

 */
@Entity
@Table(name = "myuser")
@UserDefinition
open class User: PanacheEntity() {

    // In Kotlin werden keine statischen Methoden verwendet
    // als Ersatz gibt es sog. Companion-Objekte
    // siehe: https://kotlinlang.org/docs/tutorials/kotlin-for-py/objects-and-companion-objects.html#companion-objects
    // der Aufruf ist aber identisch zu statischen Methoden, ein Beispiel kann man in der UserResource finden
    companion object: PanacheCompanion<User, Long> {

        fun add(user: User): User {
            // todo check if user exists
            // falls nicht, dann anlegen, falls doch: 400er Fehler

            // stellt sicher, dass das Passwort gehasht gespeichert wird
            // d.h. es ist nicht möglich, das PW aus dem gespeicherten Hash zu rekonstruieren
            // Bcrypt verwendet auch einen Salt - d.h. selbst 2 Nutzer gleiche Passworte verwenden,
            // ist der Hash ein unterschiedlicher -> Rainbow-Tabellen-Angriffe werden sehr schwer
            // vgl. https://de.wikipedia.org/wiki/Rainbow_Table
            user.password = BcryptUtil.bcryptHash(user.password)
            User.persist(user)
            return user
        }

    }

    @Username
    @Column(unique = true, updatable = false) // eindeutig, nicht änderbar
    @Pattern(regexp = "[a-z0-9]+") // keine Sonderzeichen und keine Großbuchstaben, mindestens ein Zeichen
    // lateinit ist notwendig, da durch JPA / Reflection nicht zu Beginn direkt gesetzt (und Feld nicht nullable)
    lateinit var username: String
    @Password
    @NotBlank
    lateinit var password: String

    @Roles
    var role: String = "USER"

}
