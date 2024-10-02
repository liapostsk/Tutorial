package com.ccsw.TutorialEntregable.client;

import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.client.model.ClientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Client", description = "API of Client")
@RequestMapping(value = "/client")
@RestController
@CrossOrigin(origins = "*")
public class ClientController {

    //Dentro del controlador inyectamos el servicio (funcionalidad, logica de negocio)
    @Autowired
    ClientService clientService;

    //Se usa para poder hacer las conversiones de entidades y DTOs
    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar todas las {@link Client}
     *
     * @return {@link List} de {@link ClientDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of Clients")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ClientDto> findAll() {

        List<Client> clients = this.clientService.findAll();

        /*
            Esto coge la lista clients y usa la funcion stream() que nos permite hacer cosas
            sobre este objeto sin modificarlo, usamos adicionalmente .map() que lo que hace es mapear para elemento de
            la lista (e como instancia de cada elemento lo convierte a DTO).
            Finalmente usamos .collect() metodo que finaliza el procesado del stream, define como recolectar los elementos del stream
            en este caso los colecta en una lista .toList().

            Resumen: Se nos acaba retornando una lista de DTO, que es lo que busca el metodo}
         */

        return clients.stream().map(e -> mapper.map(e, ClientDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar una {@link Client}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Client")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto dto) {

        this.clientService.save(id, dto);
    }

    /**
     * Método para borrar una {@link Client}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Client")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.clientService.delete(id);
    }

}