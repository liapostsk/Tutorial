package com.ccsw.TutorialEntregable.category;

import com.ccsw.TutorialEntregable.category.model.Category;
import com.ccsw.TutorialEntregable.category.model.CategoryDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Category get(Long id) {

        return this.categoryRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> findAll() {

        return (List<Category>) this.categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, CategoryDto dto) {

        Category category;

        if (id == null) {
            category = new Category();
        } else {
            //category = this.categoryRepository.findById(id).orElse(null);
            //Ahora se ha añadido el metodo getId en el service por lo que quitamos la linea de arriba para no tener duplicado codigo q hace lo mismo
            category = this.get(id);
        }

        category.setName(dto.getName());

        this.categoryRepository.save(category); //se guarda o se actualiza
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }
        this.categoryRepository.deleteById(id);
    }

}