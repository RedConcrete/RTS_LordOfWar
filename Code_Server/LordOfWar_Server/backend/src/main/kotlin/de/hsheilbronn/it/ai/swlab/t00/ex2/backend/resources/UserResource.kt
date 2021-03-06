package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.resources;

import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.entities.User
import io.quarkus.panache.common.Sort
import java.util.stream.Collectors
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Hier wird der User-Endpunkt definiert.
 * Allgemeines zu REST in Quarkus:
 * https://quarkus.io/guides/rest-json
 *
 * Eine Rest-Resource hat eine Basisurl (hier: /api/v1/user)
 * und einen oder mehrere Endpunkte.
 *
 * Beispiel: Get-Endpunkt auf /api/v1/user -> gibt alle Benutzer zurück, unauthentiziert
 * Get-Endpunkt auf /api/v1/user/343 -> gibt Benutzer mit ID 343 zurück oder wenn nicht gefunden einen 404-Fehler
 * Der 404-Fehler wird zurückgegeben, wenn eine NoSuchElementException fliegt. Dies wird durch den NoSuchElementExceptionMapper (utils-package) umgesetzt.
 *
 */
@Path("/api/v1/user")
class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): List<User> {
        return User.findAll(Sort.by("username")).list().stream().map { user ->
            user.password = "NO_PASSWORD_HERE"
            user
        }.collect(Collectors.toList())
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getUserById(@PathParam("id") id: Long): User? {
        val user = User.findById(id)
        if (user != null) {
            user.password = "NO_PASSWORD_HERE"
            return user
        } else {
            throw NoSuchElementException()
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    fun add(@Valid user: User): User {
        val persistedUser = User.add(user)
        persistedUser.password = "NO_PASSWORD_HERE"
        return persistedUser
    }
}
