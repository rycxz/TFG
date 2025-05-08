/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author recur
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publicaciones")
public class PublicacionModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long id;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String descripcion;
    @CreationTimestamp
    private LocalDateTime fechaPublicacion;
    private String imagen;
    @Column(nullable = false)
    private String ubicacion;
    private String tipo;
    private String precio;
    private String estado;
    @Column(nullable = false)
    private int metrosCuadrados;
    private String habitaciones;
    private boolean permiteMascotas;
    private int numeroCompañeros;
       /*
     * hay que tener algo en cuenta aqui
     * antes cuando inicaliabas un anucnio hacias :
     *  publicacion.setIdUsuario(buscabas el usuario por id);
     *
     * ahora hay que hacer:
     * publicacion.setUsuario(usuarioRepository.findById(id).orElseThrow());
     * esto es por que asi Spring Data JPA puede hacer joins automáticos yo antes lo hacia mal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModel usuario;
    //esto aplica igual que para lo de arriba
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barrio", nullable = false)
    private BarriosModel barrio;


}
