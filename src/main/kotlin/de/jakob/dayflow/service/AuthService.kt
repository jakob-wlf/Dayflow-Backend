package de.jakob.dayflow.service

import de.jakob.dayflow.dto.LoginRequest
import de.jakob.dayflow.dto.RegisterRequest
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.repository.UserRepository
import de.jakob.dayflow.security.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun register(request: RegisterRequest): User {
        if (userRepository.findByEmail(request.email).isPresent) {
            throw IllegalArgumentException("Email already in use")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )

        return userRepository.save(user)
    }

    fun oauthLogin(email: String) {
        if (!userRepository.findByEmail(email).isPresent) {
            val user = User(
                email = email,
                password = passwordEncoder.encode(UUID.randomUUID().toString()), // random password
                enabled = true
            )
            userRepository.save(user)
        }
    }


    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Invalid email or password") }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        return jwtUtil.generateToken(user.email)
    }
}