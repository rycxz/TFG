/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package kiricasa.programa.controller;

import java.time.LocalDate;

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
public class RegisterRequets {
    String nombre;
    String password;
    String email;
    String numero;
    boolean esAdmin;
    LocalDate fechaNacimiento;
    LocalDate fechaRegistro;
    LocalDate fechaAdmin;
}
