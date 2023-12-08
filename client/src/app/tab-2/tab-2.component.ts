import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PilaCoinJson, QueryResponse } from '../tab-0/model';
import { QueueService } from '../tab-0/queue-service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-tab-2',
  templateUrl: './tab-2.component.html',
  styleUrl: './tab-2.component.css'
})
export class Tab2Component {

  queryData!: QueryResponse<PilaCoinJson>;
  isLoading!: boolean;
  isError!: boolean;

  pilas!: PilaCoinJson[];

  constructor(private queueService: QueueService) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {

    this.isLoading = true;
    this.isError = false;

    this.queueService.getFrom("pilas").subscribe(
      {
        next: (response: QueryResponse<PilaCoinJson>) => {

          console.log("Loggin : [this.queueService.getFrom(\"usuarios\")] " + response);
          console.log("Loggin : [this.queueService.getFrom(\"usuarios\")] " + JSON.stringify(response));
          
          this.queryData = response;

          this.pilas = response.result;

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
}
