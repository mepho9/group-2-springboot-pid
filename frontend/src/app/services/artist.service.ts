import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface Artist {
  id: number;
  firstname: string;
  lastname: string;
}

@Injectable({
  providedIn: 'root'
})
export class ArtistService {
  private apiUrl = '/api/artists';

  constructor(private http: HttpClient) { }

  getArtists(): Observable<Artist[]> {
    return this.http.get<any>(this.apiUrl).pipe(
      map(response => {
        if (response && response._embedded) {
          // Fallback if the collection name varies
          const keys = Object.keys(response._embedded);
          if (keys.length > 0) {
            return response._embedded[keys[0]];
          }
        }
        return [];
      })
    );
  }

  getArtist(id: string): Observable<Artist> {
    return this.http.get<Artist>(`${this.apiUrl}/${id}`);
  }
}
