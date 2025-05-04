/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String titulo;
    private String descripcion;
    @CreationTimestamp
    private LocalDateTime fechaPublicacion;
    private String imagen;
    private String ubicacion;
    private String tipo;
    private String precio;
    private String estado;
    private int metrosCuadrados;
    private String habitaciones;
    private boolean permiteMascotas;
    private int numeroCompañeros;
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;
    @Column(name = "id_barrio", nullable = false)
    private Long idBarrio;

}
