import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CategoryListComponent } from './category/category-list/category-list.component';
import { ClientListComponent } from './client/client-list/client-list.component';
import { AuthorListComponent } from './author/author-list/author-list.component';
import { PrestamoListComponent } from './prestamo/prestamo-list/prestamo-list.component';
import { GameListComponent } from './game/game-list/game-list.component';

const routes: Routes = [
  { path: '', redirectTo: '/games', pathMatch: 'full'}, //Si no se especifica nada, la ruta "default" es ir a games
  { path: 'categories', component: CategoryListComponent },
  { path: 'clientes', component: ClientListComponent },
  { path: 'authors', component: AuthorListComponent },
  { path: 'prestamos', component: PrestamoListComponent},
  { path: 'games', component: GameListComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }