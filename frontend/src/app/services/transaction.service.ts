import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Transaction {
  id: number;
  symbol: string;
  transactionType: string;
  quantity: number;
  price: number;
  totalAmount: number;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private apiUrl = 'http://localhost:9090/api/transactions';

  constructor(private http: HttpClient) {}

  getTransactionsByUser(userId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(
      `${this.apiUrl}/user/${userId}`
    );
  }
}