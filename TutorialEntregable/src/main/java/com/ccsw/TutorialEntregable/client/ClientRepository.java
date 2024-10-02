package com.ccsw.TutorialEntregable.client;

import com.ccsw.TutorialEntregable.client.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author ccsw
 *
 */
public interface ClientRepository extends CrudRepository<Client, Long> {
    Optional<Client> findByName(String name); //Consulta que manda la query para encontrar por nombre
}