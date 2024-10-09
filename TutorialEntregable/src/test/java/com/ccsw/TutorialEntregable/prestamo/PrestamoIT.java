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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    //Listado paginado
    private static final int TOTAL_PRESTAMOS = 6;
    private static final int PAGE_SIZE = 5;

    //Listado Filtrado, parametros
    private static final String NOT_EXISTING_GAME = "NO EXISTO";
    private static final String NOT_EXISTING_CLIENT = "NO EXISTO";
    private static final String NOT_EXISTING_INIDATE = "2023-09-01";
    private static final String NOT_EXISTING_ENDDATE = "2023-09-10";

    private static final String EXISTING_INIDATE = "2024-09-01";
    private static final String EXISTING_ENDDATE = "2024-09-05";
    private static final String EXISTS_GAME = "On Mars";
    private static final String EXISTS_CLIENT = "Lia";
    private static final String EXISTIS_P_CLIENT = "Sandra";

    private static final String GAME_PARAM = "nameGame";
    private static final String CLIENT_PARAM = "nameClient";
    private static final String INI_DATE = "iniDate";
    private static final String END_DATE = "endDate";

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; //para llamadas HTTP a la API

    //Para el manejo de tipos genericos en las respuestas API
    //En este caso el tipo generico es ResponsePage<PrestamoDto>, la solicitud HTTP espera una pagina de resultados que contiene una lista de objetos PrestamoDto
    //Respuesta en pages con la infor de prestamoDto
    ParameterizedTypeReference<ResponsePage<PrestamoDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<PrestamoDto>>() {
    };

    public PrestamoIT() throws ParseException {
    }

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_PARAM, "{" + GAME_PARAM + "}").queryParam(CLIENT_PARAM, "{" + CLIENT_PARAM + "}").queryParam(INI_DATE, "{" + INI_DATE + "}")
                .queryParam(END_DATE, "{" + END_DATE + "}").encode().toUriString();
    }

    @Test
    public void findWithoutFiltersShouldReturnAllPrestamosInPage() {

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findExistsGameNameShouldReturnGamesInPage() {

        int GAMES_WITH_FILTER = 1;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, EXISTS_GAME);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);
        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().getContent().size());
    }

    @Test
    public void findExistsCategoryShouldReturnGamesInPage() {

        int PRESTAMOS_WITH_FILTER = 2;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, EXISTS_CLIENT);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getContent().size());
    }

    @Test
    public void findByDateRangeShouldReturnFilteredPrestamosInPage() throws ParseException {
        int EXPECTED_COUNT = 3;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, EXISTING_INIDATE);
        params.put(END_DATE, EXISTING_ENDDATE);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().getContent().size());
    }

    @Test
    public void findExistsGameAndClientShouldReturnGames() {
        int PRESTAMOS_WITH_FILTER = 1;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, EXISTS_GAME);
        params.put(CLIENT_PARAM, EXISTIS_P_CLIENT);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(PRESTAMOS_WITH_FILTER, response.getBody().getContent().size());
    }

    @Test
    public void findByNonExistingGameNameShouldReturnEmpty() {
        int EXPECTED_COUNT = 0;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, NOT_EXISTING_GAME);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().getContent().size());
    }

    @Test
    public void findByNonExistingClientNameShouldReturnEmpty() {
        int EXPECTED_COUNT = 0;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, NOT_EXISTING_CLIENT);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().getContent().size());
    }

    @Test
    public void findByNotExistingDateRangeShouldReturnEmpty() {
        int EXPECTED_COUNT = 0;

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, NOT_EXISTING_INIDATE);
        params.put(END_DATE, NOT_EXISTING_ENDDATE);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(EXPECTED_COUNT, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_PRESTAMOS - PAGE_SIZE; //la info restante

        //pone valor al elemento pageable que tiene searchDto asi cuando se lanza la peticion http, tendremos la page solicitada (1, page_size)
        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);

        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewPrestamo() throws ParseException {
        long newPrestamoId = TOTAL_PRESTAMOS + 1;

        PrestamoDto dto = new PrestamoDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);

        Date iniDate = format.parse("2024-09-16");
        Date endDate = format.parse("2024-09-20");
        dto.setIniDate(iniDate);
        dto.setEndDate(endDate);

        //Realizar llamada PUT para guardar el prestamo
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        //Verificamos que el prestamo se ha añadido correctamente: Pagina 0, +1 prestamo
        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newPrestamoId));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_PARAM, null);
        params.put(CLIENT_PARAM, null);
        params.put(INI_DATE, null);
        params.put(END_DATE, null);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(TOTAL_PRESTAMOS + 1, response.getBody().getTotalElements());

        PrestamoDto prestamo = response.getBody().getContent().stream().filter(item -> item.getId().equals(newPrestamoId)).findFirst().orElse(null);
        assertNotNull(prestamo);
        assertEquals(gameDto.getId(), prestamo.getGame().getId());
        assertEquals(clientDto.getId(), prestamo.getClient().getId());
    }

    @Test
    public void saveWithEndDateBeforeStartDateShouldThrowException() throws ParseException {
        PrestamoDto dto = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        Date iniDate = format.parse(EXISTING_ENDDATE);
        Date endDate = format.parse(EXISTING_INIDATE);
        dto.setIniDate(iniDate);
        dto.setEndDate(endDate);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveWithMoreThan14DaysShouldThrowException() throws ParseException {
        PrestamoDto dto = new PrestamoDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        dto.setGame(gameDto);
        dto.setClient(clientDto);
        //prestamo de más de 14 días
        Date iniDate = format.parse("2024-11-1");
        Date endDate = format.parse("2024-11-20");
        dto.setIniDate(iniDate);
        dto.setEndDate(endDate);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveWithOverlappingGameForDifferentClientShouldThrowException() throws ParseException {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        PrestamoDto dto1 = new PrestamoDto();

        ClientDto clientDto1 = new ClientDto();
        clientDto1.setId(1L);

        dto1.setGame(gameDto);
        dto1.setClient(clientDto1);
        Date iniDate = format.parse("2024-09-01");
        Date endDate = format.parse("2024-09-05");
        dto1.setIniDate(iniDate);
        dto1.setEndDate(endDate);

        //Guardado del primer prestamo
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto1), Void.class);

        PrestamoDto dto2 = new PrestamoDto();

        ClientDto clientDto2 = new ClientDto();
        clientDto2.setId(2L);

        dto2.setGame(gameDto);
        dto2.setClient(clientDto2);
        Date iniDate2 = format.parse("2024-09-03"); // 03/09/2024 (Fecha solapada con dto1)
        Date endDate2 = format.parse("2025-09-08");
        dto2.setIniDate(iniDate2);
        dto2.setEndDate(endDate2);

        // Intentamos guardar el segundo que solapa
        ResponseEntity<?> response2 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto2), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    public void saveWithMoreThanTwoGamesForSameClientOnSameDayShouldThrowException() throws ParseException {
        ClientDto clientDto = new ClientDto(); //cliente de los prestamos
        clientDto.setId(1L);

        //Primer prestamo
        PrestamoDto dto1 = new PrestamoDto();
        GameDto gameDto1 = new GameDto();
        gameDto1.setId(1L);

        dto1.setGame(gameDto1);
        dto1.setClient(clientDto);
        Date iniDate1 = format.parse("2024-10-01");
        Date endDate1 = format.parse("2024-10-05");
        dto1.setIniDate(iniDate1);
        dto1.setEndDate(endDate1);

        PrestamoDto dto2 = new PrestamoDto();
        GameDto gameDto2 = new GameDto();
        gameDto2.setId(2L);
        dto2.setGame(gameDto2);
        dto2.setClient(clientDto);  // Mismo cliente
        Date iniDate2 = format.parse("2024-10-01");
        Date endDate2 = format.parse("2024-10-05");
        dto2.setIniDate(iniDate2);
        dto2.setEndDate(endDate2);

        PrestamoDto dto3 = new PrestamoDto();
        GameDto gameDto3 = new GameDto();
        gameDto3.setId(3L);
        dto3.setGame(gameDto3);
        dto3.setClient(clientDto);  //Mismo cliente
        Date iniDate3 = format.parse("2024-10-03"); // 03/09/2024 (Fecha solapada)
        Date endDate3 = format.parse("2024-10-08");
        dto3.setIniDate(iniDate3);
        dto3.setEndDate(endDate3);

        // Guardamos los dos primeros préstamos
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto1), Void.class);
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto2), Void.class);

        // Intentamos guardar el tercero que supera el límite de 2 juegos
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto3), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
