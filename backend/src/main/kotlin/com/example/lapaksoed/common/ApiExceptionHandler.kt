package com.example.lapaksoed.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

data class ApiError(val status: Int, val message: String, val fieldErrors: Map<String, String> = emptyMap())

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validation(exception: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val errors = exception.bindingResult.fieldErrors.associate { error ->
            error.field to (error.defaultMessage ?: "Nilai tidak valid")
        }
        val body = ApiError(HttpStatus.BAD_REQUEST.value(), "Validasi gagal", errors)
        return ResponseEntity.badRequest().body(body)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun status(exception: ResponseStatusException): ResponseEntity<ApiError> {
        val status = HttpStatus.valueOf(exception.statusCode.value())
        return ResponseEntity.status(status).body(ApiError(status.value(), exception.reason ?: status.reasonPhrase))
    }
}