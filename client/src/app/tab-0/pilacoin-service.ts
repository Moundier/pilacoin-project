import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PilaCoin } from './model';

@Injectable({
  providedIn: 'root',
})
export class PilaCoinService {
  
  private API = 'http://localhost:8080/pila';

  constructor(private http: HttpClient) {}

  getAnyPila(): Observable<PilaCoin> {
    return this.http.get<PilaCoin>(`${this.API}/any`);
  }

  deletePilaById(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}