package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration marks the class a configuration for some aspect of Springboot
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Returns a component that is used by SpringBoot
    // Factory method that springboot use later, sometimes internally.
    // Create an instance of a component that it needs
    // If Springboot ever needs an object of the class 'SecurityFilterChain', it will call this method
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http securityClass allows us to define security rules
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/stripe/webhook"))
        .authorizeHttpRequests(authz ->
        authz.requestMatchers("/register", "login", "/css/**", "/js/**", "/stripe/webhook", "/images/**").permitAll() //no login required to these urls
        .anyRequest().authenticated() 
        ).formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/products", true).permitAll()
        ).logout(logout -> logout.permitAll()
        );
        return http.build();
    }
}
