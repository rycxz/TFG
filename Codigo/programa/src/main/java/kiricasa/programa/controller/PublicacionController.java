/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.repository.PublicacionRepository;
import lombok.AllArgsConstructor;

/**
 *
 * @author 6003194
 */
@Controller
@RequestMapping()
@AllArgsConstructor
public class PublicacionController {
    private final PublicacionRepository publicacionRepository;

@GetMapping("/detalle")
public String verDetalle(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

    // Aquí buscas el anuncio por ID y lo pasas al modelo
    PublicacionModel publicacion = publicacionRepository.findById(id).orElse(null);
    String token = (String) session.getAttribute("jwt");
    Object usuario = session.getAttribute("usuario");
    if (usuario == null || token == null) {
        return "redirect:/nl/home";
    }

    model.addAttribute("usuario", usuario);

    if (publicacion == null) {
        redirectAttributes.addFlashAttribute("error", "Ha ocurrido un error: el anuncio no existe.");

        return "redirect:/home";
    }

    model.addAttribute("publicacion", publicacion);
    return "publicacion"; // tu plantilla detalle.html
}


}
