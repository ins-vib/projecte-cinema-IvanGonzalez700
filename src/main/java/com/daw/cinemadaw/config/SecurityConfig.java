package com.daw.cinemadaw.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import com.daw.cinemadaw.domain.user.User;
import com.daw.cinemadaw.repository.UserRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        // Desactiva CSRF (necessari per H2)
        .csrf(csrf -> csrf.disable())

        // Permet carregar la consola H2 en un iframe
        .headers(headers -> headers
            .frameOptions(frame -> frame.disable())
        )

        // Configuració d'autoritzacions
        .authorizeHttpRequests(auth -> auth

            // Accés públic
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/login", "/register", "/logout-success", "/css/**", "/", "/cookies/**").permitAll()
            .requestMatchers("/cinemes", "/cinemes/**").permitAll()
            .requestMatchers("/movies", "/movies/user").permitAll()
            .requestMatchers("/news", "/news/**").permitAll()
            .requestMatchers("/screenings/**").permitAll()

            // Rutes protegides per rol - ADMIN
            .requestMatchers("/movies/create", "/movies/edit/**", "/admin/**").hasRole("ADMIN")
            .requestMatchers("/cinemes/create", "/cinemes/edit/**").hasRole("ADMIN")
            .requestMatchers("/seats/**").hasRole("ADMIN")
            .requestMatchers("/rooms/**").hasRole("ADMIN")
            
            // Rutes protegides per rol - CLIENT
            .requestMatchers("/carrito/**").hasAnyRole("CLIENT", "ADMIN")
            .requestMatchers("/client/**", "/entrades/**", "/sessions/**").hasAnyRole("CLIENT", "ADMIN")

            // Qualsevol altra petició necessita autenticació
            .anyRequest().authenticated()
        )

        // Configuració del formulari de login
        .formLogin(form -> form
            .loginPage("/login") // pàgina personalitzada de login
            .successHandler((request, response, authentication) -> {
                List<String> roles = new ArrayList<>();
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    roles.add(authority.getAuthority());
                }
                if (roles.contains("ROLE_ADMIN")) {
                    response.sendRedirect("/admin");
                } else if (roles.contains("ROLE_CLIENT")) {
                    response.sendRedirect("/");
                } else {
                    response.sendRedirect("/");
                }
            })
            .permitAll()
        )
        .logout(logout -> logout
            .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/logout"))
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/logout-success")
            .permitAll()
        );

        return http.build();
    }

    // Bean per encriptar contrasenyes amb BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean per carregar usuaris
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuari no trobat"));
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

}
