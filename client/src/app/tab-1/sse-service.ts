// sse.service.ts
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SseService {

  private eventSource!: EventSource;

  getServerSentEventUpdates(): Observable<string> {
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

  closeConnection() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }

}
