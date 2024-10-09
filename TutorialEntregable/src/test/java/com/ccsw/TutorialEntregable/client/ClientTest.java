package com.ccsw.TutorialEntregable.client;

import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
    Esta clase contiene todos los tests unitarios
 */

//Indica que no se ha de inicializar el contexto porque son test estaticos y da igual
@ExtendWith(MockitoExtension.class)
public class ClientTest {

    @Mock
    private ClientRepository clientRepository; //simula el Repo que tenemos

    @InjectMocks
    private ClientServiceImpl clientService; //simula el Service, la interficie

    // Define el ID que usarás en el test de eliminación
    private static final Long EXISTS_CLIENT_ID = 1L;
    private static final Long NOT_EXISTS_CLIENT_ID = 0L;
    public static final String CLIENT_NAME = "CLIENT1";
    public static final String EXISTING_CLIENT_NAME = "Lia";

    @Test
    public void getExistsClientIdShouldReturnClient() {

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(EXISTS_CLIENT_ID);
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        Client authorResponse = clientService.get(EXISTS_CLIENT_ID);

        assertNotNull(authorResponse);

        assertEquals(EXISTS_CLIENT_ID, authorResponse.getId());
    }

    @Test
    public void getNotExistsAuthorIdShouldReturnNull() {

        when(clientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty());

        Client client = clientService.get(NOT_EXISTS_CLIENT_ID);

        assertNull(client);
    }

    /*
        Resumen
        Configuración del Entorno: @ExtendWith(MockitoExtension.class) y las anotaciones @Mock y @InjectMocks preparan el entorno de prueba y gestionan los mocks.
        Preparación de Datos: Se crea una lista mockeada para simular datos del repositorio.
        Configuración del Comportamiento: Se configura el mock del repositorio para devolver la lista preparada.
        Ejecución y Verificación: Se ejecuta el método bajo prueba y se verifican los resultados para asegurar que el comportamiento del servicio es el esperado.
     */

    @Test
    public void findAllShouldReturnAllClients() {
        //Creación de datos simulados
        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class)); //esto simula un cliente  que se añade a la lista cuando el repositorio sea llamado

        //Configuración del comportamiento del mock
        when(clientRepository.findAll()).thenReturn(list); //configura el mock para que cuando se llama al metodo findAll, devuelva la list preparada anteriormente

        List<Client> clients = clientService.findAll();

        assertNotNull(clients); //verificación de que la lista es not null
        assertEquals(1, clients.size()); //verificación test
    }

    @Test
    public void saveNotExistsClientIdShouldInsert() {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        /*
            Como buscas guardar un nuevo client, sabes que al guardarla esta sera una entity
            ArgumentCaptor se usa para capturar un argumento de tipo Client. Esto posteriormente
            permitirá inspeccionar el objeto Client para ver si se ha hecho correctamente la insercion de sus valores
            al realizarse el "save"
         */
        ArgumentCaptor<Client> client = ArgumentCaptor.forClass(Client.class);

        //se invoca al servicio interface y se hace el guardado id = null, porque es un nuevo cliente
        clientService.save(null, clientDto);

        //Se verifica si el metodo save del clientRepository fue llamado con el argumento caoturado
        verify(clientRepository).save(client.capture());

        //compara que el nombre del client coincida con el del cliente guardado
        assertEquals(CLIENT_NAME, client.getValue().getName());
    }

    @Test
    public void saveClientWithExistingNameClientIdShouldException() {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXISTING_CLIENT_NAME);

        Client existingClient = new Client();
        existingClient.setName(EXISTING_CLIENT_NAME);

        when(clientRepository.findByName(EXISTING_CLIENT_NAME)).thenReturn(Optional.of(existingClient));

        ResponseEntity<?> response = clientService.save(null, clientDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ya existe un cliente con el nombre: " + EXISTING_CLIENT_NAME, ((java.util.Map) response.getBody()).get("error"));
    }

    // Pruebas de borrado
    @Test
    public void deleteExistsClientIdShouldDelete() throws Exception {

        Client client = mock(Client.class); //simulación de un entity client

        //Configuración del comportamiento: Cuando lo encuentre lo devuelva, se usa Optional para gestionar en el caso de que no este
        //Evita que si no existe no nos de NullPointerException, si no esta nos devuelve un optional.empty()
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        //funcionalidad
        clientService.delete(EXISTS_CLIENT_ID);

        //verifica que se ha borrado del "repo mockeado"
        verify(clientRepository).deleteById(EXISTS_CLIENT_ID);
    }

}