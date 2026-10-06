import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderRequest {
  portfolioId: number;
  stockId: number;
  quantity: number;
}

export interface OrderResponse {
  message: string;
  symbol: string;
  quantity: number;
  price: number;
  totalAmount: number;
  remainingBalance: number;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:9090/api/orders';

  constructor(private http: HttpClient) {}

  buy(request: OrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(
      `${this.apiUrl}/buy`,
      request
    );
  }

  sell(request: OrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(
      `${this.apiUrl}/sell`,
      request
    );
  }
}