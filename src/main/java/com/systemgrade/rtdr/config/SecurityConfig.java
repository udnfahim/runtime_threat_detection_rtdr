package com.systemgrade.rtdr.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.oauth2.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${security.telemetry.agent-token}")
    private String agentToken;

    @Bean
    public NimbusJwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    private OncePerRequestFilter createAgentTokenFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                String path = request.getRequestURI();
                if (path.startsWith("/api/v1/telemetry") || path.startsWith("/api/v1/enforcement/stream")) {
                    String token = request.getHeader("X-Agent-Token");
                    if (token == null || !Objects.equals(token, agentToken)) {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid agent token");
                        return;
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Explicitly allow the internal error dispatch path along with Actuator
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        // 2. Telemetry Ingestion via Agents
                        .requestMatchers(HttpMethod.POST, "/api/v1/telemetry/**").permitAll()

                        // 3. Secured Application Vectors
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/ws/**", "/topic/**").authenticated()

                        // 4. Secure Baseline Catch-all
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        http.addFilterBefore(createAgentTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}