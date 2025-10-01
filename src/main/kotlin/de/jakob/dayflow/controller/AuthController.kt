package de.jakob.dayflow.controller

import de.jakob.dayflow.dto.AuthResponse
import de.jakob.dayflow.dto.LoginRequest
import de.jakob.dayflow.dto.RegisterRequest
import de.jakob.dayflow.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        authService.register(request)
        return ResponseEntity.ok(AuthResponse("User registered successfully"))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val token = authService.login(request)
        return ResponseEntity.ok(AuthResponse("Login successful. Token: $token"))
    }
}
