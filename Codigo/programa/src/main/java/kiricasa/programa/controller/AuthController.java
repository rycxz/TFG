/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kiricasa.programa.service.AuthService;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author recur
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
        private final AuthService authService;

        @PostMapping("/login")
        public ResponseEntity<AuthReponse> login(@RequestBody LoginRequest request) {
                return ResponseEntity.ok(authService.login(request));
        }

        @PostMapping("/register")
        public ResponseEntity<AuthReponse> register(@RequestBody RegisterRequets request) {
                return ResponseEntity.ok(authService.register(request));
        }



}
