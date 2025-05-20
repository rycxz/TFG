package kiricasa.programa.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.UsuarioRepository;
import kiricasa.programa.requests.LoginRequest;
import kiricasa.programa.requests.RegisterRequest;
import kiricasa.programa.service.AuthService;
import kiricasa.programa.service.CorreoService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final CorreoService correoService;


   @PostMapping("/login")
public String login(
        @ModelAttribute LoginRequest request,
        HttpSession session,
        RedirectAttributes redirectAttributes
) {
    try {
        // Intentamos autenticar
        String token = authService.login(request).getToken();
        session.setAttribute("jwt", token);

        UsuarioModel usuario = usuarioRepository
            .findByNombreIgnoreCase(request.getNombre())
            .orElse(null);
        session.setAttribute("usuario", usuario);

        return "redirect:/home";
    } catch (Exception ex) {
        // Capturamos cualquier excepción de credenciales inválidas
        redirectAttributes.addFlashAttribute("loginError", "Usuario o contraseña incorrectos");
        return "redirect:/auth/login";
    }
}

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        // Si hay errores de validación, volvemos a la vista de registro
        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.registerRequest", bindingResult);
            model.addAttribute("registerRequest", request);
            return "register";
        }

        // Si todo OK, procedemos a crear y autenticar
        String token = authService.register(request).getToken();
        session.setAttribute("jwt", token);

        UsuarioModel usuario = usuarioRepository
            .findByNombreIgnoreCase(request.getNombre())
            .orElse(null);
        session.setAttribute("usuario", usuario);

        return "redirect:/home";
    }


@GetMapping("/recupera")
public String mostrarFormularioRecuperacion() {

    return "recupera";
}
@PostMapping("/recupera")
public String procesarFormularioRecuperacion(
        @RequestParam("email") String email,
        Model model) {
    System.out.println("Email recibido: " + email);
    try {
        correoService.enviarCodigoRecuperacion(email);
        model.addAttribute("email", email);
        return "introduce-codigo"; // OK: solo si el email es válido
    } catch (UsernameNotFoundException e) {
        model.addAttribute("error", "No existe un usuario con ese correo");
        model.addAttribute("email", email); // opcional: para que el campo quede rellenado
        return "recupera"; // Volver a la página original
    }
}

@PostMapping("/nueva-password")
public String cambiarPassword(
        @RequestParam("email") String email,
        @RequestParam("codigo") String codigo,
        @RequestParam("nuevaPassword") String nuevaPassword,
        Model model) {

    try {
        authService.cambiarPasswordConCodigo(email, codigo, nuevaPassword);
        return "redirect:/login?recuperacionExitosa"; // Redirige al login
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("email", email);
        return "introduce-codigo"; // Vuelve a la vista con error
    }
}



}




