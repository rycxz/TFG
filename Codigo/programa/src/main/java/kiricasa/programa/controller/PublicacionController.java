/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
@RequestMapping()
@AllArgsConstructor
public class PublicacionController {
    private final PublicacionRepository publicacionRepository;
        private final BarriosRepository barriosRepository;

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
    //sacar ula lista con todas las fotos de la publicacion
    List<String> fotos = publicacion.getFotos();
    model.addAttribute("fotos", fotos);
    model.addAttribute("publicacion", publicacion);


  boolean puedeGestionar = false;
        UsuarioModel usuario1 = (UsuarioModel) session.getAttribute("usuario");
            Long idBarrio = publicacion.getBarrio().getId();

            String nombreBarrio = barriosRepository.findNombreById(idBarrio)
                      .orElse("Barrio desconocido");
        if (usuario1 != null) {
            puedeGestionar = usuario1.getRol() == UsuarioRol.ADMIN
                || publicacion.getUsuario().getId().equals(usuario1.getId());
        }
        boolean puedeAñadirFavoritos = false;
        if (usuario1 != null && !publicacion.getUsuario().getId().equals(usuario1.getId())) {
            puedeAñadirFavoritos = true;
        }

model.addAttribute("puedeAñadirFavoritos", puedeAñadirFavoritos);


model.addAttribute("puedeGestionar", puedeGestionar);
model.addAttribute("nombreBarrio", nombreBarrio);

    return "publicacion"; // tu plantilla detalle.html
}


}
