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
export class ShowService {
  private apiUrl = '/api/shows';
  private reservationsUrl = '/api/reservations';

  constructor(private http: HttpClient) {}

  getShow(): Observable<Show[]> {
    return this.http.get<Show[]>(this.apiUrl);
  }

  getShowById(id: number): Observable<Show> {
    return this.http.get<Show>(`${this.apiUrl}/${id}`);
  }

  getShowReviews(showId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${showId}/reviews`);
  }

  addShowReview(showId: number, reviewData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${showId}/reviews`, reviewData);
  }

  getUserReservations(userLogin: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.reservationsUrl}?userLogin=${userLogin}`);
  }

  checkUserReservation(userLogin: string, showId: number): Observable<{ reserved: boolean }> {
    return this.http.get<{ reserved: boolean }>(`${this.reservationsUrl}/check?userLogin=${userLogin}&showId=${showId}`);
  }

  createReservation(userLogin: string, showId: number): Observable<any> {
    return this.http.post<any>(this.reservationsUrl, { userLogin, showId });
  }
}
