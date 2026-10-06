    import { Injectable } from '@angular/core';
    import { HttpClient } from '@angular/common/http';
    import { Observable } from 'rxjs';

    export interface PortfolioSummary {
    totalPortfolio: number;
    investedAmount: number;
    availableBalance: number;
    todaysProfitLoss: number;
    }

    @Injectable({
    providedIn: 'root'
    })
    export class PortfolioService {

    private apiUrl = 'http://localhost:9090/api/portfolio';

    constructor(private http: HttpClient) {}

    getPortfolioSummary(
        portfolioId: number
    ): Observable<PortfolioSummary> {

        return this.http.get<PortfolioSummary>(
        `${this.apiUrl}/summary/${portfolioId}`
        );
    }
    }