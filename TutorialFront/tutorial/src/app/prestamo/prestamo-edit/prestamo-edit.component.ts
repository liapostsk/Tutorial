import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PrestamoService } from '../prestamo.service';
import { Prestamo } from '../model/Prestamo';
import { Game } from 'src/app/game/model/Game';
import { GameService } from 'src/app/game/game.service';
import { Client } from 'src/app/client/model/Client';
import { ClientService } from 'src/app/client/client.service';
import { MatDialog } from '@angular/material/dialog';
import { DialogErrorComponent } from 'src/app/core/dialog-error/dialog-error.component';

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
        public dialog: MatDialog,
        private prestamoService: PrestamoService,
        private gameService: GameService,
        private clientService: ClientService
    ) { }

    ngOnInit(): void {

        if (this.data.prestamo != null) {
            this.prestamo = Object.assign({}, this.data.prestamo);
        } else {
            this.prestamo = new Prestamo();
            this.prestamo.iniDate = new Date(); // O establece una fecha predeterminada
            this.prestamo.endDate = new Date(); // O establece una fecha predeterminada
        }

        this.gameService.getGames().subscribe(
            games => this.games = games
        );

        this.clientService.getClients().subscribe(
            clients => this.clients = clients
        );
    }

    onSave() {
        // Convertir a UTC
        this.prestamo.iniDate = new Date(this.prestamo.iniDate.getTime() - this.prestamo.iniDate.getTimezoneOffset() * 60000);
        this.prestamo.endDate = new Date(this.prestamo.endDate.getTime() - this.prestamo.endDate.getTimezoneOffset() * 60000);

        this.prestamoService.savePrestamo(this.prestamo).subscribe({
            next: (result) => {
                console.log("Holaaaaaa");
                this.onClose();
            },
            error: err => {
                console.log("ADIOS");
                this.dialog.open(DialogErrorComponent, {
                    data: {
                      title: 'Error!',
                      description: err.error.error || "Error en guardar el préstamo."
                    }
                });
            }
        });
    }

    onClose() {
        this.dialogRef.close();
    }

}