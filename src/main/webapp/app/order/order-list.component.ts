import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { OrderService } from 'app/order/order.service';
import { OrderDTO } from 'app/order/order.model';


@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './order-list.component.html'})
export class OrderListComponent implements OnInit, OnDestroy {

  orderService = inject(OrderService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  orders?: OrderDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@order.delete.success:Order was removed successfully.`,
      'order.product.order.referenced': $localize`:@@order.product.order.referenced:This entity is still referenced by Product ${details?.id} via field Order.`,
      'order.payment.order.referenced': $localize`:@@order.payment.order.referenced:This entity is still referenced by Payment ${details?.id} via field Order.`
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
    this.orderService.getAllOrders()
        .subscribe({
          next: (data) => this.orders = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      // this.orderService.deleteOrder(id)
      //     .subscribe({
      //       next: () => this.router.navigate(['/orders'], {
      //         state: {
      //           msgInfo: this.getMessage('deleted')
      //         }
      //       }),
      //       error: (error) => {
      //         if (error.error?.code === 'REFERENCED') {
      //           const messageParts = error.error.message.split(',');
      //           this.router.navigate(['/orders'], {
      //             state: {
      //               msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
      //             }
      //           });
      //           return;
      //         }
      //         this.errorHandler.handleServerError(error.error)
      //       }
      //     });
    }
  }

}
