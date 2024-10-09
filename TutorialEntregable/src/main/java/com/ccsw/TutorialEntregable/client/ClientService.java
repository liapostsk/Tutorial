package com.ccsw.TutorialEntregable.client;

import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.client.model.ClientDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface ClientService {

    /**
     * Recupera un {@link Client} a través de su ID
     *
     * @param id PK de la entidad
     * @return {@link Client}
     */
    Client get(Long id);

    /**
     * Método para recuperar todas los clientes
     *
     * @return {@link List} de {@link Client}
     */
    List<Client> findAll();

    /**
     * Método para crear o actualizar un cliente
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     * @return {@link ResponseEntity} con el estado y mensaje correspondiente
     */
    ResponseEntity<?> save(Long id, ClientDto dto);

    /**
     * Método para borrar un cliente
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

}