package com.ccsw.TutorialEntregable.game;

import com.ccsw.TutorialEntregable.author.AuthorService;
import com.ccsw.TutorialEntregable.category.CategoryService;
import com.ccsw.TutorialEntregable.common.criteria.SearchCriteria;
import com.ccsw.TutorialEntregable.game.model.Game;
import com.ccsw.TutorialEntregable.game.model.GameDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    CategoryService categoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Game get(Long id) {

        return this.gameRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> find(String title, Long idCategory) {

        GameSpecification titleSpec = new GameSpecification(new SearchCriteria("title", ":", title)); //especificacion del titulo
        GameSpecification categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory)); //especificacion del autor

        // Combinamos las especificaciones: buscamos juegos que cumplan ambas condiciones
        Specification<Game> spec = Specification.where(titleSpec).and(categorySpec);

        // Utilizamos el repositorio para buscar todos los juegos que coincidan con las especificaciones
        return this.gameRepository.findAll(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, GameDto dto) {
        // GameDto -> internamente dispone de AuthroDto y CategoryDto
        Game game;

        if (id == null) {
            game = new Game();
        } else {
            game = this.get(id);
        }

        //copia las propiedades de dto a game, ignorando id, author y category porque de estas ya tenemos el id del juego
        // y en el caso de author y category usamos sus id, es por ello que hemos de cogerlos de los servicios correspondientes
        BeanUtils.copyProperties(dto, game, "id", "author", "category");

        game.setAuthor(authorService.get(dto.getAuthor().getId()));
        game.setCategory(categoryService.get(dto.getCategory().getId()));

        this.gameRepository.save(game);
    }

}