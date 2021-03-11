import io.quarkus.runtime.Quarkus

object Main {
    @JvmStatic
    fun main(vararg args: String) {
        Quarkus.run(*args)
    }
}