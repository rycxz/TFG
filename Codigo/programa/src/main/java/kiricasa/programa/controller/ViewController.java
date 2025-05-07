package kiricasa.programa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class ViewController {

    @GetMapping("/register")
    public String showRegisterPage() {
        System.out.println("register--------------- pero por get");
        return "registro";  // Thymeleaf buscará templates/registro.html
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
