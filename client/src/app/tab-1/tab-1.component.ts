import { Component } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { SseService } from './sse-service';
import { HttpErrorResponse } from '@angular/common/http';

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
      {
        next: (response: any) => {
          this.messages.push(response);
        },
        error: (error: HttpErrorResponse) => {
          console.error('SSE Error:', error.error);
        }
      }
    );
  }

  ngOnDestroy() {
    this.sseService.closeConnection();
    this.sseSubscription.unsubscribe();
  }
}
