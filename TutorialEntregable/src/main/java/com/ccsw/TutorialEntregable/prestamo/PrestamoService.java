package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface PrestamoService {

    /**
     *
     * @param nameGame nombre del juego
     * @param nameClient nombre del cliente
     * @param iniDate fecha de inicio
     * @param endDate fecha de fin
     * @param dto dto de la búsqueda
     * @return {@link Page} de {@link Prestamo}
     */
    Page<Prestamo> findPageWithFilters(String nameGame, String nameClient, Date iniDate, Date endDate, PrestamoSearchDto dto);

    /**
     * Método para crear o actualizar un {@link Prestamo}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return {@link ResponseEntity} con un mensaje y un código de estado HTTP
     */
    ResponseEntity<String> save(Long id, PrestamoDto dto);

    /**
     * Método para crear o actualizar un {@link Prestamo}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;
}
