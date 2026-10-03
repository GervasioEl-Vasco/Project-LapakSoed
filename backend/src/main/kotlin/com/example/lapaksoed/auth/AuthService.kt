package com.example.lapaksoed.auth

import com.example.lapaksoed.security.JwtService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) {
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        val email = request.email.trim().lowercase()
        val nim = request.nim.trim()
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Email sudah terdaftar")
        }
        if (userRepository.existsByNim(nim)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "NIM sudah terdaftar")
        }
        val user = userRepository.save(
            User(email = email, passwordHash = passwordEncoder.encode(request.password), fullName = request.fullName.trim(), nim = nim),
        )
        return user.toAuthResponse()
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmailIgnoreCase(request.email.trim())
        if (user == null || !passwordEncoder.matches(request.password, user.passwordHash)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email atau password salah")
        }
        return user.toAuthResponse()
    }

    private fun User.toAuthResponse() = AuthResponse(jwtService.createToken(requireNotNull(id)), user = toResponse())
}