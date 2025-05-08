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
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            .requestMatchers("/auth/**","/nl/**", "/css/**", "/js/**", "/images/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(sess -> sess
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    )

        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}

}
