import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PrestamoService } from '../prestamo.service';
import { Prestamo } from '../model/Prestamo';
import { Game } from 'src/app/game/model/Game';
import { GameService } from 'src/app/game/game.service';
import { Client } from 'src/app/client/model/Client';
import { ClientService } from 'src/app/client/client.service';

@Component({
selector: 'app-prestamo-edit',
templateUrl: './prestamo-edit.component.html',
styleUrls: ['./prestamo-edit.component.scss']
})
export class PrestamoEditComponent implements OnInit {

    prestamo : Prestamo;
    games: Game[];
    clients: Client[];

    constructor(
        public dialogRef: MatDialogRef<PrestamoEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private prestamoService: PrestamoService,
        private gameService: GameService,
        private clientService: ClientService
    ) { }

    ngOnInit(): void {
        if (this.data.prestamo != null) {
            this.prestamo = Object.assign({}, this.data.prestamo);
        }
        else {
            this.prestamo = new Prestamo();
        }

        this.gameService.getGames().subscribe(
            games => this.games = games
        );

        this.clientService.getClients().subscribe(
            clients => this.clients = clients
        );
    }

    onSave() {
        this.prestamoService.savePrestamo(this.prestamo).subscribe(result =>  {
            this.dialogRef.close();
        }); 
    }  

    onClose() {
        this.dialogRef.close();
    }

}