import { Component } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { SseService } from './sse-service';

@Component({
  selector: 'app-tab-1',
  templateUrl: './tab-1.component.html',
  styleUrl: './tab-1.component.css'
})
export class Tab1Component {

  messages: any[] = [];
  private sseSubscription!: Subscription;

  constructor(private sseService: SseService) {}

  ngOnInit() {
    this.sseSubscription = this.sseService.connect().subscribe(
      (data: any) => {
        this.messages.push(data);
      },
      error => {
        console.error('SSE Error:', error);
      }
    );
  }

  ngOnDestroy() {
    this.sseService.closeConnection();
    this.sseSubscription.unsubscribe();
  }
}
