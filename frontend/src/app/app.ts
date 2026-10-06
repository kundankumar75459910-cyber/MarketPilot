import {
  WatchlistService,
  WatchlistItem
} from './services/watchlist.service';
import {
  OrderHistory,
  OrderHistoryService
} from './services/order-history.service';
import {
  Transaction,
  TransactionService
} from './services/transaction.service';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { DecimalPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';

import { Stock, StockService } from './services/stock.service';
import { Holding, HoldingService } from './services/holding.service';

import {
  PortfolioSummary,
  PortfolioService
} from './services/portfolio.service';

import {
  OrderRequest,
  OrderService,
  OrderResponse
} from './services/order.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    DecimalPipe,
    DatePipe,
    FormsModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {

  stocks: Stock[] = [];
  loading = true;
  errorMessage = '';

  holdings: Holding[] = [];
  holdingsLoading = true;
  holdingsError = ''; 

  transactions: Transaction[] = [];
transactionsLoading = true;
transactionsError = '';

orders: OrderHistory[] = [];
ordersLoading = true;
ordersError = '';
orderLoading: 'buy' | 'sell' | null = null;

watchlist: WatchlistItem[] = [];
watchlistLoading = true;
watchlistError = '';
watchlistActionStockId: number | null = null;
watchlistMessage = '';

private userId = 1;

  portfolioSummary: PortfolioSummary | null = null;
portfolioLoading = true;
portfolioError = '';
selectedStockId = 1;
orderQuantity = 1;

orderMessage = '';
orderError = '';


 constructor(
  private stockService: StockService,
  private holdingService: HoldingService,
  private portfolioService: PortfolioService,
  private orderService: OrderService,
  private transactionService: TransactionService,
  private orderHistoryService: OrderHistoryService,
  private watchlistService: WatchlistService,
  private cdr: ChangeDetectorRef
) {}
  ngOnInit(): void {

    console.log('APP COMPONENT STARTED');

    // Load stocks
    this.stockService.getAllStocks().subscribe({

      next: (data) => {
        console.log('Stocks received from backend:', data);

        this.stocks = data;
        this.loading = false;

        console.log('Loading after API:', this.loading);
        console.log('Stock count:', this.stocks.length);

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('STOCK API ERROR:', error);

        this.errorMessage = 'Unable to load stock data';
        this.loading = false;

        this.cdr.detectChanges();
      }

    });

    // Load holdings

   // Load dashboard data

this.loadHoldings();
this.loadPortfolioSummary();
this.loadTransactions();
this.loadOrders();
this.loadWatchlist();
  }
  loadPortfolioSummary(): void {

  this.portfolioService.getPortfolioSummary(1).subscribe({

    next: (data) => {

      console.log(
        'Portfolio summary received:',
        data
      );

      this.portfolioSummary = data;
      this.portfolioLoading = false;

      this.cdr.detectChanges();
    },

    error: (error) => {

      console.error(
        'PORTFOLIO SUMMARY API ERROR:',
        error
      );

      this.portfolioError =
        'Unable to load portfolio summary';

      this.portfolioLoading = false;

      this.cdr.detectChanges();
    }

  });
}

  loadHoldings(): void {

    this.holdingService.getHoldingsByPortfolio(1).subscribe({

      next: (data) => {

        console.log('Holdings received from backend:', data);

        this.holdings = data;
        this.holdingsLoading = false;

        console.log('Holding count:', this.holdings.length);

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error('HOLDINGS API ERROR:', error);

        this.holdingsError = 'Unable to load holdings';
        this.holdingsLoading = false;

        this.cdr.detectChanges();
      }

    });
  }

  getPercentageChange(stock: Stock): number {

    if (stock.previousPrice === 0) {
      return 0;
    }

    return (
      ((stock.currentPrice - stock.previousPrice)
        / stock.previousPrice) * 100
    );
  }
  loadTransactions(): void {

  this.transactionService
    .getTransactionsByUser(1)
    .subscribe({

      next: (data) => {

        console.log(
          'Transactions received from backend:',
          data
        );

        this.transactions = data;
        this.transactionsLoading = false;

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error(
          'TRANSACTIONS API ERROR:',
          error
        );

        this.transactionsError =
          'Unable to load transaction history';

        this.transactionsLoading = false;

        this.cdr.detectChanges();
      }
    });
}
  buyStock(): void {

  if (this.orderLoading !== null) {
    return;
  }

  if (this.orderQuantity <= 0) {
    this.orderError = 'Quantity must be greater than 0';
    return;
  }

  const request: OrderRequest = {
    portfolioId: 1,
    stockId: this.selectedStockId,
    quantity: this.orderQuantity
  };

  this.orderLoading = 'buy';

  this.orderMessage = '';
  this.orderError = '';

  this.orderService
    .buy(request)
    .subscribe({

      next: (response: OrderResponse) => {

        console.log('BUY SUCCESS:', response);

        this.orderMessage =
          `${response.message}: ` +
          `${response.quantity} ` +
          `${response.symbol} bought for ₹` +
          `${response.totalAmount}`;

        this.orderError = '';

        this.orderLoading = null;

        this.loadHoldings();
        this.loadPortfolioSummary();
        this.loadTransactions();
        this.loadOrders();

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error('BUY ERROR:', error);

        this.orderError =
          error.error?.message ||
          error.error ||
          'Unable to complete buy order';

        this.orderLoading = null;

        this.cdr.detectChanges();
      },

      complete: () => {

        console.log('BUY REQUEST COMPLETED');

        this.orderLoading = null;

        this.cdr.detectChanges();
      }

    });
}
sellStock(): void {

  if (this.orderLoading !== null) {
    return;
  }

  if (this.orderQuantity <= 0) {
    this.orderError = 'Quantity must be greater than 0';
    return;
  }

  const request: OrderRequest = {
    portfolioId: 1,
    stockId: this.selectedStockId,
    quantity: this.orderQuantity
  };

  this.orderLoading = 'sell';

  this.orderMessage = '';
  this.orderError = '';

  this.orderService
    .sell(request)
    .subscribe({

      next: (response: OrderResponse) => {

        console.log('SELL SUCCESS:', response);

        this.orderMessage =
          `${response.message}: ` +
          `${response.quantity} ` +
          `${response.symbol} sold for ₹` +
          `${response.totalAmount}`;

        this.orderError = '';

        this.orderLoading = null;

        this.loadHoldings();
        this.loadPortfolioSummary();
        this.loadTransactions();
        this.loadOrders();

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error('SELL ERROR:', error);

        this.orderError =
          error.error?.message ||
          error.error ||
          'Unable to complete sell order';

        this.orderLoading = null;

        this.cdr.detectChanges();
      },

      complete: () => {

        console.log('SELL REQUEST COMPLETED');

        this.orderLoading = null;

        this.cdr.detectChanges();
      }

    });
}
loadOrders(): void {

  this.orderHistoryService
    .getOrdersByUser(1)
    .subscribe({

      next: (data) => {

        console.log(
          'Orders received from backend:',
          data
        );

        this.orders = data;
        this.ordersLoading = false;

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error(
          'ORDERS API ERROR:',
          error
        );

        this.ordersError =
          'Unable to load order history';

        this.ordersLoading = false;

        this.cdr.detectChanges();
      }
    });
}
loadWatchlist(): void {

  this.watchlistLoading = true;

  this.watchlistService
    .getWatchlist(this.userId)
    .subscribe({

      next: (data: WatchlistItem[]) => {

        console.log(
          'Watchlist received from backend:',
          data
        );

        this.watchlist = data;

        this.watchlistLoading = false;

        // IMPORTANT:
        // API response has completed,
        // so remove Processing...
        this.watchlistActionStockId = null;

        this.cdr.detectChanges();
      },

      error: (error: any) => {

        console.error(
          'WATCHLIST API ERROR:',
          error
        );

        this.watchlistError =
          'Unable to load watchlist';

        this.watchlistLoading = false;

        // Also stop Processing on error
        this.watchlistActionStockId = null;

        this.cdr.detectChanges();
      }

    });
}
isInWatchlist(stockId: number): boolean {
  return this.watchlist.some(
    item => item.stockId === stockId
  );
}
isWatchlistProcessing(stockId: number): boolean {
  return this.watchlistActionStockId !== null
      && this.watchlistActionStockId === stockId;
}

addToWatchlist(stockId: number): void {

  if (this.watchlistActionStockId !== null) {
    return;
  }

  this.watchlistActionStockId = stockId;
  this.watchlistMessage = '';
  this.watchlistError = '';

  this.watchlistService
    .addToWatchlist(this.userId, stockId)
    .subscribe({

     next: (message) => {

  console.log('WATCHLIST ADD SUCCESS:', message);

  this.watchlistMessage = message;

  this.watchlistActionStockId = null;

  console.log(
    'AFTER RESET:',
    this.watchlistActionStockId
  );

  this.loadWatchlist();

  this.cdr.detectChanges();

  console.log(
    'AFTER DETECT CHANGES:',
    this.watchlistActionStockId
  );
},
      error: (error: any) => {

        console.error(
          'ADD WATCHLIST ERROR:',
          error
        );

        this.watchlistError =
          error.error?.message ||
          error.error ||
          'Unable to add stock to watchlist';

        this.watchlistActionStockId = null;

        this.cdr.detectChanges();
      }

    });
}
removeFromWatchlist(stockId: number): void {

  if (this.watchlistActionStockId !== null) {
    return;
  }

  this.watchlistActionStockId = stockId;
  this.watchlistMessage = '';
  this.watchlistError = '';

  this.watchlistService
    .removeFromWatchlist(this.userId, stockId)
    .subscribe({

      next: (message) => {

        console.log(
          'WATCHLIST REMOVE SUCCESS:',
          message
        );

        this.watchlistMessage = message;

        this.loadWatchlist();
      },

      error: (error: any) => {

        console.error(
          'REMOVE WATCHLIST ERROR:',
          error
        );

        this.watchlistError =
          error.error?.message ||
          error.error ||
          'Unable to remove stock from watchlist';

        this.watchlistActionStockId = null;

        this.cdr.detectChanges();
      }

    });
}
}