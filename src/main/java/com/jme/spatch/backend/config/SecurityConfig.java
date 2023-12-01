package com.jme.spatch.backend.config;

import com.jme.spatch.backend.config.jwt.JwtAuthenticationFilter;
import com.jme.spatch.backend.model.user.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var resource = new CorsConfiguration();
                    resource.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:3006","http://localhost:3009"));
                    resource.setAllowedMethods(List.of("POST","PUT", "GET", "DELETE"));
                    resource.setAllowedHeaders(List.of("*"));
                    return resource;
                }))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/spatch/**", "/api/v1/auth/spatch/file/view/**","/api/v1/rider/spatch/reg/**","/api/v1/admin/spatch/reg/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/spatch/file/**").hasAnyRole("USER","RIDER","ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/admin/spatch/save").hasAnyRole("SUPER_ADMIN","ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/admin/spatch/req/**").hasAnyRole("SUPER_ADMIN","ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/admin/spatch/sao/**").hasAnyRole("SUPER_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/spatch/**").hasRole("USER"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/rider/**").hasRole("RIDER"))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
