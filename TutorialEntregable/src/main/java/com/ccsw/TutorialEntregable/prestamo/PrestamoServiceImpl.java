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

import java.util.Date;
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
    public Page<Prestamo> findPageWithFilters(String nameGame, String nameClient, Date iniDate, Date endDate, PrestamoSearchDto dto) {
        // Especificaciones para los criterios de búsqueda, a null por si no tenemos ninguno de ellos
        Specification<Prestamo> spec = Specification.where(null);

        if (nameGame != null) {
            // Se define el criterio de busqueda que se define por SearchCriteria(key, operation, param)
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
    private boolean datesOverlap(Date start1, Date end1, Date start2, Date end2) {
        return !(start1.after(end2) || end1.before(start2));
    }

    public void save(Long id, PrestamoDto data) {
        Prestamo prestamo;
        Date iniDate = data.getIniDate();
        Date endDate = data.getEndDate();

        long daysBetween = (endDate.getTime() - iniDate.getTime()) / (1000 * 60 * 60 * 24); // Calcular días entre fechas

        //  1. iniDate < endDate
        if (endDate.before(iniDate)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        // 2. Dias de prestamo <= 14
        if (daysBetween > 14) {
            throw new IllegalArgumentException("El préstamo no puede superar los 14 días");
        }

        //  3. Dos clientes no pueden tener el mismo juego prestado en el mismo rango de fechas
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
            Date pIniDate = p.getIniDate();
            Date pEndDate = p.getEndDate();
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
            throw new Exception("No existe");
        } else {
            this.prestamoRepository.deleteById(id);
        }
    }
}

