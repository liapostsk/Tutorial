import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Prestamo } from './model/Prestamo';
import { PrestamoPage } from './model/PrestamoPage';

@Injectable({
providedIn: 'root'
})
export class PrestamoService {

    constructor(
        private http: HttpClient
    ) { }
    
    getPrestamos(pageable: Pageable, nameGame?: String, nameClient?: string, iniDate?: string, endDate?: string): Observable<PrestamoPage> {
        // Componer la URL con los filtros
        const url = this.composeFindUrl(nameGame, nameClient, iniDate, endDate);
        
        // solicitud POST con los parámetros de paginación en el body
        return this.http.post<PrestamoPage>(url, { pageable: pageable });
    }
    

    savePrestamo(prestamo: Prestamo): Observable<void> {
        let url = 'http://localhost:8080/prestamo';
        if (prestamo.id != null) url += '/'+prestamo.id;

        return this.http.put<void>(url, prestamo);
    }

    deletePrestamo(idPrestamo : number): Observable<void> {
        return this.http.delete<void>('http://localhost:8080/prestamo/'+idPrestamo);
    }
    
    private composeFindUrl(nameGame?: String, nameClient?: string, iniDate?: string, endDate?: string) : string {
        let params = '';

        if (nameGame != null) {
            params += 'nameGame='+nameGame;
        }

        if (nameClient != null) {
            if (params != '') params += "&";
            params += "nameClient="+nameClient;
        }

        if (iniDate != null) {
            if (params != '') params += "&";
            params += "iniDate="+iniDate;
        }

        if (endDate != null) {
            if (params != '') params += "&";
            params += "endDate="+endDate;
        }

        let url = 'http://localhost:8080/prestamo'

        if (params == '') return url;
        else return url + '?'+params;
    }

}