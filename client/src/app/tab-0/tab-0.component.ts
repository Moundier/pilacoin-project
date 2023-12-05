import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QueueService } from './queue-service';
import { QueryResponse, QueryResponseJson, UsuarioJson } from './model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-tab-0',
  templateUrl: './tab-0.component.html',
  styleUrl: './tab-0.component.css'
})
export class Tab0Component {

  queryData!: QueryResponse<UsuarioJson>;
  isLoading!: boolean;
  isError!: boolean;

  users!: UsuarioJson[];

  constructor(private queueService: QueueService) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {

    this.isLoading = true;
    this.isError = false;

    this.queueService.getFrom("usuarios").subscribe(
      {
        next: (response: QueryResponse<UsuarioJson>) => {

          console.log("Loggin : [this.queueService.getFrom(\"usuarios\")] " + response);
          console.log("Loggin : [this.queueService.getFrom(\"usuarios\")] " + JSON.stringify(response));
          
          this.queryData = response;

          this.users = response.result;

          this.isLoading = false;
        },
        error: (error: HttpErrorResponse) => {
          this.isLoading = false;
          this.isError = true;
          this.handleErrors(error);
        }
      }
    );
  }

  private handleErrors(error: HttpErrorResponse): void {
    switch (error.status) {
      case 0:
        console.warn("Lost connection" + error.status)
        break;
      case 401:
        console.error('Error 401: ' + error.status);
        break;
      case 404:
        console.error('Not Found: ' + error.status);
        break;
      case 500:
        console.error('Internal Server Error: ' + error.status);
        break;
      default:
        console.error('Error: ' + error.status);
        break;
    }
  }

  // Add a method to handle reloading
  reload(): void {
    this.loadData();
  }

}
