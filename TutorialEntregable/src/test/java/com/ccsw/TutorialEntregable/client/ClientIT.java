package com.ccsw.TutorialEntregable.client;

import com.ccsw.TutorialEntregable.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
    Esta clase contiene todos los tests de integración
 */

//inicializa el contexto de Spring
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Esta anotacion nos sirve para poder inicializar el contexto para cada test, por si alguno de ellos ha modificado algo anteriormente
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/client";

    @LocalServerPort
    private int port; //inyecta el puerto aleatorio en el que el servidor corre

    @Autowired
    private TestRestTemplate restTemplate; //inyecta herramiento de SpringBoot para realizar solicitudes HTTP y probar el comportamiento de la app

    ParameterizedTypeReference<List<ClientDto>> responseType = new ParameterizedTypeReference<List<ClientDto>>() {
    }; // Se una para especificar el tip de respuesta esperada en las llamadas http, aqui se espera una lista de ClientDto

    @Test
    public void findAllShouldReturnAllClients() {

        /*
        En el contexto de Spring y pruebas, ResponseEntity es una clase que representa
        la respuesta completa de una solicitud HTTP, incluyendo el cuerpo de la respuesta,
        los encabezados HTTP y el código de estado HTTP.

        null -> cuerpo de la solicitud
        responseType-> lista de clientDto
         */
        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(4, response.getBody().size());
    }

    public static final Long NEW_CLIENT_ID = 5L;
    public static final String NEW_CLIENT_NAME = "CLIENT4";
    public static final String EXISTING_CLIENT_NAME = "Lia";

    @Test
    public void saveWithoutIdShouldCreateNewClient() {

        ClientDto dto = new ClientDto();
        dto.setName(NEW_CLIENT_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(5, response.getBody().size());

        //Busca dentro de response para cada elemento coge el primer elemento que cumple la condicion devuelve un "Optional o un optional.empty()"
        ClientDto clientSearch = response.getBody().stream().filter(item -> item.getId().equals(NEW_CLIENT_ID)).findFirst().orElse(null);
        assertNotNull(clientSearch);
        assertEquals(NEW_CLIENT_NAME, clientSearch.getName());
    }

    @Test
    public void saveClientWithExistingNameShouldReturnError() {
        ClientDto dto = new ClientDto();
        dto.setName(EXISTING_CLIENT_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void modifyWithNotExistIdShouldInternalError() {

        ClientDto dto = new ClientDto();
        dto.setName(NEW_CLIENT_NAME);

        //?: tipo generico desconocido
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_CLIENT_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        //Debe dar error porque buscar cambiar un cliente que no existe previamente (recuerda que el contexto se reinicia)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //Pruebas de borrado

    public static final Long DELETE_CLIENT_ID = 4L;
    public static final Long EXISTS_ID_CLIENT = 3L;

    @Test
    public void deleteWithExistsIdAndWithoutPrestamosShouldDeleteClient() {

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_CLIENT_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(3, response.getBody().size());
    }

    @Test
    public void deleteWithExistsIdAndWithPrestamosShouldThrowException() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_ID_CLIENT, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithNotExistsIdShouldInternalError() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_CLIENT_ID, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}