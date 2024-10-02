package com.ccsw.TutorialEntregable.client;

import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Client get(Long id) {

        return this.clientRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, ClientDto dto) {
        // Buscar si ya existe un cliente con el mismo nombre
        Optional<Client> existingClient = this.clientRepository.findByName(dto.getName());

        // Si existe un cliente con ese nombre y no es una actualización del mismo cliente (por ID), lanzar excepción
        if (existingClient.isPresent() && (id == null || !existingClient.get().getId().equals(id))) {
            throw new IllegalArgumentException("Ya existe un cliente con el nombre: " + dto.getName());
        }

        Client client;

        if (id == null) {
            client = new Client();
        } else {
            client = this.get(id);
        }

        client.setName(dto.getName());

        this.clientRepository.save(client); //se guarda o se actualiza
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.clientRepository.deleteById(id);
    }

}