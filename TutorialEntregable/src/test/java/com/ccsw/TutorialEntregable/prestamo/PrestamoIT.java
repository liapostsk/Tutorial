package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.client.model.ClientDto;
import com.ccsw.TutorialEntregable.common.pagination.PageableRequest;
import com.ccsw.TutorialEntregable.config.ResponsePage;
import com.ccsw.TutorialEntregable.game.model.GameDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoSearchDto;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PrestamoIT {
    //URL para las peticiones HTTP
    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/prestamo";

    //Variables usadas para las pruebas
    public static final Long DELETE_PRESTAMO_ID = 6L;
    public static final Long MODIFY_PRESTAMO_ID = 3L;
    LocalDate NEW_INIDATE = LocalDate.of(2024, 9, 1); //2024/09/01
    LocalDate NEW_ENDDATE = LocalDate.of(2024, 9, 10);
    private static final int TOTAL_PRESTAMOS = 6;
    private static final int PAGE_SIZE = 5;

    //Listado Filtrado, parametros
    private static final String NOT_EXISTING_GAME = "NO EXISTO";
    private static final String NOT_EXISTING_CLIENT = "NO EXISTO";

    private static final String EXISTS_GAME = "On Mars";
    private static final String EXISTS_CLIENT = "Lia";

    private static final String GAME_PARAM = "nameGame";
    private static final String CLIENT_PARAM = "nameClient";
    private static final String INI_DATE = "iniDate";
    private static final String END_DATE = "endDate";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; //para llamadas HTTP a la API

    ParameterizedTypeReference<List<PrestamoDto>> responseType = new ParameterizedTypeReference<List<PrestamoDto>>() {
    };

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_PARAM, "{" + GAME_PARAM + "}").queryParam(CLIENT_PARAM, "{" + CLIENT_PARAM + "}").queryParam(INI_DATE, "{" + INI_DATE + "}")
                .queryParam(END_DATE, "{" + END_DATE + "}").encode().toUriString();
    }

    @Test
    public void findWithoutFiltersShouldReturnAllPrestamosInDB() {

        int PRESTAMOS_WITH_FILTER = 6;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsGameNameShouldReturnGames() {

        int GAMES_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, EXISTS_GAME);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsCategoryShouldReturnGames() {

        int PRESTAMOS_WITH_FILTER = 2;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, EXISTS_CLIENT);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findByDateRangeShouldReturnFilteredPrestamos() {
        int EXPECTED_COUNT = 3;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, LocalDate.of(2024, 9, 1));
        params.put(END_DATE, LocalDate.of(2024, 9, 5));

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().size());
    }

    @Test
    public void findExistsGameAndClientShouldReturnGames() {

        int PRESTAMOS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, EXISTS_GAME);
        params.put(CLIENT_PARAM, EXISTS_CLIENT);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findByNonExistingGameNameShouldReturnEmpty() {
        int EXPECTED_COUNT = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, NOT_EXISTING_GAME);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().size());
    }

    @Test
    public void findByNonExistingClientNameShouldReturnEmpty() {
        int EXPECTED_COUNT = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, NOT_EXISTING_CLIENT);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().size());
    }

    @Test
    public void findByNotExistingDateRangeShouldReturnEmpty() {
        int EXPECTED_COUNT = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, LocalDate.of(2023, 8, 1));
        params.put(END_DATE, LocalDate.of(2023, 8, 5));

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().size());
    }

    //---------------------------------------------------------
    //-----------------PRESTAMO---PAGINADO---------------------
    //---------------------------------------------------------

    //Para el manejo de tipos genericos en las respuestas API
    //En este caso el tipo generico es ResponsePage<PrestamoDto>, la solicitud HTTP espera una pagina de resultados que contiene una lista de objetos PrestamoDto
    //Respuesta en pages con la infor de prestamoDto
    ParameterizedTypeReference<ResponsePage<PrestamoDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<PrestamoDto>>() {
    };

    // Test relacionados con el paginado

    /*
        Verifica que la primera pagina de resultados
        nos devuelva los primeros cinco autores
     */
    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        //configuracion de la paginacion usando PageableRequest, 0 primera magina y el nombre de autores por pagina
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        //Llamada a la API: se dice la URL, la entidad que tiene el cuerpo (searchDto) y el tipo de respuesta esperado
        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_PRESTAMOS - PAGE_SIZE; //la info restante

        //pone valor al elemento pageable que tiene searchDto asi cuando se lanza la peticion http, tendremos la page solicitada (1, page_size)
        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        //get... vienen de la classe Page
        assertEquals(TOTAL_PRESTAMOS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewPrestamo() {
        long newPrestamoId = TOTAL_PRESTAMOS + 1;

        PrestamoDto dto = new PrestamoDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setIniDate(LocalDate.of(2024, 9, 16));
        dto.setEndDate(LocalDate.of(2024, 9, 20));

        //Realizar llamada PUT para guardar el prestamo
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        //Verificamos que el prestamo se ha añadido correctamente: Pagina 0, +1 prestamo
        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newPrestamoId));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS + 1, response.getBody().getTotalElements());

        PrestamoDto prestamo = response.getBody().getContent().stream().filter(item -> item.getId().equals(newPrestamoId)).findFirst().orElse(null);
        assertNotNull(prestamo);
        assertEquals(gameDto.getId(), prestamo.getGame().getId());
        assertEquals(clientDto.getId(), prestamo.getClient().getId());
    }

    @Test
    public void saveWithEndDateBeforeStartDateShouldThrowException() {
        PrestamoDto dto = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        //Fecha de fin anterior a la de inicio
        dto.setIniDate(NEW_ENDDATE);
        dto.setEndDate(NEW_INIDATE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void saveWithMoreThan14DaysShouldThrowException() {
        PrestamoDto dto = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        //prestamo de más de 14 días
        dto.setIniDate(LocalDate.of(2024, 9, 1));   // 01/09/2024
        dto.setEndDate(LocalDate.of(2024, 9, 20));  // 20/09/2024 (más de 14 días)

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void saveWithOverlappingGameForDifferentClientShouldThrowException() {
        PrestamoDto dto1 = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto1 = new ClientDto();
        clientDto1.setId(1L);

        dto1.setGame(gameDto);
        dto1.setClient(clientDto1);
        dto1.setIniDate(LocalDate.of(2024, 9, 1));  // 01/09/2024
        dto1.setEndDate(LocalDate.of(2024, 9, 5));  // 05/09/2024

        //Guardado del primer prestamo
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto1), Void.class);

        PrestamoDto dto2 = new PrestamoDto();
        ClientDto clientDto2 = new ClientDto();
        clientDto2.setId(2L);

        dto2.setGame(gameDto);
        dto2.setClient(clientDto2);
        dto2.setIniDate(LocalDate.of(2024, 9, 3));  // 03/09/2024 (Fecha solapada con dto1)
        dto2.setEndDate(LocalDate.of(2024, 9, 8));

        // Intentamos guardar el segundo que solapa
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto2), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void saveWithMoreThanTwoGamesForSameClientOnSameDayShouldThrowException() {
        ClientDto clientDto = new ClientDto(); //cliente de los prestamos
        clientDto.setId(1L);

        //Primer prestamo
        PrestamoDto dto1 = new PrestamoDto();
        GameDto gameDto1 = new GameDto();
        gameDto1.setId(1L);

        dto1.setGame(gameDto1);
        dto1.setClient(clientDto);
        dto1.setIniDate(LocalDate.of(2024, 9, 1));  // 01/09/2024
        dto1.setEndDate(LocalDate.of(2024, 9, 5));  // 05/09/2024

        PrestamoDto dto2 = new PrestamoDto();
        GameDto gameDto2 = new GameDto();
        gameDto2.setId(2L);
        dto2.setGame(gameDto2);
        dto2.setClient(clientDto);  // Mismo cliente
        dto2.setIniDate(LocalDate.of(2024, 9, 1));  // 01/09/2024
        dto2.setEndDate(LocalDate.of(2024, 9, 5));

        PrestamoDto dto3 = new PrestamoDto();
        GameDto gameDto3 = new GameDto();
        gameDto3.setId(3L);
        dto3.setGame(gameDto3);
        dto3.setClient(clientDto);  //Mismo cliente
        dto3.setIniDate(LocalDate.of(2024, 9, 3));  // 03/09/2024 (Fecha solapada)
        dto3.setEndDate(LocalDate.of(2024, 9, 8));  // 08/09/2024

        // Guardamos los dos primeros préstamos
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto1), Void.class);
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto2), Void.class);

        // Intentamos guardar el tercero que supera el límite de 2 juegos
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto3), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void modifyWithExistIdShouldModifyPrestamo() {
        //En este test modificamos el prestamo numero 3
        PrestamoDto dto = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setIniDate(LocalDate.of(2024, 9, 16));
        dto.setEndDate(LocalDate.of(2024, 9, 20));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_PRESTAMO_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        //Vemos que el prestamo se ha modificado correctamente
        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        //como solo hemos modificado el numero de prestamos ha de ser el mismo
        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS, response.getBody().getTotalElements());

        PrestamoDto prestamo = response.getBody().getContent().stream().filter(item -> item.getId().equals(MODIFY_PRESTAMO_ID)).findFirst().orElse(null);
        assertNotNull(prestamo);
        assertEquals(gameDto.getId(), prestamo.getGame().getId());
        assertEquals(clientDto.getId(), prestamo.getClient().getId());
    }

    @Test
    public void modifyWithNotExistIdShouldThrowException() {

        long prestamoIdNoexists = TOTAL_PRESTAMOS + 1;

        PrestamoDto dto = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setIniDate(NEW_INIDATE);
        dto.setEndDate(NEW_ENDDATE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + prestamoIdNoexists, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteCategory() {

        long newPrestamosSize = TOTAL_PRESTAMOS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_PRESTAMO_ID, HttpMethod.DELETE, null, Void.class);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_PRESTAMOS));

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newPrestamosSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deletePrestamoId = TOTAL_PRESTAMOS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deletePrestamoId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
