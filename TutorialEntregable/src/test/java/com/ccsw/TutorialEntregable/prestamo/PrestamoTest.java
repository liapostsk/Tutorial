package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.common.pagination.PageableRequest;
import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoSearchDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        Prestamo prestamo = new Prestamo();  // Se puede crear una instancia real o mockearla
        list.add(prestamo);

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
    public void deleteShouldDeletePrestamoIfExists() throws Exception {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(EXISTING_PRESTAMO_ID);

        when(prestamoRepository.findById(EXISTING_PRESTAMO_ID)).thenReturn(Optional.of(prestamo));

        prestamoService.delete(EXISTING_PRESTAMO_ID);

        verify(prestamoRepository).deleteById(EXISTING_PRESTAMO_ID);
    }
}
