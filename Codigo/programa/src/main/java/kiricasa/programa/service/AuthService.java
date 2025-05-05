package kiricasa.programa.service;


import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import kiricasa.programa.controller.AuthReponse;
import kiricasa.programa.controller.LoginRequest;
import kiricasa.programa.controller.RegisterRequets;
import kiricasa.programa.models.UsuarioModel;
import kiricasa.programa.repository.UsuarioRepository;
import kiricasa.programa.roles.UsuarioRol;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

     private final UsuarioRepository usuarioRepository;
     private final JwtService jwtService;
    public AuthReponse login(LoginRequest request) {

      return null;
    }

    /**
     * metodo que recibe el requets para crear un nuevo objeto usuario con el builder  lo guarda en la BBDD
     * y devuelve el token de acceso
     * @param request
     * @return
     */
    public AuthReponse register(RegisterRequets request) {
  UsuarioModel usuario = UsuarioModel.builder()
                        .nombre(request.getNombre())
                        .password(request.getPassword())
                        .email(request.getEmail())
                        .numero(request.getNumero())
                        .esAdmin(false)
                        .fechaNacimiento(request.getFechaNacimiento())
                        .fechaRegistro(LocalDateTime.now())
                        .fechaAdmin(null)
                        .rol(UsuarioRol.USER)
                        .build();
                        //insertamos el nuevo registo en la BBDD
                        usuarioRepository.save(usuario);
                        //devolvemos el token de acceso
                        //el token se genera con el metodo getToken de la clase JwtService
                        String token = jwtService.getToken(usuario);
                    System.out.println("🔑 Token generado: " + token);



                        return AuthReponse.builder()
                        .token(jwtService.getToken(usuario))
                        .build();


    }

}
