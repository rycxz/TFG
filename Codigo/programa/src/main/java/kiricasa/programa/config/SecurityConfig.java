package kiricasa.programa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import kiricasa.programa.jwt.JWTAuthentificationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthentificationFilter jwtAuthentificationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.exceptionHandling(ex -> ex
    .accessDeniedHandler((request, response, accessDeniedException) -> {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
    })
    .authenticationEntryPoint((request, response, authException) -> {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autenticado");
    })
);

    return http

        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            .requestMatchers("/auth/**","/nl/**", "/css/**", "/js/**", "/images/**","/uploads/**" ).permitAll()
            .anyRequest().authenticated()
        )

        .sessionManagement(sess -> sess
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    )


        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/nl/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )

        .build();

}

}
