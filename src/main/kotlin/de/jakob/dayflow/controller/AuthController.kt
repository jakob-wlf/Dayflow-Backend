package de.jakob.dayflow.controller

import de.jakob.dayflow.dto.AuthResponse
import de.jakob.dayflow.dto.LoginRequest
import de.jakob.dayflow.dto.RegisterRequest
import de.jakob.dayflow.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
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

    @GetMapping("/auth/oauth2/success")
    fun oauth2Success(@AuthenticationPrincipal principal: OAuth2User): Map<String, String> {
        val email = principal.getAttribute<String>("email") ?: throw IllegalArgumentException("Email not found in OAuth2 user attributes")

        // Create local user if not exists
        authService.oauthLogin(email)

        return mapOf("message" to "Logged in with Google!", "email" to email)
    }


}
