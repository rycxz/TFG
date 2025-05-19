/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kiricasa.programa.models.FavoritosModel;
import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.FavoritosRepository;
import kiricasa.programa.repository.PublicacionRepository;
import kiricasa.programa.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

/**
 *
 * @author 6003194
 */
@Controller
@RequestMapping("/perfil")
@AllArgsConstructor
public class PerfilController {
    private PublicacionRepository publicacionRepository;
    private FavoritosRepository favoritosRepository;
    private UsuarioRepository usuarioRepository;

         /**
          * Método para mostrar el perfil del usuario.
          * @param model
          * @param session
          * @return
          */
       @GetMapping("/ver")
         public String verPerfil(Model model, HttpSession session) {
              String token = (String) session.getAttribute("jwt");
              //hay que obtener el usuario logueado,sus publicaciones y sus favoritos
              UsuarioModel usuarioLogueado = (UsuarioModel) session.getAttribute("usuario");
            List<PublicacionModel> publicaciones = publicacionRepository.findByUsuario(usuarioLogueado);
            List<FavoritosModel> favoritos = favoritosRepository.findByUsuario(usuarioLogueado);
              if (usuarioLogueado == null || token == null) {
                return "redirect:/nl/home";
              }

              model.addAttribute("usuario", usuarioLogueado);
                model.addAttribute("publicaciones", publicaciones);
                model.addAttribute("favoritos", favoritos);

              return "perfil";
         }
        @GetMapping("/editar")
        public String mostrarFormularioEdicion(HttpSession session, Model model) {
            UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
            String token = (String) session.getAttribute("jwt");

            if (usuario == null || token == null) {
                return "redirect:/nl/home";
            }

            model.addAttribute("usuario", usuario);
            return "perfil_editar"; // Vista con el formulario
        }
        @PostMapping("/editar")
        public String procesarEdicionPerfil(@ModelAttribute UsuarioModel usuarioForm,
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) {

            UsuarioModel usuarioSesion = (UsuarioModel) session.getAttribute("usuario");

            if (usuarioSesion == null) {
                return "redirect:/nl/home";
            }

            // Copiar solo los campos editables que quieras permitir
            usuarioSesion.setNombre(usuarioForm.getNombre());
            usuarioSesion.setEmail(usuarioForm.getEmail());
            // Añade aquí cualquier otro campo editable

            usuarioRepository.save(usuarioSesion);
            session.setAttribute("usuario", usuarioSesion); // Actualizar en sesión

            redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente.");
            return "redirect:/perfil/ver";
        }
        @GetMapping("/2fa")
       public String mostrarFormulario2FA(HttpSession session, Model model) {
        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/auth/login";

        if (usuario.isVerificado()) {
            return "redirect:/perfil"; // Ya activado
        }

        // Generar código de 6 cifras
        String codigo = String.format("%06d", new Random().nextInt(999999));

        // Guardar en BD
        usuario.setCodigoVerificacion(codigo);
        usuarioRepository.save(usuario);

        // (Opcional) Simular envío por consola o correo
        System.out.println("🔐 Código 2FA: " + codigo);

        model.addAttribute("usuario", usuario);
        return "perfil_2fa_verificar"; // Vista con campo para introducir código
    }
        @PostMapping
    public String verificarCodigo(@RequestParam("codigo") String codigoIngresado,
                                  HttpSession session,
                                  Model model) {
        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/auth/login";

        if (usuario.getCodigoVerificacion().equals(codigoIngresado)) {
            usuario.setVerificado(true);
            usuario.setCodigoVerificacion(null);
            usuarioRepository.save(usuario);

            session.setAttribute("usuario", usuario); // actualizar en sesión
            model.addAttribute("success", "Verificación completada.");
            return "redirect:/perfil/ver";
        } else {
            model.addAttribute("error", "Código incorrecto.");
            return "perfil_2fa_verificar";
        }
    }



}
