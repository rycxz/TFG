/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author 6003194
 */
@Controller
@RequestMapping("/nl")
public class NoLoggedController  {
    /**
     * * Método que muestra la página de inicio
     * Lo que se quiere hacer con esto es que si el usuario no esta loggeado y por x motivo llega al controlador de HomeController
     * se le redirija a la página de inicio de no loggeado
     * @return
     */
    @RequestMapping("/home")
    public String mostrarHome() {
        return "home";
    }

}
