import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Category } from './model/Category';
import { HttpClient } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(
    private http: HttpClient
) { }

  //Metodo que realiza una solicitud HTTP para obtener un listao de categorias desde el backend
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>('http://localhost:8080/category');
  }

  //Mas peticiones, simulamos los 2 botones que hay en pantalla al lado de cada categoria
  saveCategory(category: Category): Observable<Category> {
    let url = 'http://localhost:8080/category';
        if (category.id != null) url += '/'+category.id;

        return this.http.put<Category>(url, category);
  }

  deleteCategory(idCategory : number): Observable<any> {
    return this.http.delete('http://localhost:8080/category/'+idCategory);
  } 
}