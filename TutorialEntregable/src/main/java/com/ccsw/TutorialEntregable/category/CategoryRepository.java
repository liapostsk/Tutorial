package com.ccsw.TutorialEntregable.category;

import com.ccsw.TutorialEntregable.category.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccsw
 * Nos devuelve objetos de tipo "Category"
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {

}