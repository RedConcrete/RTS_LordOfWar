package de.hsheilbronn.it.ai.swlab.t00.ex2.backend

import de.hsheilbronn.it.ai.swlab.t00.ex2.backend.entities.User
import io.quarkus.runtime.StartupEvent
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logmanager.LogManager
import javax.enterprise.event.Observes
import javax.inject.Singleton
import javax.transaction.Transactional


@Singleton
class Startup {
    // findet lokal beim Entwickeln statt (siehe application.properties)
    @ConfigProperty(name= "loadUsers")
    var loadUsers: Boolean = false

    @Transactional
    fun loadUsers(@Observes evt: StartupEvent?) {
        if (loadUsers) {
            // Nutzer zurücksetzen, Testnutzer erstellen
            val logger = LogManager.getLogManager().getLogger(Startup::class.java.name)
            logger.info("reset all users")
            User.deleteAll()
            for (i in 1..9) {
                val user = User()
                user.username = ""+i
                user.password = "1234"
                User.add(user)
            }
        }
    }
}