import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface WatchlistItem {
  id: number;
  userId: number;
  stockId: number;
  addedAt: string;
}


@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  private apiUrl = 'http://localhost:9090/api/watchlist';

  constructor(private http: HttpClient) {}

  getWatchlist(
    userId: number
  ): Observable<WatchlistItem[]> {

    return this.http.get<WatchlistItem[]>(
      `${this.apiUrl}/user/${userId}`
    );
  }

  addToWatchlist(
    userId: number,
    stockId: number
  ): Observable<string> {

    return this.http.post(
      `${this.apiUrl}/user/${userId}/stock/${stockId}`,
      {},
      {
        responseType: 'text'
      }
    );
  }

  removeFromWatchlist(
    userId: number,
    stockId: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}/user/${userId}/stock/${stockId}`,
      {
        responseType: 'text'
      }
    );
  }
}