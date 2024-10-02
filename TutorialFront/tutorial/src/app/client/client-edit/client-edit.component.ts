import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { Client } from '../model/Client';

@Component({
  selector: 'app-client-edit',
  templateUrl: './client-edit.component.html',
  styleUrls: ['./client-edit.component.scss']
})

export class ClientEditComponent implements OnInit {

  client : Client;

  constructor(
    public dialogRef: MatDialogRef<ClientEditComponent>, //Permite controlar el dialogo (abrir o cerrar)
    @Inject(MAT_DIALOG_DATA) public data: any,
    private clientService: ClientService //Uso de metodos del servicio
  ) { }

  //metodo que se llama despues de la inicializacion del componente
  ngOnInit(): void {
    //se crea una instancia de Client, se prepara para permitir añadir un nuevo usuario o editar uno existente
    if (this.data.client != null) {
      this.client = Object.assign({}, this.data.client); //Antes pasabamos el mismo objeto desde el listado a la ventana de dialogo y se veia como se editaban a la vez
      //es por ello que ahora se hace otra asignacion para que el objeto no sea el mismo.
    }
    else {
      this.client = new Client();
    }
  }

  onSave() {
    this.clientService.saveClient(this.client).subscribe(result => {
      this.dialogRef.close();
    });    
  }  

  onClose() {
    this.dialogRef.close();
  }

}