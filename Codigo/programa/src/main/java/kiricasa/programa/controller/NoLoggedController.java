/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.repository.PublicacionRepository;

/**
 *
 * @author 6003194
 */
@Controller
@RequestMapping("/nl")
public class NoLoggedController  {
        @Autowired
    /**
     * * Método que muestra la página de inicio
     * Lo que se quiere hacer con esto es que si el usuario no esta loggeado y por x motivo llega al controlador de HomeController
     * se le redirija a la página de inicio de no loggeado
     * @return
     */
       private PublicacionRepository publicacionRepository;
  @GetMapping("/home")
    public String mostrarHome(Model model, HttpSession session) {
/* if (!sessionUtils.isUserLogged(session))
        {
        return "redirect:/auth/login";
        } */
       List<PublicacionModel> publicaciones = publicacionRepository.findAll();

            //para la iamgen siempre se eleige la primera
  for (PublicacionModel publicacion : publicaciones) {
    if (publicacion.getFotos() != null && !publicacion.getFotos().isEmpty()) {
        System.out.println("Imagen: " + publicacion.getFotos().get(0));
        publicacion.setImagen(publicacion.getFotos().get(0));
    }
}
                 model.addAttribute("publicaciones", publicaciones);
               return "home";
    }

@GetMapping("/detalle")
public String verDetalle(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

    PublicacionModel publicacion = publicacionRepository.findById(id).orElse(null);
    if (publicacion == null) {
        redirectAttributes.addFlashAttribute("error", "Ha ocurrido un error: el anuncio no existe.");
        return "redirect:/nl/home";
    }
    List<String> fotos = publicacion.getFotos();
    System.out.println("Fotos: " + fotos);
    for (int i = 0; i < fotos.size(); i++) {
        String foto = fotos.get(i);
        System.out.println("Foto " + i + ": " + foto);

    }
    boolean puedeGestionar = false;
    boolean enFavoritos = false;

    model.addAttribute("usuario", null);
    model.addAttribute("fotos", fotos);
    model.addAttribute("publicacion", publicacion);
    model.addAttribute("puedeGestionar", puedeGestionar);

    return "publicacion";
}

}
