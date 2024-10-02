package com.ccsw.TutorialEntregable.common.criteria;

public class SearchCriteria {

    private String key; //campo (parametro)
    private String operation; //operacion
    private Object value; //valor (del parametro)

    /*
    Ejemplo: select * fromTable where name = 'búsqueda'
    name = key,
    = = operation
    busqueda = value
     */

    public SearchCriteria(String key, String operation, Object value) {

        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}