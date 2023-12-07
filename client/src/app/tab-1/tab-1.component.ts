import { Component, OnDestroy, OnInit } from '@angular/core';
import { SseService } from './sse-service';
import { Observable, Subscription } from 'rxjs';
import { SseMessage, SseMessageType } from '../tab-0/model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-tab-1',
  templateUrl: './tab-1.component.html',
  styleUrl: './tab-1.component.css'
})
export class Tab1Component {

  public pulseMessages: string[] = [];
  public sseMessages: SseMessage[] = [];
  private sseSubscription!: Subscription;

  constructor(private sseService: SseService) { }

  ngOnInit() {
    this.findEvent();
    this.connect();
  }

  findEvent(): void {
    this.sseSubscription = this.sseService.findEvent().subscribe({
      next: (message: string) => {
        this.pulseMessages.push(message);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Sse (findEvent):', error.status)
      }
    });
  }

  connect(): void {
    this.sseSubscription = this.sseService.connectEvent().subscribe({
      next: (sseMessage: string) => {

        let sseConverted: SseMessage;

        try {
          sseConverted = JSON.parse(sseMessage);
          this.sseMessages.push(sseConverted);
        } catch (error) {
          console.error('Error parsing JSON:', error);
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error('Sse (findConnection):', error.status)
      }
    });
  }

  ngOnDestroy() {
    this.sseSubscription.unsubscribe();
    this.sseService.closeConnection();
  }

}