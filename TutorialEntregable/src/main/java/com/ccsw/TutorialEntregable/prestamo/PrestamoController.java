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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            @RequestParam(value = "iniDate", required = false) LocalDate iniDate, @RequestParam(value = "endDate", required = false) LocalDate endDate, @RequestBody PrestamoSearchDto dto) {
        // Llamada al servicio, se encarga de devolver una pagina filtrada
        Page<Prestamo> page = this.prestamoService.findPageWithFilters(nameGame, nameClient, iniDate, endDate, dto);

        // Hemos de devolver Page<PrestamoDto> traducimos para cada entity Prestamo a PrestamoDto
        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para recuperar un listado paginado de {@link Prestamo}
     *
     * @param dto dto de búsqueda
     * @return {@link Prestamo} de {@link PrestamoDto}
     */
    /*
    @Operation(summary = "Find Page", description = "Method that return a page of Prestamos")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<PrestamoDto> findPage(@RequestBody PrestamoSearchDto dto) {
        Page<Prestamo> page = this.prestamoService.findPage(dto);
        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }
    */

    //Listado Filtrado

    /**
     * Método para recuperar una lista de {@link Prestamo}
     *
     * @param nameGame nombre del juego
     * @param nameClient nombre del cliente
     * @param iniDate fecha de inicio del prestamo
     * @param endDate fecha de fin del prestamo
     * @return {@link List} de {@link PrestamoDto}
     */
    /*
    @Operation(summary = "Find", description = "Method that return a filtered list of Prestamos")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<PrestamoDto> find(@RequestParam(value = "nameGame", required = false) String nameGame, @RequestParam(value = "nameClient", required = false) String nameClient,
            @RequestParam(value = "iniDate", required = false) LocalDate iniDate, @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        List<Prestamo> prestamos = prestamoService.find(nameGame, nameClient, iniDate, endDate);

        return prestamos.stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList());
    }
    */

    /**
     * Método para crear o actualizar un {@link Prestamo}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Prestamo")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody PrestamoDto dto) {
        this.prestamoService.save(id, dto);
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