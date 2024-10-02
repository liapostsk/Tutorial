import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from './core/core.module';
import { CategoryModule } from './category/category.module';
import { HttpClientModule } from '@angular/common/http';
import { ClientModule } from './client/client.module';
import { AuthorModule } from './author/author.module';
import { PrestamoModule } from './prestamo/prestamo.module';
import { GameModule } from './game/game.module';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatIconModule,
    CoreModule,
    CategoryModule,
    HttpClientModule,
    ClientModule,
    AuthorModule,
    PrestamoModule,
    GameModule,
    BrowserAnimationsModule
  ],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'es-ES' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
