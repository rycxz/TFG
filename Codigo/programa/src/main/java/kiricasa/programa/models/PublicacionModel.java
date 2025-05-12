/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import kiricasa.programa.enums.TipoPiso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String imagen2;
    private String imagen3;
    private String imagen4;
    private String imagen5;
    private String imagen6;
    private String imagen7;
    private String imagen8;
    private String imagen9;
    @Column(nullable = false)
    private String ubicacion;
    @Enumerated(EnumType.STRING)
    private TipoPiso tipo;

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
 @ToString.Exclude
    private UsuarioModel usuario;
    //esto aplica igual que para lo de arriba
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barrio", nullable = false)
     @ToString.Exclude
    private BarriosModel barrio;
    @Transient
    private String imagenAleatoria;

    public String getImagenAleatoria() {
        return imagenAleatoria;
    }

    public void setImagenAleatoria(String imagenAleatoria) {
        this.imagenAleatoria = imagenAleatoria;
    }

    public List<String> getFotos() {
        // Devuelve una lista con todas las fotos de la publicación
        List<String> fotos = List.of(imagen, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9);
        return fotos.stream()
                .filter(foto -> foto != null && !foto.isEmpty())
                .toList();
    }


}
