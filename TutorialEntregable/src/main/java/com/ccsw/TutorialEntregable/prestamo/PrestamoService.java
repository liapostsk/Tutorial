package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoSearchDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoService {

    /**
     * Recupera los juegos filtrando opcionalmente por título y/o categoría
     *
     * @param nameGame nombre del juego
     * @param nameClient nombre del cliente
     * @return {@link List} de {@link Prestamo}
     */
    List<Prestamo> find(String nameGame, String nameClient, LocalDate iniDate, LocalDate endDate);

    /**
     * Método para recuperar un listado paginado de {@link Prestamo}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link Prestamo}
     */
    Page<Prestamo> findPage(PrestamoSearchDto dto);

    /**
     *
     * @param nameGame nombre del juego
     * @param nameClient nombre del cliente
     * @param iniDate fecha de inicio
     * @param endDate fecha de fin
     * @param dto dto de la búsqueda
     * @return {@link Page} de {@link Prestamo}
     */
    Page<Prestamo> findPageWithFilters(String nameGame, String nameClient, LocalDate iniDate, LocalDate endDate, PrestamoSearchDto dto);

    /**
     * Método para crear o actualizar un {@link Prestamo}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, PrestamoDto dto);

    /**
     * Método para crear o actualizar un {@link Prestamo}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;
}
