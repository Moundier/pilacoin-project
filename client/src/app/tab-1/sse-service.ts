// sse.service.ts
import { EventListenerFocusTrapInertStrategy } from '@angular/cdk/a11y';
import { Injectable } from '@angular/core';
import { Observable, Observer } from 'rxjs';
import { SseMessage } from '../tab-0/model';

@Injectable({
  providedIn: 'root',
})
export class SseService {

  private eventSource!: EventSource;

  findEvent(): Observable<string> {
    this.eventSource = new EventSource('http://localhost:8080/sse/updates');

    return new Observable(observer => {
      this.eventSource.addEventListener('message', (event: MessageEvent) => {
        observer.next(event.data);
      });

      this.eventSource.onerror = error => {
        observer.error(error);
      };
    });
  }

  connectEvent(): Observable<SseMessage> {
    this.eventSource = new EventSource('http://localhost:8080/sse/connect');

    return new Observable(observer => {
      this.eventSource.addEventListener('message', (event: MessageEvent) => {
        observer.next(event.data);
      });

      this.eventSource.onerror = error => {
        observer.error(error);
      };
    });
  }

  closeConnection(): void {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }

}
