/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.models;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "usuarios")

public class UsuarioModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
   private  String nombre;
   private String password;
   private  String email;
   private String numero;
  private  boolean esAdmin;
  private LocalDateTime fechaNacimiento;
  @CreationTimestamp
  private LocalDateTime fechaRegistro;
  @UpdateTimestamp
  private LocalDateTime fechaAdmin;

}
