package de.jakob.dayflow.config

import de.jakob.dayflow.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }        // disable CSRF
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                auth.anyRequest().authenticated()
            }
            .formLogin { it.disable() }
            .oauth2Login { oauth2 ->
                oauth2.defaultSuccessUrl("/api/auth/oauth2/success")
                oauth2.failureUrl("/api/auth/oauth2/failure")
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // REST: no session
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
