package com.pcs.restaurantapi.config;

import com.pcs.restaurantapi.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",  // Allow Swagger UI
                                "/v3/api-docs/**", // Allow OpenAPI docs
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/menu/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/menu").hasAuthority("ROLE_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/menu/**").hasAuthority("ROLE_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/menu/**").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/api/v1/delivery/**").hasAuthority("ROLE_DELIVERY_CREW")
                        .requestMatchers("/api/v1/customer/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers("/api/v1/profile/**").hasAnyAuthority(
                                "ROLE_MANAGER", "ROLE_DELIVERY_CREW", "ROLE_CUSTOMER"
                        )
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
