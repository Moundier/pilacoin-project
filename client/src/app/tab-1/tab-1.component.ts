import { Component, OnDestroy, OnInit } from '@angular/core';
import { SseService } from './sse-service';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-tab-1',
  templateUrl: './tab-1.component.html',
  styleUrl: './tab-1.component.css'
})
export class Tab1Component {

 
  messages: string[] = [];
    private sseSubscription!: Subscription;

    constructor(private sseService: SseService) {}

    ngOnInit() {
        this.sseSubscription = this.sseService.getServerSentEventUpdates().subscribe(
            (message: string) => this.messages.push(message),
            error => console.error('Error in SSE:', error)
        );
    }

    ngOnDestroy() {
        this.sseSubscription.unsubscribe();
        this.sseService.closeConnection();
    }
}
