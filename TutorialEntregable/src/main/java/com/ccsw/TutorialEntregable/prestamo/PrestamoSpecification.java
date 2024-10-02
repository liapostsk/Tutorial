package com.ccsw.TutorialEntregable.prestamo;

import com.ccsw.TutorialEntregable.common.criteria.SearchCriteria;
import com.ccsw.TutorialEntregable.prestamo.model.Prestamo;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class PrestamoSpecification implements Specification<Prestamo> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public PrestamoSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        //Operación de =
        //Si la operacion es de = i el valor es diferente de null
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("between") && criteria.getValue() instanceof List) {
            // Filtrado por rango de fechas
            List<LocalDate> dates = (List<LocalDate>) criteria.getValue();
            return builder.and(builder.greaterThanOrEqualTo(root.get("iniDate"), dates.get(0)), builder.lessThanOrEqualTo(root.get("endDate"), dates.get(1)));
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