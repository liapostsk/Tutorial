package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Prestamo", description = "API of Prestamo")
@RequestMapping(value = "/prestamo")
@RestController
@CrossOrigin(origins = "*")
public class PrestamoController {

    @Autowired
    PrestamoService prestamoService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar un listado paginado y filtrado de {@link Prestamo}
     *
     * @param nameGame nombre del juego
     * @param nameClient nombre del cliente
     * @param iniDate fecha de inicio del prestamo
     * @param endDate fecha de fin del prestamo
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link PrestamoDto}
     */
    @Operation(summary = "Find Page with Filters", description = "Method that returns a page of filtered Prestamos")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<PrestamoDto> findPageWithFilters(@RequestParam(value = "nameGame", required = false) String nameGame, @RequestParam(value = "nameClient", required = false) String nameClient,
            @RequestParam(value = "iniDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date iniDate, @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestBody PrestamoSearchDto dto) {
        // Llamada al servicio, se encarga de devolver una pagina filtrada
        Page<Prestamo> page = this.prestamoService.findPageWithFilters(nameGame, nameClient, iniDate, endDate, dto);
        // Hemos de devolver Page<PrestamoDto> traducimos para cada entity Prestamo a PrestamoDto
        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para crear o actualizar un {@link Prestamo}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return {@link ResponseEntity} con el estado y mensaje correspondiente
     */
    @Operation(summary = "Save", description = "Method that saves or updates a Prestamo")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<?> save(@PathVariable(name = "id", required = false) Long id, @RequestBody PrestamoDto dto) {
        return this.prestamoService.save(id, dto);
    }

    /**
     * Método para borrar un {@link Prestamo}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Prestamo")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        this.prestamoService.delete(id);
    }

}