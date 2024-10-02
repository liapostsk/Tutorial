package com.ccsw.TutorialEntregable.game;

import com.ccsw.TutorialEntregable.game.model.Game;
import com.ccsw.TutorialEntregable.game.model.GameDto;

import java.util.List;

public interface GameService {

    /**
     * Recupera un {@link Game} a través de su ID
     *
     * @param id PK de la entidad
     * @return {@link Game}
     */
    Game get(Long id);

    /**
     * Recupera los juegos filtrando opcionalmente por título y/o categoría
     *
     * @param title título del juego
     * @param idCategory PK de la categoría
     * @return {@link List} de {@link Game}
     */
    List<Game> find(String title, Long idCategory);

    /**
     * Guarda o modifica un juego, dependiendo de si el identificador está o no informado
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, GameDto dto);
}
