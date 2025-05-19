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

import jakarta.servlet.http.HttpSession;
import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.repository.PublicacionRepository;




/**
 *
 * @author 6003194
 */
@Controller
@RequestMapping()


public class HomeController {
    @Autowired
    private PublicacionRepository publicacionRepository;
  @GetMapping("/home")
    public String mostrarHome(Model model, HttpSession session) {
        /* if (!sessionUtils.isUserLogged(session))
        {
        return "redirect:/auth/login";
        } */

        String token = (String) session.getAttribute("jwt");
        Object usuario = session.getAttribute("usuario");


       List<PublicacionModel> publicaciones = publicacionRepository.findAll();

      //para la iamgen siempre se eleige la primera
  for (PublicacionModel publicacion : publicaciones) {
    if (publicacion.getFotos() != null && !publicacion.getFotos().isEmpty()) {
        System.out.println("Imagen: " + publicacion.getFotos().get(0));
        publicacion.setImagen(publicacion.getFotos().get(0));
    }
}



        // Agregar la lista de publicaciones al modelo

    model.addAttribute("publicaciones", publicaciones);
        if (usuario == null || token == null) {
            return "redirect:/nl/home";
        }

        model.addAttribute("usuario", usuario);

        return "home";
    }


}
