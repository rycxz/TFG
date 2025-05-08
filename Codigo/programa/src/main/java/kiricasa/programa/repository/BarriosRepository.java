package kiricasa.programa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kiricasa.programa.models.BarriosModel;

@Repository
public interface BarriosRepository extends JpaRepository<BarriosModel, Long> {

    // Buscar por nombre exacto
    Optional<BarriosModel> findByNombre(String nombre);

    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<BarriosModel> findByNombreIgnoreCase(String nombre);

    // Buscar barrios que contienen cierta palabra en el nombre
    List<BarriosModel> findByNombreContainingIgnoreCase(String palabra);

    // Buscar barrios por código postal
    List<BarriosModel> findByCodigoPostal(String codigoPostal);

    // Buscar barrios por ubicación aproximada
    List<BarriosModel> findByUbicacionContainingIgnoreCase(String ubicacion);

    // Buscar barrios que tengan más de X anuncios
    List<BarriosModel> findByCantidadAnunciosGreaterThan(int cantidad);

    // Ordenar barrios por número de anuncios (descendente)
    List<BarriosModel> findAllByOrderByCantidadAnunciosDesc();
}
