import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QueueService } from './queue-service';
import { QueryResponseJson } from './model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-tab-0',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tab-0.component.html',
  styleUrl: './tab-0.component.css'
})
export class Tab0Component {

  users!: QueryResponseJson[];
  pilas!: QueryResponseJson[];
  blocks!: QueryResponseJson[];

  constructor(private queueService: QueueService) {}

  ngOnInit(): void {
    this.subscribeToUsers();
    this.subscribeToPilas();
    this.subscribeToBlocks();
  }

  private subscribeToUsers(): void {
    
    this.queueService.getUsuarios().subscribe({
      next: (value: any) => {
        console.log(value);
      }, 
      error: (error: HttpErrorResponse) => {
        this.handleErrors(error);
      }
    });
  }

  private subscribeToPilas(): void {
    
    this.queueService.getPilas().subscribe({
      next: (value: any) => {
        console.log(value);
      }, 
      error: (error: HttpErrorResponse) => {
        this.handleErrors(error);
      }
    });
  }

  private subscribeToBlocks(): void {

    this.queueService.getBlocos().subscribe({
      next: (value: any) => {
        console.log(value);
      }, 
      error: (error: HttpErrorResponse) => {
        this.handleErrors(error);
      }
    });
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
        console.error('Error 404: ' + error.status);
        break;
      case 500:
        console.error('Error: ' + error.status);
        break;
      default:
        console.error('Error: ' + error.status);
        break;
    }
  }
}
