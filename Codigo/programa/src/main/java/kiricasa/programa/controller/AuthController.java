/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpSession;
import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.UsuarioRepository;
import kiricasa.programa.requests.LoginRequest;
import kiricasa.programa.requests.RegisterRequest;
import kiricasa.programa.service.AuthService;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author recur
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
        private final AuthService authService;
        private final UsuarioRepository usuarioRepository;

                        @PostMapping("/login")
                        public String login(@ModelAttribute LoginRequest request, HttpSession session) {
                        String token = authService.login(request).getToken();
                        session.setAttribute("jwt", token);

                                    UsuarioModel usuario = usuarioRepository.findByNombreIgnoreCase(request.getNombre())
                        .orElse(null);
                        session.setAttribute("usuario", usuario);
                        return "redirect:/home"; // después del login o registro

                        }


                        @PostMapping("/register")
                        public String register(@ModelAttribute RegisterRequest request, HttpSession session) {
                            String token = authService.register(request).getToken();
                            session.setAttribute("jwt", token);
                            UsuarioModel usuario = usuarioRepository.findByNombreIgnoreCase(request.getNombre())
                                .orElse(null);
                            session.setAttribute("usuario", usuario);
                           // System.out.println("request.isRecibirNotificaciones()" + request.isRecibirNotificaciones());
                           return "redirect:/home"; // después del login o registro

                        }




}
