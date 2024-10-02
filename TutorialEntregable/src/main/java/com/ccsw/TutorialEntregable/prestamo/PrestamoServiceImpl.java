package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.client.ClientService;
import com.ccsw.TutorialEntregable.common.criteria.SearchCriteria;
import com.ccsw.TutorialEntregable.game.GameService;
import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoDto;
import com.ccsw.TutorialEntregable.prestamo.model.PrestamoSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto) {
        return this.prestamoRepository.findAll(dto.getPageable().getPageable());
    }

    //LISTA FILTRADA

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Prestamo> find(String nameGame, String nameClient, LocalDate iniDate, LocalDate endDate) {
        //Especificaciones para los criterios de búsqueda
        Specification<Prestamo> spec = Specification.where(null);

        if (nameGame != null) {
            PrestamoSpecification gameSpec = new PrestamoSpecification(new SearchCriteria("game.title", ":", nameGame));
            spec = spec.and(gameSpec);
        }

        if (nameClient != null) {
            PrestamoSpecification clientSpec = new PrestamoSpecification(new SearchCriteria("client.name", ":", nameClient));
            spec = spec.and(clientSpec);
        }

        if (iniDate != null && endDate != null) {
            PrestamoSpecification dateRangeSpec = new PrestamoSpecification(new SearchCriteria("iniDate", "between", List.of(iniDate, endDate)));
            spec = spec.and(dateRangeSpec);
        }

        return this.prestamoRepository.findAll(spec);
    }

    @Override
    public Page<Prestamo> findPageWithFilters(String nameGame, String nameClient, LocalDate iniDate, LocalDate endDate, PrestamoSearchDto dto) {
        // Especificaciones para los criterios de búsqueda, a null por si no tenemos ninguno de ellos
        Specification<Prestamo> spec = Specification.where(null);

        if (nameGame != null) {
            //Se define el criterio de busqueda que se define por SearchCriterai(key, operation, param)
            PrestamoSpecification gameSpec = new PrestamoSpecification(new SearchCriteria("game.title", ":", nameGame));
            spec = spec.and(gameSpec);
        }

        if (nameClient != null) {
            PrestamoSpecification clientSpec = new PrestamoSpecification(new SearchCriteria("client.name", ":", nameClient));
            spec = spec.and(clientSpec);
        }

        if (iniDate != null && endDate != null) {
            PrestamoSpecification dateRangeSpec = new PrestamoSpecification(new SearchCriteria("iniDate", "between", List.of(iniDate, endDate)));
            spec = spec.and(dateRangeSpec);
        }

        // Llamada al repositorio con las especificaciones y paginación
        return this.prestamoRepository.findAll(spec, dto.getPageable().getPageable());
    }

    // Método para verificar si dos rangos de fechas se solapan
    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    public void save(Long id, PrestamoDto data) {
        Prestamo prestamo;
        LocalDate iniDate = data.getIniDate();
        LocalDate endDate = data.getEndDate();

        long daysBetween = ChronoUnit.DAYS.between(iniDate, endDate);

        //  1. iniDate < endDate
        if (endDate.isBefore(iniDate)) {
            throw new IllegalArgumentException("La data de fin no puede ser anterior a la fecha de inicio");
        }

        // 2. Dias de prestamo <= 14
        if (daysBetween > 14) {
            throw new IllegalArgumentException("El prestamo no puede superar los 14 dias");
        }

        //  3. Dos clientes no pueden tener el mismo juego prestado, en = rango de fechas
        // Recuperar todos los préstamos para las validaciones
        Iterable<Prestamo> allPrestamos = prestamoRepository.findAll();

        for (Prestamo p : allPrestamos) {
            if (p.getGame().getId().equals(data.getGame().getId()) && datesOverlap(p.getIniDate(), p.getEndDate(), iniDate, endDate)) {
                throw new IllegalArgumentException("El juego ya está prestado en ese rango de fechas a otro cliente.");
            }
        }

        // 4. Validar que el mismo cliente no tenga más de 2 juegos prestados en el mismo rango de fechas
        int prestamosDelCliente = 0;
        for (Prestamo p : allPrestamos) {
            LocalDate pIniDate = p.getIniDate();
            LocalDate pEndDate = p.getEndDate();
            if (p.getClient().getId().equals(data.getClient().getId()) && datesOverlap(pIniDate, pEndDate, iniDate, endDate)) {
                prestamosDelCliente++;
                if (prestamosDelCliente >= 2) {
                    throw new IllegalArgumentException("El cliente ya tiene prestados 2 juegos en ese rango de fechas.");
                }
            }
        }

        if (id == null) {
            prestamo = new Prestamo();
        } else {
            prestamo = this.prestamoRepository.findById(id).orElse(null);
        }

        BeanUtils.copyProperties(data, prestamo, "id", "game", "client");
        prestamo.setGame(gameService.get(data.getGame().getId()));
        prestamo.setClient(clientService.get(data.getClient().getId()));
        this.prestamoRepository.save(prestamo);
    }

    public void delete(Long id) throws Exception {
        if (this.prestamoRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        } else
            this.prestamoRepository.deleteById(id);
    }
}
