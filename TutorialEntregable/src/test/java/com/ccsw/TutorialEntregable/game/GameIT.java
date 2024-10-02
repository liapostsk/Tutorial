package com.ccsw.TutorialEntregable.game;

import com.ccsw.TutorialEntregable.author.model.AuthorDto;
import com.ccsw.TutorialEntregable.category.model.CategoryDto;
import com.ccsw.TutorialEntregable.game.model.GameDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GameIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/game";

    public static final Long EXISTS_GAME_ID = 1L;
    public static final Long NOT_EXISTS_GAME_ID = 0L;
    private static final String NOT_EXISTS_TITLE = "NotExists";
    private static final String EXISTS_TITLE = "Aventureros";
    private static final String NEW_TITLE = "Nuevo juego";
    private static final Long NOT_EXISTS_CATEGORY = 0L;
    private static final Long EXISTS_CATEGORY = 3L;

    private static final String TITLE_PARAM = "title";
    private static final String CATEGORY_ID_PARAM = "idCategory";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<GameDto>> responseType = new ParameterizedTypeReference<List<GameDto>>() {
    };

    //Construye una URL con los parametros de consulta para la solicitud HTTP
    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(TITLE_PARAM, "{" + TITLE_PARAM + "}").queryParam(CATEGORY_ID_PARAM, "{" + CATEGORY_ID_PARAM + "}").encode().toUriString();
    }

    //Buscar un juego sin nigun filtro -> nos ha de retornar todos los juegos de la BD
    @Test
    public void findWithoutFiltersShouldReturnAllGamesInDB() {

        int GAMES_WITH_FILTER = 6; //cantidad esperada de la bd

        Map<String, Object> params = new HashMap<>(); //mapa con los parametros de la consulta como ambos son null no se aplica ningun tipo de filtro
        params.put(TITLE_PARAM, null);
        params.put(CATEGORY_ID_PARAM, null);

        //Se realiza una llamada HTTP, solicitud de tipo GET, en las solicitudes GET no se envía body por lo que NULL, responseType -> lista de GameDto, urlVariables
        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    //Buscar filtrando por un titulo que exista
    @Test
    public void findExistsTitleShouldReturnGames() {

        int GAMES_WITH_FILTER = 1; //tiene que haber una sola respuesta

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, null); //solo aplicamos 1 filtro el de titulo, por eso este es null

        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    //Buscar filtrando por una categoria que exista
    @Test
    public void findExistsCategoryShouldReturnGames() {

        int GAMES_WITH_FILTER = 2;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    //Usando ambos filtros
    @Test
    public void findExistsTitleAndCategoryShouldReturnGames() {

        int GAMES_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    //Filtrado por titulo, pero el titulo no existe en bd
    @Test
    public void findNotExistsTitleShouldReturnEmpty() {

        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        //Porque no es null sino que es una lista de GameDto vacía
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    //Filtrado por categoria, pero esta no existe
    @Test
    public void findNotExistsCategoryShouldReturnEmpty() {

        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    //Filtrado por ambos pero no existe
    @Test
    public void findNotExistsTitleOrCategoryShouldReturnEmpty() {

        int GAMES_WITH_FILTER = 0;

        //Titulo y categoria que no existen
        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());

        //Titulo no existe pero si categoria
        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());

        //Titulo existe pero no categoria
        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    //Creación de un juego
    @Test
    public void saveWithoutIdShouldCreateNewGame() {

        GameDto dto = new GameDto();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);

        dto.setTitle(NEW_TITLE);
        dto.setAge("18");
        dto.setAuthor(authorDto);
        dto.setCategory(categoryDto);

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NEW_TITLE);
        params.put(CATEGORY_ID_PARAM, null);

        //Ver que no exista ya un juego con esas caracteristicas
        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());

        //si no existe, hacemos la solicitud y el programa debe crearlo
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        //otra solicitud para obtenerlo
        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        //si existe hay 1
        assertEquals(1, response.getBody().size());
    }

    //Modificacion de juego existente
    @Test
    public void modifyWithExistIdShouldModifyGame() {

        //Juego a modificar
        GameDto dto = new GameDto();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);

        dto.setTitle(NEW_TITLE);
        dto.setAge("18");
        dto.setAuthor(authorDto);
        dto.setCategory(categoryDto);

        //Mirar que no existe juego con ese titulo ya en la bd
        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NEW_TITLE);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());

        //Se hace la solicitud de modificacion del juego
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_GAME_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        //Se hace otra solicitud para obtener el juego cambiado
        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        //Si la modificacion se ha hecho con existo habrá 1 resultado
        assertEquals(1, response.getBody().size());
        //y su id será igual al existente
        assertEquals(EXISTS_GAME_ID, response.getBody().get(0).getId());
    }

    //Modificas un juego que no existe
    @Test
    public void modifyWithNotExistIdShouldThrowException() {

        GameDto dto = new GameDto(); //lo que hay en el body es un GameDto
        dto.setTitle(NEW_TITLE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_GAME_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
