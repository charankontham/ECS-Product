package com.ecs.ecs_product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(HttpMethod.PUT, "/api/productCategory").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/productCategory/**").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/productCategory/**").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/productBrand/**").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/productBrand/**").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/productBrand/**").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/product").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/product").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("INVENTORY_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
