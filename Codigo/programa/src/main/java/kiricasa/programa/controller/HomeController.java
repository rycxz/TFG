/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;


/**
 *
 * @author 6003194
 */
@Controller
@RequestMapping()
public class HomeController {

  @GetMapping("/home")
    public String mostrarHome(Model model, HttpSession session) {
        /* if (!sessionUtils.isUserLogged(session))
        {
        return "redirect:/auth/login";
        } */
        String token = (String) session.getAttribute("jwt");
        Object usuario = session.getAttribute("usuario");
        System.out.println("usuario en home: " + usuario);
        System.out.println("token en home: " + token);
        if (usuario == null || token == null) {
            return "redirect:/nl/home";
        }

        model.addAttribute("usuario", usuario);

        return "home";
    }


}
