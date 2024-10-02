import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CategoryService } from '../category.service';
import { Category } from '../model/Category';

@Component({
  selector: 'app-category-edit',
  templateUrl: './category-edit.component.html',
  styleUrls: ['./category-edit.component.scss']
})
export class CategoryEditComponent implements OnInit {

  category : Category;

  constructor(
    public dialogRef: MatDialogRef<CategoryEditComponent>, //acceso al dialogo permite cerrarlo o realizar otra op
    @Inject(MAT_DIALOG_DATA) public data: any,
    private categoryService: CategoryService //Se inyecta el servicio para poder usarlo en el componente
  ) { }

  ngOnInit(): void {
    //si hay categoria a editar
    if (this.data.category != null) {
      this.category = this.data.category;
    }
    else {
      //estamos creando una nueva categoria
      this.category = new Category();
    }
  }

  onSave() {
    //Llama al metodo de guardar del servicio con la categoria
    this.categoryService.saveCategory(this.category).subscribe(result => {
      this.dialogRef.close(); //cerramos dialogo
    });    
  }  

  onClose() {
    this.dialogRef.close();
  }

}