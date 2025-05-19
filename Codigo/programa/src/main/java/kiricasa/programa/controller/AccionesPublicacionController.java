/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kiricasa.programa.enums.TipoPiso;
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
@RequestMapping("/publicacion")
@AllArgsConstructor
public class AccionesPublicacionController {
        private final PublicacionRepository publicacionRepository;
              private final BarriosRepository barriosRepository;
@GetMapping("/editar/{id}")
public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model, HttpSession session) {
    UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/auth/login";

    PublicacionModel publicacion = publicacionRepository.findById(id).orElse(null);
    if (publicacion == null) return "redirect:/home";

    // Solo dueño o admin
    if (!usuario.getRol().equals(UsuarioRol.ADMIN) && !publicacion.getUsuario().getId().equals(usuario.getId())) {
        return "redirect:/home";
    }
    model.addAttribute("usuario", usuario);
    model.addAttribute("publicacion", publicacion);
    model.addAttribute("barrios", barriosRepository.findAll());

    return "publicacion_editar"; // vista
}

/**
 * Método para subir una imagen a una publicación.
 *
 * @param id                 ID de la publicación.
 * @param archivo            Archivo de imagen a subir.
 * @param redirectAttributes Atributos para redirección.
 * @return Redirección a la página de edición de la publicación.
 *
 * @param id
 * @param archivo
 * @param redirectAttributes
 * @return
 */
@PostMapping("/editar/{id}/subir-imagen")
public String subirImagen(@PathVariable Long id,
                          @RequestParam("imagen") MultipartFile archivo,
                          RedirectAttributes redirectAttributes) {



    PublicacionModel publicacion = publicacionRepository.findById(id).orElse(null);
    if (publicacion == null) {
        redirectAttributes.addFlashAttribute("error", "La publicación no existe.");
        return "redirect:/home";
    }

    if (archivo == null || archivo.isEmpty()) {
        redirectAttributes.addFlashAttribute("error", "No se seleccionó ninguna imagen.");
        return "redirect:/detalle?id=" + id;
    }

    // Reasignar carpeta si es la predeterminada
    String carpetaActual = publicacion.getCarpetaImagen();
    if (carpetaActual.equals("pisos_auto")) {
        carpetaActual = "publicacion_" + id;
        publicacion.setCarpetaImagen(carpetaActual);

    }

    // RUTA ABSOLUTA para guardar en disco correctamente
    String basePath = "C:/Users/6003194/Desktop/TFG/Codigo/programa/uploads/publicaciones/";
    String carpetaFinal = basePath + carpetaActual;
    File directorio = new File(carpetaFinal);
    if (!directorio.exists()) {
        boolean creado = directorio.mkdirs();

    }

    try {
        String nombreArchivo = archivo.getOriginalFilename();

        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {

            redirectAttributes.addFlashAttribute("error", "Nombre de archivo inválido.");
            return "redirect:/detalle?id=" + id;
        }

        Path ruta = Paths.get(carpetaFinal, nombreArchivo);


        archivo.transferTo(ruta.toFile());

        // Actualizar primer hueco vacío
        List<String> imagenes = new ArrayList<>(publicacion.getFotos());
        for (int i = 0; i < imagenes.size(); i++) {
            if (imagenes.get(i) == null || imagenes.get(i).isEmpty() || imagenes.get(i).equals("predeterminada.png")) {
                imagenes.set(i, nombreArchivo);
                              break;
            }
        }





        redirectAttributes.addFlashAttribute("success", "Imagen subida correctamente.");

    } catch (IOException e) {
        System.out.println("❌ Error al guardar imagen en disco");
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("error", "Error al guardar la imagen.");
    }

    return "redirect:/detalle?id=" + id;
}

@GetMapping("/editar/{id}/eliminar-imagen")
public String eliminarImagen(@PathVariable Long id,
                             @RequestParam("imagen") String nombre,
                             RedirectAttributes redirectAttributes) {

    PublicacionModel publicacion = publicacionRepository.findById(id).orElse(null);
    if (publicacion == null) {
        redirectAttributes.addFlashAttribute("error", "Publicación no encontrada.");
        return "redirect:/home";
    }

    // Detectar la posición de la imagen en la lista original
    List<String> imagenes = publicacion.getFotosOriginales();
    int posicion = imagenes.indexOf(nombre);

    if (posicion != -1) {

        publicacion.setImagenPorIndice(posicion,"" );
        publicacionRepository.save(publicacion);

        // Eliminar físicamente el archivo
        String ruta = "C:/Users/6003194/Desktop/TFG/Codigo/programa/uploads/publicaciones/"
                    + publicacion.getCarpetaImagen() + "/" + nombre;

        File archivo = new File(ruta);
        if (archivo.exists()) archivo.delete();

        redirectAttributes.addFlashAttribute("success", "Imagen eliminada correctamente.");
    } else {
        redirectAttributes.addFlashAttribute("error", "Imagen no encontrada.");
    }

    return "redirect:/detalle?id=" + id;
}

/**
 * Método para mostrar el formulario de edición de una publicación.
 *
 * @param id                 ID de la publicación.
 * @param model              Modelo para la vista.
 * @param session            Sesión HTTP.
 * @param redirectAttributes Atributos para redirección.
 * @return Vista del formulario de edición.
 *
 * @param id
 * @param model
 * @param session
 * @param redirectAttributes
 * @return
 */
@PostMapping("/editar/{id}/editarinfo")
public String editarInfoPublicacion(@PathVariable Long id,
                                    @RequestParam String titulo,
                                    @RequestParam String descripcion,
                                    @RequestParam String precio,
                                    @RequestParam String estado,
                                    @RequestParam TipoPiso tipo,
                                    @RequestParam String ubicacion,
                                    @RequestParam int metrosCuadrados,
                                    @RequestParam String habitaciones,
                                    @RequestParam(required = false, defaultValue = "false") boolean permiteMascotas,
                                    @RequestParam int numeroCompañeros,
                                    @RequestParam Long barrioId,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {

    UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
    String token = (String) session.getAttribute("jwt");

    if (usuario == null || token == null) {
        redirectAttributes.addFlashAttribute("error", "Sesión expirada.");
        return "redirect:/nl/home";
    }

    Optional<PublicacionModel> optionalPublicacion = publicacionRepository.findById(id);
    if (optionalPublicacion.isEmpty()) {
        redirectAttributes.addFlashAttribute("error", "La publicación no existe.");
        return "redirect:/home";
    }

    PublicacionModel publicacion = optionalPublicacion.get();

    // Verificar permisos
    if (!usuario.getId().equals(publicacion.getUsuario().getId()) && usuario.getRol() != UsuarioRol.ADMIN) {
        redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar esta publicación.");
        return "redirect:/home";
    }

    // Actualizar los datos
    publicacion.setTitulo(titulo);
    publicacion.setDescripcion(descripcion);
    publicacion.setPrecio(precio);
    publicacion.setEstado(estado);
    publicacion.setTipo(tipo);
    publicacion.setUbicacion(ubicacion);
    publicacion.setMetrosCuadrados(metrosCuadrados);
    publicacion.setHabitaciones(habitaciones);
    publicacion.setPermiteMascotas(permiteMascotas);
    publicacion.setNumeroCompañeros(numeroCompañeros);

    barriosRepository.findById(barrioId).ifPresent(publicacion::setBarrio);

    publicacionRepository.save(publicacion);

    redirectAttributes.addFlashAttribute("success", "Cambios guardados correctamente.");
    return "redirect:/detalle?id=" + id;

}





}
