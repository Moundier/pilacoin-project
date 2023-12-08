import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Transferencia, UsuarioJson } from "./model";
import { PilaCoinService } from "./pilacoin-service";
import { PilaCoin } from "./model";
import { Observable } from "rxjs";

interface TransferenciaDTO {
  chaveUsuarioDestino: string;
  nomeUsuarioDestino: string;
  noncePila: string;
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private API = 'http://localhost:8080/transferir'; 
  private pila?: PilaCoin;

  constructor(private http: HttpClient, private pilaCoinService: PilaCoinService) { }

  getPilaData(): void {
    this.pilaCoinService.getAnyPila().subscribe({
      next: (pila: PilaCoin) => {
        if (this.pila && this.pila.nonce !== null) {
          console.log('Complete pila:', this.pila);
          console.log('Nonce:', this.pila.nonce);
        } else {
          if (pila) {
            console.log("Set nonce: " + pila.nonce);
            this.pila = pila;  // Set this.pila only if pila is defined
          } else {
            console.log("Received undefined or null pila");
          }
        }
      }, 
      error: (error: HttpErrorResponse) => {
        console.log('getPila: ' +  JSON.stringify(error));
        console.log('error: ' + error.status);
      }
    });
  }

  performTransaction(user: UsuarioJson): boolean {

    this.getPilaData();

    const dataDTO = {
      chaveUsuarioDestino: user.chavePublica,
      nomeUsuarioDestino: user.nome,
      noncePila: this.pila?.nonce  // seta nonce
    };

    console.log('Data: ' + dataDTO)

    this.http.post<Transferencia>(this.API, dataDTO).subscribe(
      {
        next: (transferencia: Transferencia) => {
          console.log('Transaction successful', transferencia);
        },
        error: (error: any) => {
          console.error('Transaction failed', error);
        }
      }
    );

    this.pilaCoinService.deletePilaById(this.pila?.id!); // or undefined

    return false; // button returns
  }

}