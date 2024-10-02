package com.ccsw.TutorialEntregable.game;

import com.ccsw.TutorialEntregable.common.criteria.SearchCriteria;
import com.ccsw.TutorialEntregable.game.model.Game;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

//Contiene la construccion de la consulta en función de los criterios proporcionados
//Hay una especificacion para cada entity a consultar, en nuestro caso solo es con Game
public class GameSpecification implements Specification<Game> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public GameSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    //Construye una expresión Predicate que representa una condición en la consulta.
    @Override
    public Predicate toPredicate(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) { //":" es el tipo de comparación si es =, no se usa"=" pq el simbolo esta reservado para otros usos
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) { //si el parametro es de tipo string se usa el builder.like
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else { //sino se usa el .equal
                return builder.equal(path, criteria.getValue());
            }
        }
        return null;
    }

    // Obtiene el camino (path) para la propiedad especificada en criteria.key
    private Path<String> getPath(Root<Game> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}