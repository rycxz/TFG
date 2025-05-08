package kiricasa.programa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kiricasa.programa.models.BarriosModel;
import kiricasa.programa.models.PublicacionModel;
import kiricasa.programa.models.UsuarioModel;

@Repository
public interface PublicacionRepository extends JpaRepository<PublicacionModel, Long> {

    // Buscar publicaciones por usuario
    List<PublicacionModel> findByUsuario(UsuarioModel usuario);

    // Buscar publicaciones por barrio
    List<PublicacionModel> findByBarrio(BarriosModel barrio);

    // Buscar publicaciones disponibles
    List<PublicacionModel> findByEstado(String estado);

    // Buscar publicaciones por tipo (piso, habitación, etc.)
    List<PublicacionModel> findByTipo(String tipo);

    // Buscar por usuario y estado
    List<PublicacionModel> findByUsuarioAndEstado(UsuarioModel usuario, String estado);

    // Buscar por barrio y estado
    List<PublicacionModel> findByBarrioAndEstado(BarriosModel barrio, String estado);

    // Buscar publicaciones que permitan mascotas
    List<PublicacionModel> findByPermiteMascotasTrue();

    // Buscar publicaciones con mínimo de metros cuadrados
    List<PublicacionModel> findByMetrosCuadradosGreaterThanEqual(int metros);
}
