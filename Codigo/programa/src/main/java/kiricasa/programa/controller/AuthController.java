/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author recur
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

        @PostMapping("/login")
        public String login() {
                return " hola estoy desde una ruta publica login";
        }

        @PostMapping("/register")
        public String register() {
                return " hola estoy desde una ruta publica register";
        }



}
