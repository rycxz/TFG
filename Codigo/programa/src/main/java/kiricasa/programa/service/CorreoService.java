/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 *
 */
package kiricasa.programa.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.UsuarioRepository;




/**
 *
 * @author 6003194
 */
@Service
public class CorreoService {
  @Autowired
    private JavaMailSender mailSender;
    private CorreoService correoService;
    private UsuarioRepository usuarioRepository;

     public void enviarCodigo(String destinatario, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Código de verificación - Kiricasa");
        mensaje.setText("Tu código de verificación es: " + codigo);
        mailSender.send(mensaje);
    }
    public void enviarCodigoRecuperacion(String email) {
    UsuarioModel usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email no registrado"));

    String codigo = String.valueOf((int)(Math.random() * 900000) + 100000);
    usuario.setCodigoRecuperacion(codigo);
    usuario.setExpiracionCodigo(LocalDateTime.now().plusMinutes(15));
    usuarioRepository.save(usuario);

    correoService.enviarCodigo(usuario.getEmail(), codigo);
}
}
