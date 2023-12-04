import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QueueService {
  
  private API = 'http://localhost:8080/query'; // Update with your API endpoint

  constructor(private http: HttpClient) { }

  getFrom(query: string): Observable<any> {
    return this.http.get<any>(`${this.API}/${query}?self=true`);
  }

  getPilas(query: string): Observable<any> {
    return this.http.get<any>(`${this.API}/${query}?self=true`);
  }

  getBlocos(query: string): Observable<any> {
    return this.http.get<any>(`${this.API}/${query}?self=true`);
  }
}
