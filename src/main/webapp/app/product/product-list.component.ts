import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { ProductService } from 'app/product/product.service';
import { ProductDTO } from 'app/product/product.model';
import {AuthenticationService} from "../security/authentication.service";


@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-list.component.html'})
export class ProductListComponent implements OnInit, OnDestroy {

  productService = inject(ProductService);
  authService = inject(AuthenticationService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  products?: ProductDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@product.delete.success:Product was removed successfully.`    };
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

    if(this.authService.hasRole("ADMIN")) {
      this.productService.getAllProducts()
        .subscribe({
          next: (data) => this.products = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    } else {
      this.productService.getMyProducts()
        .subscribe({
          next: (data) => this.products = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    }

  }


  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.productService.deleteProduct(id)
          .subscribe({
            next: () => this.router.navigate(['/products'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
