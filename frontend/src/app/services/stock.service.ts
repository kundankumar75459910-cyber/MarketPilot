    import { Injectable } from '@angular/core';
    import { HttpClient } from '@angular/common/http';
    import { Observable } from 'rxjs';

    export interface Stock {
    id: number;
    symbol: string;
    companyName: string;
    sector: string;
    currentPrice: number;
    previousPrice: number;
    marketCap: number;
    peRatio: number;
    eps: number;
    revenue: number;
    }

    @Injectable({
    providedIn: 'root'
    })
    export class StockService {

    private apiUrl = 'http://localhost:9090/api/stocks';

    constructor(private http: HttpClient) {}

    getAllStocks(): Observable<Stock[]> {
        return this.http.get<Stock[]>(this.apiUrl);
    }

    getStockById(id: number): Observable<Stock> {
        return this.http.get<Stock>(`${this.apiUrl}/${id}`);
    }

    getStockBySymbol(symbol: string): Observable<Stock> {
        return this.http.get<Stock>(
        `${this.apiUrl}/symbol/${symbol}`
        );
    }
    }