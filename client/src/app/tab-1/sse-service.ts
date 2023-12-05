// sse.service.ts
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SseService {
  private eventSource!: EventSource;

  connect(): Observable<any> {
    this.eventSource = new EventSource('http://localhost:8080/sse/stream'); // Adjust the URL as needed
    return new Observable(observer => {
      this.eventSource.onmessage = event => {
        const data = JSON.parse(event.data);
        observer.next(data);
      };

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
