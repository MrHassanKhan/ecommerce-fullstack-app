import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CartService } from 'app/cart/cart.service';
import {CartDTO, CartItemDTO} from 'app/cart/cart.model';


@Component({
  selector: 'app-cart-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart-list.component.html'})
export class CartListComponent implements OnInit, OnDestroy {

  cartService = inject(CartService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  carts?: CartItemDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@cart.delete.success:Cart Cleared successfully.`,
      'cart.product.cart.referenced': $localize`:@@cart.product.cart.referenced:This entity is still referenced by Product ${details?.id} via field Cart.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }

  loadData() {
    this.cartService.getMyCart()
        .subscribe({
          next: (data) => this.carts = data.cartItems,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }



  clearCart() {
    if (confirm(this.getMessage('confirm'))) {
      this.cartService.clearCart()
          .subscribe({
            next: () => this.router.navigate(['/carts'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/carts'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }

  removeCart(id?: number|null) {
    if (confirm(this.getMessage('confirm'))) {
      this.cartService.removeFromCart(id||0)
          .subscribe({
            next: () => this.loadData(),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

  createOrder() {
    this.router.navigate(['/orders/create']);
  }

}
