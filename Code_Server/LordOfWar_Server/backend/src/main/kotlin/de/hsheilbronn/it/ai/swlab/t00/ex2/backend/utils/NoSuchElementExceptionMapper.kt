package de.hsheilbronn.it.ai.swlab.t00.ex2.backend.utils

import java.util.NoSuchElementException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class NoSuchElementExceptionMapper: ExceptionMapper<NoSuchElementException> {
    override fun toResponse(ex: NoSuchElementException?): Response {
        return Response.status(404).build()
    }
}