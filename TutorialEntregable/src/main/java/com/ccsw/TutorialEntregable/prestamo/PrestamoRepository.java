package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PrestamoRepository extends CrudRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {
    /**
     * Método para recuperar un listado paginado de {@link Prestamo} aplicando fetch optimizado.
     *
     * @param spec Especificación de búsqueda (filtros)
     * @param pageable param de paginación
     * @return {@link Page} de {@link Prestamo}
     */
    @Override
    @EntityGraph(attributePaths = { "game", "client" })
    // Optimiza el fetch de las relaciones
    Page<Prestamo> findAll(Specification<Prestamo> spec, Pageable pageable);

}
