import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Show {
  id: number;
  slug: string;
  title: string;
  description: string;
  poster: string;
  bookable: boolean;
  price: number;
}

@Injectable({
  providedIn: 'root'
})

export class ShowService{
   private apiUrl = '/api/shows'

  constructor(private  http: HttpClient) {}

  getShow(): Observable<Show[]> {
     return this.http.get<Show[]>(this.apiUrl);
  }
}
