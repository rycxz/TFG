/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kiricasa.programa.enums.UsuarioRol;
import kiricasa.programa.models.BarriosModel;
import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.AnunciosVistosRepository;
import kiricasa.programa.repository.BarriosRepository;
import kiricasa.programa.repository.FavoritosRepository;
import kiricasa.programa.repository.PublicacionRepository;
import kiricasa.programa.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

/**
 *
 * @author recur
 */
@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    @SuppressWarnings("FieldMayBeFinal")
    private PublicacionRepository publicacionRepository;
      @SuppressWarnings("FieldMayBeFinal")
    private UsuarioRepository usuarioRepository;
      @SuppressWarnings("FieldMayBeFinal")
    private BarriosRepository barriosRepository;
      @SuppressWarnings("FieldMayBeFinal")
    private FavoritosRepository favoritosRepository;
      @SuppressWarnings("FieldMayBeFinal")
    private AnunciosVistosRepository anunciosVistosRepository;
    @GetMapping("/ver")
    public String verAdmin(Model model, HttpSession session) {
        UsuarioModel current = (UsuarioModel) session.getAttribute("usuario");
        if (current == null || current.getRol() != UsuarioRol.ADMIN) {
            return "redirect:/home";
        }
         UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("barrios", barriosRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("barrios", barriosRepository.findAll());
        return "admin";
    }
@PostMapping("/usuarios/delete/{id}")
@Transactional
public String deleteUser(@PathVariable Long id,
                         RedirectAttributes ra,
                         HttpSession session) {

    UsuarioModel current = (UsuarioModel) session.getAttribute("usuario");
    if (current == null || current.getRol() != UsuarioRol.ADMIN) {
        ra.addFlashAttribute("error", "No tienes permisos para esta acción.");
        return "redirect:/home";
    }

    // 1) Borrar favoritos del usuario
    favoritosRepository.deleteByUsuario_Id(id);

    // 2) Borrar registros de anuncios vistos del usuario
    anunciosVistosRepository.deleteByUsuario_Id(id);

    // 3) Borrar publicaciones del usuario
    List<PublicacionModel> publicaciones = publicacionRepository.findByUsuario_Id(id);
    for (PublicacionModel pub : publicaciones) {
        // Borrar anuncios vistos relacionados con la publicación
        anunciosVistosRepository.deleteByPublicacion_Id(pub.getId());

        // Borrar favoritos que referencian a esta publicación
        favoritosRepository.deleteByPublicacion_Id(pub.getId());

        // Borrar la publicación
        publicacionRepository.delete(pub);
    }

    // 4) Borrar finalmente al usuario
    usuarioRepository.deleteById(id);

    ra.addFlashAttribute("success", "Usuario eliminado correctamente.");
    return "redirect:/admin/ver";
}
@PostMapping("/usuarios/hacer-admin/{id}")
@Transactional
public String hacerAdmin(@PathVariable Long id,
                         RedirectAttributes ra,
                         HttpSession session) {
    UsuarioModel current = (UsuarioModel) session.getAttribute("usuario");
    if (current == null || current.getRol() != UsuarioRol.ADMIN) {
        ra.addFlashAttribute("error", "No tienes permisos para esta acción.");
        return "redirect:/home";
    }

    usuarioRepository.findById(id).ifPresent(u -> {
        u.setRol(UsuarioRol.ADMIN);
        usuarioRepository.save(u);
    });

    ra.addFlashAttribute("success", "El usuario ahora es administrador.");
    return "redirect:/admin/ver";
}

@PostMapping("/usuarios/quitar-admin/{id}")
@Transactional
public String quitarAdmin(@PathVariable Long id,
                          RedirectAttributes ra,
                          HttpSession session) {
    UsuarioModel current = (UsuarioModel) session.getAttribute("usuario");
    if (current == null || current.getRol() != UsuarioRol.ADMIN) {
        ra.addFlashAttribute("error", "No tienes permisos para esta acción.");
        return "redirect:/home";
    }

    usuarioRepository.findById(id).ifPresent(u -> {
        u.setRol(UsuarioRol.USER);
        usuarioRepository.save(u);
    });

    ra.addFlashAttribute("success", "El usuario ya no es administrador.");
    return "redirect:/admin/ver";
}
@PostMapping("/barrios/add")
public String agregarBarrio(@RequestParam String nombre,
                            @RequestParam String descripcion,
                            @RequestParam String ubicacion,
                            RedirectAttributes ra) {

    if (nombre.trim().isEmpty() || descripcion.trim().isEmpty() || ubicacion.trim().isEmpty()) {
        ra.addFlashAttribute("error", "Todos los campos son obligatorios.");
        return "redirect:/admin/ver";
    }

    BarriosModel barrio = new BarriosModel();
    barrio.setNombre(nombre.trim());
    barrio.setDescripcion(descripcion.trim());
    barrio.setUbicacion(ubicacion.trim());

    // Establecer valores por defecto para los campos opcionales
    barrio.setImagen("default.jpg");
    barrio.setCodigoPostal(null);
    barrio.setRangoPrecios(null);
    barrio.setCantidadAnuncios(0);

    barriosRepository.save(barrio);
    ra.addFlashAttribute("success", "Barrio creado correctamente.");
    return "redirect:/admin/ver";
}
@PostMapping("/barrios/delete/{id}")
@Transactional
public String eliminarBarrio(@PathVariable Long id, RedirectAttributes ra, HttpSession session) {
    UsuarioModel current = (UsuarioModel) session.getAttribute("usuario");
    if (current == null || current.getRol() != UsuarioRol.ADMIN) {
        ra.addFlashAttribute("error", "No tienes permisos para esta acción.");
        return "redirect:/home";
    }

    // 1. Obtener las publicaciones del barrio
    List<PublicacionModel> publicaciones = publicacionRepository.findByBarrio_Id(id);
    for (PublicacionModel pub : publicaciones) {
        favoritosRepository.deleteByPublicacion_Id(pub.getId());
        anunciosVistosRepository.deleteByPublicacion_Id(pub.getId());
        publicacionRepository.delete(pub);
    }

    // 2. Borrar el barrio
    barriosRepository.deleteById(id);
    ra.addFlashAttribute("success", "Barrio y sus publicaciones eliminados correctamente.");
    return "redirect:/admin/ver";
}






}
