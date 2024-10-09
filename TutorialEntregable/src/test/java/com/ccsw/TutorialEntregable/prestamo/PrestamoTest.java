package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.client.ClientService;
import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.client.model.ClientDto;
import com.ccsw.TutorialEntregable.common.pagination.PageableRequest;
import com.ccsw.TutorialEntregable.game.GameService;
import com.ccsw.TutorialEntregable.game.model.Game;
import com.ccsw.TutorialEntregable.game.model.GameDto;
import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoSearchDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrestamoTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private GameService gameService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    private static final Long EXISTING_PRESTAMO_ID = 1L;

    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final String gameName = "On Mars";

    private static final int PAGE_SIZE = 5;

    @Test
    public void findPageWithFiltersShouldReturnFilteredPrestamos() {
        // Creación de datos simulados: mocks
        List<Prestamo> list = new ArrayList<>();
        list.add(mock(Prestamo.class));

        Page<Prestamo> prestamoPage = new PageImpl<>(list);

        // Configuramos el comportamiento del mock
        when(prestamoRepository.findAll(any(), any(Pageable.class))).thenReturn(prestamoPage); // Cuando se llame al repo con findAll, devuela una pagina de prestamos

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        Page<Prestamo> prestamos = prestamoService.findPageWithFilters(gameName, null, null, null, searchDto);

        // Verify results
        assertNotNull(prestamos);
        assertEquals(1, prestamos.getContent().size()); // deberíamos obtener un préstamo
    }

    @Test
    public void saveShouldReturnBadRequestIfEndDateBeforeStartDate() throws ParseException {
        Date iniDate = format.parse("2024-09-20");
        Date endDate = format.parse("2024-09-16");

        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setIniDate(iniDate);  // Colocamos la fecha de inicio antes de la fecha de fin
        prestamoDto.setEndDate(endDate);

        ResponseEntity<?> response = prestamoService.save(null, prestamoDto);

        // Verificar que el código de estado sea 400 (BAD REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verificar que el mensaje de error sea el correcto
        assertNotNull(response.getBody());
        assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio.", ((java.util.Map) response.getBody()).get("error"));
    }

    @Test
    public void saveShouldReturnBadRequestIfLoanExceeds14Days() throws ParseException {
        Date iniDate = format.parse("2024-09-10");
        Date endDate = format.parse("2024-10-26");

        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setIniDate(iniDate);
        prestamoDto.setEndDate(endDate);

        ResponseEntity<?> response = prestamoService.save(null, prestamoDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El préstamo no puede superar los 14 días.", ((java.util.Map) response.getBody()).get("error"));
    }

    @Test
    public void saveShouldInsertPrestamoSuccessfully() throws ParseException {
        // Configuración de las fechas para el préstamo
        Date iniDate = format.parse("2024-09-20");
        Date endDate = format.parse("2024-09-25");

        // Crear un DTO para el préstamo
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setIniDate(iniDate);
        prestamoDto.setEndDate(endDate);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        prestamoDto.setGame(gameDto);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        prestamoDto.setClient(clientDto);

        Game game = mock(Game.class);
        Client client = mock(Client.class);
        //Comportamiento del mock al aplicarle getId
        when(game.getId()).thenReturn(1L);
        when(client.getId()).thenReturn(1L);

        // Mockear los servicios
        when(gameService.get(1L)).thenReturn(game);
        when(clientService.get(1L)).thenReturn(client);

        // Capturador para verificar que el préstamo se guarda correctamente
        ArgumentCaptor<Prestamo> prestamoCaptor = ArgumentCaptor.forClass(Prestamo.class);

        // Ejecutamos metodo save
        ResponseEntity<?> response = prestamoService.save(null, prestamoDto);

        // Verificar que el repositorio ha llamado al método save y captura el prestamo
        verify(prestamoRepository).save(prestamoCaptor.capture());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar que el préstamo se haya guardado correctamente
        Prestamo savedPrestamo = prestamoCaptor.getValue();
        assertNotNull(savedPrestamo);
        assertEquals(iniDate, savedPrestamo.getIniDate());
        assertEquals(endDate, savedPrestamo.getEndDate());
        assertEquals(1L, savedPrestamo.getGame().getId());
        assertEquals(1L, savedPrestamo.getClient().getId());

        // Verificar que el mensaje de éxito sea el correcto
        assertNotNull(response.getBody());
        assertEquals("Préstamo guardado exitosamente.", ((java.util.Map) response.getBody()).get("message"));
    }

    @Test
    public void deleteShouldDeletePrestamoIfExists() throws Exception {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(EXISTING_PRESTAMO_ID);

        when(prestamoRepository.findById(EXISTING_PRESTAMO_ID)).thenReturn(Optional.of(prestamo));

        prestamoService.delete(EXISTING_PRESTAMO_ID);

        verify(prestamoRepository).deleteById(EXISTING_PRESTAMO_ID);
    }
}
