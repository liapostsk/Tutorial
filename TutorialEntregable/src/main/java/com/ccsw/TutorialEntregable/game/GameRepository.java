package com.ccsw.TutorialEntregable.game;

import com.ccsw.TutorialEntregable.game.model.Game;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ccsw
 *
 */

/*
    Una vez construido el Specification ya podemos usar el método por defecto que nos proporciona Spring Data
    para dicho fin, tan solo tenemos que decirle a nuestro GameRepository que además extender de CrudRepository
    debe extender de JpaSpecificationExecutor, para que pueda ejecutarlas.
 */
public interface GameRepository extends CrudRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    @Override
    @EntityGraph(attributePaths = { "category", "author" })
    List<Game> findAll(Specification<Game> spec);
}