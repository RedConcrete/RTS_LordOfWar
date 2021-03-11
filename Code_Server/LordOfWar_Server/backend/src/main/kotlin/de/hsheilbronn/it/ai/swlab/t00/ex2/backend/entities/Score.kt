package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Entity
import javax.persistence.ManyToOne

// Entity-Erklärung siehe User
@Entity
open class Score: PanacheEntity() {

    companion object: PanacheCompanion<Score, Long> {
        fun getScoreForUser(userid: Long) = list("user.id", userid)
    }

    // Ein Score hängt an einem User
    // Ein User kann mehrere Scores haben, aber ein Score gehört zu genau einem User
    @ManyToOne
    lateinit var user: User
    var value: Double = 0.0
}