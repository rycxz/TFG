/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kiricasa.programa.enums.UsuarioRol;
import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.BarriosRepository;
import kiricasa.programa.repository.PublicacionRepository;
import lombok.AllArgsConstructor;

/**
 *
 * @author 6003194
 */
@Controller
@RequestMapping("/publicacion")
@AllArgsConstructor
public class AccionesPublicacionController {
        private final PublicacionRepository publicacionRepository;
              private final BarriosRepository barriosRepository;
@GetMapping("/editar/{id}")
public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model, HttpSession session) {
    UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/auth/login";

    PublicacionModel publicacion = publicacionRepository.findById(id).orElse(null);
    if (publicacion == null) return "redirect:/home";

    // Solo dueño o admin
    if (!usuario.getRol().equals(UsuarioRol.ADMIN) && !publicacion.getUsuario().getId().equals(usuario.getId())) {
        return "redirect:/home";
    }
    model.addAttribute("usuario", usuario);
    model.addAttribute("publicacion", publicacion);
    model.addAttribute("barrios", barriosRepository.findAll());

    return "publicacion_editar"; // vista Thymeleaf
}

}
