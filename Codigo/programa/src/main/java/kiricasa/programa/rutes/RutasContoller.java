package kiricasa.programa.rutes;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author recur
 */

@RestController
@RequestMapping("/kiricasa")
@RequiredArgsConstructor
public class RutasContoller {

    @RequestMapping("/rutas")
    public String rutas() {
        return "Hola desde la ruta /rutas";
    }

    @RequestMapping("/rutas/1")
    public String rutas1() {
        return "Hola desde la ruta /rutas/1";
    }
}
