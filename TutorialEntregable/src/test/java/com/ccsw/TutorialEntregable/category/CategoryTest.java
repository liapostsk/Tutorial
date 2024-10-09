package com.ccsw.TutorialEntregable.category;

import com.ccsw.TutorialEntregable.category.model.Category;
import com.ccsw.TutorialEntregable.category.model.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
    Esta clase contiene todos los tests unitarios
 */

//Indica que no se ha de inicializar el contexto porque son test estaticos y da igual
@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository; //simula el Repo que tenemos

    @InjectMocks
    private CategoryServiceImpl categoryService; //simula el Service, la interficie

    private static final Long EXISTS_CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "CAT1";

    public static final Long NOT_EXISTS_CATEGORY_ID = 0L;

    @Test
    public void getExistsCategoryIdShouldReturnCategory() {

        /*
        Nos permite simular el comportamiento de los metodos category sin acceder
        a una BD o otros servicios externos
         */
        Category category = mock(Category.class);

        //Configura el comportamiento del mock al aplicarle getId
        when(category.getId()).thenReturn(EXISTS_CATEGORY_ID);
        //Configura el repository para que al aplicarle findById retorne la categoria category
        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(category));

        //Invocacion del metodo get
        Category categoryResponse = categoryService.get(EXISTS_CATEGORY_ID);

        assertNotNull(categoryResponse);
        assertEquals(EXISTS_CATEGORY_ID, category.getId());
    }

    @Test
    public void getNotExistsCategoryIdShouldReturnNull() {

        when(categoryRepository.findById(NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());

        Category category = categoryService.get(NOT_EXISTS_CATEGORY_ID);

        assertNull(category);
    }

    /*
        Resumen
        Configuración del Entorno: @ExtendWith(MockitoExtension.class) y las anotaciones @Mock y @InjectMocks preparan el entorno de prueba y gestionan los mocks.
        Preparación de Datos: Se crea una lista mockeada para simular datos del repositorio.
        Configuración del Comportamiento: Se configura el mock del repositorio para devolver la lista preparada.
        Ejecución y Verificación: Se ejecuta el método bajo prueba y se verifican los resultados para asegurar que el comportamiento del servicio es el esperado.
     */

    @Test
    public void findAllShouldReturnAllCategories() {
        //Creación de datos simulados
        List<Category> list = new ArrayList<>();
        list.add(mock(Category.class)); //esto simula una categoria  que se añade a la lista cuando el repositorio sea llamado

        //Configuración del comportamiento del mock
        when(categoryRepository.findAll()).thenReturn(list); //configura el mock para que cuando se llama al metodo findAll, devuelva la list preparada anteriormente

        List<Category> categories = categoryService.findAll();

        assertNotNull(categories); //verificación de que la lista es not null
        assertEquals(1, categories.size()); //verificación test
    }

    @Test
    public void saveNotExistsCategoryIdShouldInsert() {
        /*
        Se crea una CategoryDto porque es lo que se envia desde fuera hacia dentro,
        de dentro hacia afuera nos devuelve una Entity (Category)
         */
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CATEGORY_NAME);

        /*
            Como buscas guardar una nueva categoria, sabes que al guardarla esto devuelve una entity
            ArgumentCaptor se usa para capturar un argumento de tipo Category. Esto posteriormente
            permitirá inspeccionar el objeto Category para ver si se ha hecho correctamente la insercion de sus valores
            al realizarse el "save"
         */
        ArgumentCaptor<Category> category = ArgumentCaptor.forClass(Category.class);

        //se invoca al servicio interface y se hace el guardado id = null, porque es una nueva categoria
        categoryService.save(null, categoryDto);

        //Se verifica si el metodo save del categoryRepository fue llamado. Además captura la entity categoria
        verify(categoryRepository).save(category.capture());

        //compara que el nombre de la categoria coincida con el de la categoria guardada
        assertEquals(CATEGORY_NAME, category.getValue().getName());
    }

    // Pruebas de borrado
    @Test
    public void deleteExistsCategoryIdShouldDelete() throws Exception {

        Category category = mock(Category.class); //simulación de un entity category

        //Configuración del comportamiento: Cuando lo encuentre lo devuelva, se usa Optional para gestionar en el caso de que no este
        //Evita que si no existe no nos de NullPointerException, si no esta nos devuelve un optional.empty()
        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(category));

        //funcionalidad
        categoryService.delete(EXISTS_CATEGORY_ID);

        //verifica que se ha borrado del "repo mockeado"
        verify(categoryRepository).deleteById(EXISTS_CATEGORY_ID);
    }

}