import { Component, OnDestroy, OnInit } from '@angular/core';
import { SseService } from './sse-service';
import { Observable, Subscription } from 'rxjs';
import { SseMessage, SseMessageType } from '../tab-0/model';

@Component({
  selector: 'app-tab-1',
  templateUrl: './tab-1.component.html',
  styleUrl: './tab-1.component.css'
})
export class Tab1Component {

  messages: string[] = [];
  private sseSubscription!: Subscription;

  validBlocks: SseMessage[] = [];
  minedBlocks: SseMessage[] = [];

  validCoins: SseMessage[] = [];
  minedCoins: SseMessage[] = [];

  constructor(private sseService: SseService) { }

  ngOnInit() {
    this.findEvent();
    this.findConnection();
  }

  findEvent(): void {
    this.sseSubscription = this.sseService.findEvent().subscribe({
      next: (message: string) => {
        this.messages.push(message);
      },
      error: (error: any) => {
        console.error('Error in SSE:', error)
      }
    });
  }

  findConnection(): void {
    this.sseSubscription = this.sseService.connectEvent().subscribe({
      next: (sseMessage: SseMessage) => {

        console.log(sseMessage)

        switch (sseMessage.messageType) {
          case SseMessageType.MINED_BLOCK:
            console.log(sseMessage)
            this.minedBlocks.push(sseMessage);
            return;
          case SseMessageType.VALID_BLOCK:
            console.log(sseMessage)
            this.validBlocks.push(sseMessage);
            return;
          case SseMessageType.MINED_PILA:
            console.log(sseMessage)
            this.minedCoins.push(sseMessage);
            return;
          case SseMessageType.VALID_PILA:
            console.log(sseMessage)
            this.validCoins.push(sseMessage);
            return;
          case SseMessageType.TRANSFERRED_PILA:
            console.log('Do nothing for now');
            return;
          default:
            return;
        }
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  ngOnDestroy() {
    this.sseSubscription.unsubscribe();
    this.sseService.closeConnection();
  }
}