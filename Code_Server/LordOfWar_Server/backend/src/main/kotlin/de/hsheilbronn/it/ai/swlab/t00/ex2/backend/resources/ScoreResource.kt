package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.resources

import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.dto.ScoreDTO
import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.entities.Score
import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.entities.User
import io.quarkus.panache.common.Sort
import io.quarkus.security.identity.SecurityIdentity
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

// Erläuterungen siehe UserResource
@Path("/api/v1/score")
class ScoreResource {

    @Inject
    var identity: SecurityIdentity? = null


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getTop(): List<ScoreDTO> {
        return Score.findAll(Sort.by("value").descending()).range(0, 10).list().map { score ->
            ScoreDTO(score.user.id!!, score.value)
        }.toList()
    }

    // falls Änderungen in der Datenbank durchgeführt werden sollen,
    // dann muss dies in einer Transaktion stattfinden
    // d.h. entweder wird die Änderung vollständig oder
    // gar nicht durchgeführt.
    // Im folgenden heißt das:
    // Wenn es einen User gibt, dann mache etwas damit
    // -> durch die Transaktion ist es ausgeschlossen, dass jemand
    // anderes den Nutzer genau zwischen Abfrage des Nutzers und
    // Speichern des Scores löscht
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun addScore(scoreDTO: ScoreDTO): ScoreDTO {
        // Prüfen, ob Nutzer existiert
        val principal = identity?.principal;
        val user = User.findById(scoreDTO.userId)
        if (user != null) {
            // Nutzer existiert
            val score = Score()
            score.user = user
            score.value = scoreDTO.value
            Score.persist(score)
            return scoreDTO
        } else {
            // Nutzer existiert nicht, 404 zurückgeben
            // Exception -> 404 in "NoSuchElementExceptionMapper"
            throw NoSuchElementException()
        }

    }

}