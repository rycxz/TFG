package kiricasa.programa.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publicaciones")
public class PublicacionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @CreationTimestamp
    private LocalDateTime fechaPublicacion;

    private String carpeta = "publicacion_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @ToString.Exclude
    private UsuarioModel usuario;

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

    /**
     * ✅ Devuelve exactamente las 9 posiciones, pero si alguna es null o vacía la reemplaza con "" (string vacío)
     * Para evitar NullPointerException en las vistas
     */
    @Transient
    public List<String> getListaImagenes() {
        List<String> imagenes = new ArrayList<>();
        imagenes.add(imagen != null ? imagen : "");
        imagenes.add(imagen2 != null ? imagen2 : "");
        imagenes.add(imagen3 != null ? imagen3 : "");
        imagenes.add(imagen4 != null ? imagen4 : "");
        imagenes.add(imagen5 != null ? imagen5 : "");
        imagenes.add(imagen6 != null ? imagen6 : "");
        imagenes.add(imagen7 != null ? imagen7 : "");
        imagenes.add(imagen8 != null ? imagen8 : "");
        imagenes.add(imagen9 != null ? imagen9 : "");
        return imagenes;
    }

    /**
     * ✅ Devuelve solo las imágenes válidas (no nulas y no vacías)
     */
    @Transient
    public List<String> getFotos() {
        return List.of(imagen, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9)
                .stream()
                .filter(f -> f != null && !f.isEmpty())
                .collect(Collectors.toList());
    }

    public String getCarpetaImagen() {
        return carpeta;
    }

    public void setCarpetaImagen(String carpetaImagen) {
        this.carpeta = carpetaImagen;
    }
}
