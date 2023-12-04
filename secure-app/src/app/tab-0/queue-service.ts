import { Injectable, Injector } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { QueryResponseJson } from './model';
import { AnyCatcher } from 'rxjs/internal/AnyCatcher';

@Injectable({
  providedIn: 'root'
})
export class QueueService {
  
  private API = 'http://localhost:8080/query';
  private http: HttpClient | undefined;

  

  constructor(private injector: Injector) {}

  private get httpService(): HttpClient {
    if (!this.http) {
      // Lazily get the HttpClient instance to avoid circular dependency
      this.http = this.injector.get(HttpClient);
    }
    return this.http;
  }

  getUsuarios(): Observable<any> {
    let params = {
      category: 'self',
      status: 'true'
    };
    return this.httpService.get<AnyCatcher>(`${this.API}/usuarios`, { params });
  }

  getPilas(): Observable<any> {
    let params = {
      category: 'self',
      status: 'true'
    };

    return this.httpService.get<any>(`${this.API}/pilas`, { params });
  }

  getBlocos(): Observable<any> {
    let params = {
      category: 'self',
      status: 'true'
    };

    return this.httpService.get<any>(`${this.API}/blocos`, { params });
  }
}
