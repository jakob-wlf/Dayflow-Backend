package de.jakob.dayflow.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {

    @GetMapping("/protected")
    fun protectedEndpoint(@AuthenticationPrincipal user: User): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, ${user.username}! You have accessed a protected endpoint.")
    }
}
