package com.ccsw.TutorialEntregable.prestamo.model;

import com.ccsw.TutorialEntregable.common.pagination.PageableRequest;

/**
 * @author ccsw
 *
 */
public class PrestamoSearchDto {

    private PageableRequest pageable;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}