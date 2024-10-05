package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.common.criteria.SearchCriteria;
import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;

public class PrestamoSpecification implements Specification<Prestamo> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public PrestamoSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // Operación de "="
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("between") && criteria.getValue() instanceof List) {
            // Filtrado por rango de fechas
            List<Date> dates = (List<Date>) criteria.getValue();

            return builder.and(builder.greaterThanOrEqualTo(root.get("iniDate"), dates.get(0)), // iniDate debe ser mayor o igual que la fecha de inicio
                    builder.lessThanOrEqualTo(root.get("iniDate"), dates.get(1)) // iniDate debe ser menor o igual que la fecha de fin
            );
        }
        return null;
    }

    //Esta función permmite acceder a campos anidados de las classes
    private Path<String> getPath(Root<Prestamo> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}