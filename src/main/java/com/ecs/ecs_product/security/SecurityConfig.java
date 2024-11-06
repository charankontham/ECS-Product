package com.ecs.ecs_product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(HttpMethod.PUT, "/api/productCategory").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/productCategory/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/productCategory/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/productBrand/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/productBrand/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/productBrand/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/product").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/product").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
