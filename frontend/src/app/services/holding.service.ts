    import { Injectable } from '@angular/core';
    import { HttpClient } from '@angular/common/http';
    import { Observable } from 'rxjs';

    export interface Holding {
    id: number;
    symbol: string;
    companyName: string;
    quantity: number;
    avgPurchasePrice: number;
    currentPrice: number;
    investedValue: number;
    currentValue: number;
    profitLoss: number;
    }

    @Injectable({
    providedIn: 'root'
    })
    export class HoldingService {

    private apiUrl = 'http://localhost:9090/api/holdings';

    constructor(private http: HttpClient) {}

    getHoldingsByPortfolio(
        portfolioId: number
    ): Observable<Holding[]> {

        return this.http.get<Holding[]>(
        `${this.apiUrl}/portfolio/${portfolioId}`
        );
    }
    }