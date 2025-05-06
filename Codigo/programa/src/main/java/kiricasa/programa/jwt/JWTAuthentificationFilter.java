/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kiricasa.programa.service.JwtService;
import lombok.RequiredArgsConstructor;


/**
 *
 * @author recur
 */
@Component
@RequiredArgsConstructor
public class JWTAuthentificationFilter  extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal( @NonNull HttpServletRequest request,  @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws  ServletException,  IOException {
        String path = request.getServletPath();
        System.out.println("Filtro ejecutado para ruta: " + path);

                // Permitir que las rutas públicas pasen directamente

                if (path.startsWith("/auth")
                    || path.endsWith(".jsp")
                    || path.startsWith("/login")
                    || path.startsWith("/register")
                    || path.startsWith("/css")
                    || path.startsWith("/js")
                    || path.startsWith("/images")) {
                    filterChain.doFilter(request, response);
                    return;
                }


        /*por lo que yo he entendio este metodo es el encargado de verficar el token que hemos creado por lo que lo primero lo recibe  y deùes se valida y devuelve la respuesta */
       final  String token = getTokenFromRequest(request);
        final String nombre;
        if (token == null ) {
            // Si el token es válido, puedes establecer la autenticación en el contexto de seguridad
            filterChain.doFilter(request, response);
            return;
        }
        nombre = jwtService.getUsernameFromToken(token);
        if(nombre != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(nombre);
            if(jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
