package kiricasa.programa.requests;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import kiricasa.programa.roles.UsuarioRol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author recur
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String nombre;
    String password;
    String email;
    String numero;
    boolean esAdmin;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate fechaNacimiento;
    LocalDate fechaRegistro;
    LocalDate fechaAdmin;
    UsuarioRol rol;
    private boolean recibirNotificaciones;


}
