import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';
import { Pageable } from 'src/app/core/model/page/Pageable';
import { PrestamoEditComponent } from '../prestamo-edit/prestamo-edit.component';
import { PrestamoService } from '../prestamo.service';
import { Prestamo } from '../model/Prestamo';
import { Game } from 'src/app/game/model/Game';
import { Client } from 'src/app/client/model/Client';
import { ClientService } from 'src/app/client/client.service';
import { GameService } from 'src/app/game/game.service';

@Component({
selector: 'app-prestamo-list',
templateUrl: './prestamo-list.component.html',
styleUrls: ['./prestamo-list.component.scss']
})
export class PrestamoListComponent implements OnInit {

    //Lista filtrado
    filterClient: Client;
    filterGame: Game;
    filterIniDate: Date;
    filterEndDate: Date;

    //Lista de los desplegables
    prestamos: Prestamo[];
    clients: Client[];
    games: Game[];

    //Listado paginado
    pageNumber: number = 0;
    pageSize: number = 5;
    totalElements: number = 0;

    dataSource = new MatTableDataSource<Prestamo>();
    displayedColumns: string[] = ['id', 'game', 'client', 'iniDate', 'endDate', 'action'];

    constructor(
        private prestamoService: PrestamoService,
        public dialog: MatDialog,
        private gameService: GameService,
        private clientService: ClientService,
    ) { }

    ngOnInit(): void {
        this.loadPage();

        this.gameService.getGames().subscribe(
            games => this.games = games
        );

        this.clientService.getClients().subscribe(
            clients => this.clients = clients
        );
    }

    onCleanFilter(): void {
        this.filterGame = null;
        this.filterClient = null;
        this.filterIniDate = null;
        this.filterEndDate = null;

        this.onSearch();
    }

    onSearch(): void {
       this.loadPage();
    }

    loadPage(event?: PageEvent) {

        let pageable: Pageable = {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            sort: [{
                property: 'id',
                direction: 'ASC'
            }]
        };

        if (event != null) {
            pageable.pageSize = event.pageSize;
            pageable.pageNumber = event.pageIndex;
        }

        let nameGame = this.filterGame != null ? this.filterGame.title : null;
        let nameClient = this.filterClient != null ? this.filterClient.name : null;
        let iniDate = this.filterIniDate ? this.filterIniDate : null;
        let endDate = this.filterEndDate ? this.filterEndDate : null;

        this.prestamoService.getPrestamos(pageable, nameGame, nameClient, iniDate, endDate).subscribe(data => {
            this.dataSource.data = data.content;
            this.pageNumber = data.pageable.pageNumber;
            this.pageSize = data.pageable.pageSize;
            this.totalElements = data.totalElements;
            //prestamos => this.prestamos = prestamos;
        });
    }  

    // NO TOCAR
    createPrestamo() {      
        const dialogRef = this.dialog.open(PrestamoEditComponent, {
            data: {}
        });

        dialogRef.afterClosed().subscribe(result => {
            this.ngOnInit();
        });      
    } 

    deletePrestamo(prestamo: Prestamo) {    
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
            data: { title: "Eliminar prestamo", description: "Atención si borra el prestamo se perderán sus datos.<br> ¿Desea eliminar el prestamo?" }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.prestamoService.deletePrestamo(prestamo.id).subscribe(result =>  {
                    this.ngOnInit();
                }); 
            }
        });
    }  
}