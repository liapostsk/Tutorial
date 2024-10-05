import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table'; //Fuente de datos para tablas (paginación, ordenación, filtrado)
import { Category } from '../model/Category'; //import de la classe Category (define sus objetos)
import { CategoryService } from '../category.service';
import { MatDialog } from '@angular/material/dialog'; //permite abrir ventanas emergentes
import { CategoryEditComponent } from '../category-edit/category-edit.component'; //Componente que se abre dentro de la ventana de dialogo
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';
import { DialogErrorComponent } from 'src/app/core/dialog-error/dialog-error.component';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.scss']
})
export class CategoryListComponent implements OnInit {

  constructor(
    private categoryService: CategoryService,
    public dialog: MatDialog, //Inyección del servicio que nos deja abrir dialogos modales
  ) { }

  //inicializacion con un arreglo vacio de objetos Category
  dataSource = new MatTableDataSource<Category>(); //Instancia de MatTableDataSource para almacenar los datos de las Categorias
  displayedColumns: string[] = ['id', 'name', 'action']; //Array que define las columnas que se muestran en la tabla (id, name, action)

  //abre un dialogo para crear una nueva categoria
  createCategory() {
    //abre el componente CategoryEditComponent como un dialogo modal
    const dialogRef = this.dialog.open(CategoryEditComponent, {
      //pasando un objeto vacio porque no hay datos previos para editar
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => { //se cierra dialogo
      this.ngOnInit(); //refresca la lista de categorias
    });    
  }

  //abre un dialogo para poder editar una categoria
  editCategory(category: Category) {
    //abre el componente CategoryEdit como un dialogo
    const dialogRef = this.dialog.open(CategoryEditComponent, {
      //el dialogo se abre con los datos de la categoria para que los podamos editar
      data: { category: category }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });
  }

  deleteCategory(category: Category) {    
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: { 
        title: "Eliminar categoría", 
        description: "Atención: si borra la categoría se perderán sus datos.<br> ¿Desea eliminar la categoría?" 
      }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.categoryService.deleteCategory(category.id).subscribe({
          next: () => {
            this.ngOnInit(); // Recargar datos si la eliminación fue exitosa
          },
          error: (error) => {
            this.dialog.open(DialogErrorComponent, {
              data: {
                title: "Error!",
                description: "No se puede eliminar la categoría porque hay datos relacionados con ella."
              }
            });
          }
        });
      }
    });
  }
  

  ngOnInit(): void { //Metodo que se llama automaticamente cuando un componente se inicializa
    //llamada al metodo get categories del servicio
    this.categoryService.getCategories().subscribe( 
      categories => this.dataSource.data = categories //actualiza los datos tabla para mostralos
    );
  }
}