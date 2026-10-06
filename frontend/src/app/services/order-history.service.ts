import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderHistory {
  id: number;
  symbol: string;
  orderType: string;
  quantity: number;
  price: number;
  totalAmount: number;
  status: string;
  rejectionReason: string | null;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  private apiUrl = 'http://localhost:9090/api/orders';

  constructor(private http: HttpClient) {}

  getOrdersByUser(userId: number): Observable<OrderHistory[]> {
    return this.http.get<OrderHistory[]>(
      `${this.apiUrl}/user/${userId}`
    );
  }
}