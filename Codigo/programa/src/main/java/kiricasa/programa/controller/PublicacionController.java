/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kiricasa.programa.enums.UsuarioRol;
import kiricasa.programa.models.FavoritosModel;
import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.BarriosRepository;
import kiricasa.programa.repository.FavoritosRepository;
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
          private final FavoritosRepository favoritosRepository;

@GetMapping("/detalle")
public String verDetalle(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

    PublicacionModel publicacion = publicacionRepository.findById(id).orElse(null);
    String token = (String) session.getAttribute("jwt");
    UsuarioModel usuarioLogueado = (UsuarioModel) session.getAttribute("usuario");

    if (usuarioLogueado == null || token == null) {
        return "redirect:/nl/home";
    }

    if (publicacion == null) {
        redirectAttributes.addFlashAttribute("error", "Ha ocurrido un error: el anuncio no existe.");
        return "redirect:/home";
    }

    // ✅ Seguridad extrema
    List<String> fotos = publicacion.getFotos();
    if (fotos == null || fotos.isEmpty()) {
        fotos = List.of("predeterminada.png");
    }

    // ✅ Protejo si publicacion.getUsuario() es null
    UsuarioModel propietario = publicacion.getUsuario();
    boolean puedeGestionar = false;
    if (propietario != null && usuarioLogueado != null) {
        puedeGestionar = usuarioLogueado.getRol() == UsuarioRol.ADMIN
                || propietario.getId().equals(usuarioLogueado.getId());
    }

    // ✅ Protejo si publicacion.getBarrio() es null
    String nombreBarrio = Optional.ofNullable(publicacion.getBarrio())
            .flatMap(b -> barriosRepository.findNombreById(b.getId()))
            .orElse("Barrio desconocido");

    boolean enFavoritos = false;
    FavoritosModel favorito = null;
    if (propietario != null && usuarioLogueado != null
        && !usuarioLogueado.getId().equals(propietario.getId())) {
        favorito = favoritosRepository.findByUsuarioAndPublicacion(usuarioLogueado, publicacion).orElse(null);
        enFavoritos = favorito != null;
    }

    model.addAttribute("enFavoritos", enFavoritos);
    model.addAttribute("favorito", favorito);
    model.addAttribute("fotos", fotos);
    model.addAttribute("publicacion", publicacion);
    model.addAttribute("usuario", usuarioLogueado);
    model.addAttribute("puedeGestionar", puedeGestionar);
    model.addAttribute("nombreBarrio", nombreBarrio);

    return "publicacion";
}


}
