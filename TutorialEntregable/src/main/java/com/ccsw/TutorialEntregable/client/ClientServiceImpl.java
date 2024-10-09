package com.ccsw.TutorialEntregable.client;

import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public ResponseEntity<?> save(Long id, ClientDto dto) {
        Client client;
        // Buscar si ya existe un cliente con el mismo nombre
        Optional<Client> existingClient = this.clientRepository.findByName(dto.getName());

        // 1. Si existe un cliente con ese nombre y no es una actualización del mismo cliente (por ID), lanzar excepción
        if (existingClient.isPresent() && (id == null || !existingClient.get().getId().equals(id))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Ya existe un cliente con el nombre: " + dto.getName()));
        }

        // 2. Si no existe, crear o actualizar el cliente
        if (id == null) {
            client = new Client();
        } else {
            client = this.clientRepository.findById(id).orElse(null);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Cliente no encontrado."));
            }
        }

        // 3. Copiar propiedades del DTO al cliente
        client.setName(dto.getName());

        try {
            // Guardar el cliente en el repositorio
            this.clientRepository.save(client);
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Cliente guardado exitosamente."));
        } catch (Exception e) {
            // Manejar y devolver error genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Ocurrió un error al guardar el cliente: " + e.getMessage()));
        }
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