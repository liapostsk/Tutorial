package com.ccsw.TutorialEntregable.author.model;

import com.ccsw.TutorialEntregable.common.pagination.PageableRequest;

/**
 * @author ccsw
 * Sirve para encapsular la información necesaria
 * para la busqueda paginada de autores
 */
public class AuthorSearchDto {

    private PageableRequest pageable; //contiene info de la paginacion y el ordenamiento de los resultados

    //acceso al objeto PageableRequest
    public PageableRequest getPageable() {
        return pageable;
    }

    //Modificacion al PageableRequest
    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}