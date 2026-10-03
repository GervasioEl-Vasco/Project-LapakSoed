package com.example.lapaksoed.security

import com.example.lapaksoed.auth.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ", ignoreCase = true) }
            ?.substringAfter(' ')

        if (token != null && SecurityContextHolder.getContext().authentication == null) {
            val userId = jwtService.readUserId(token)
            val user = userId?.let(userRepository::findById)?.orElse(null)
            if (user != null) {
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(user, null, emptyList())
            }
        }
        chain.doFilter(request, response)
    }
}