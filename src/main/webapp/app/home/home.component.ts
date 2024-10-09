import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {NavigationEnd, Router, RouterLink} from '@angular/router';
import {ErrorHandler} from "../common/error-handler.injectable";
import {ProductDTO} from "../product/product.model";
import {Subscription} from "rxjs";
import {PublicService} from "../common/services/public.service";
import {CartService} from "../cart/cart.service";
import {ProductView1Component} from "../common/components/product/product-view1.component";


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, ProductView1Component],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  publicService = inject(PublicService);

  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  products?: ProductDTO[];
  navigationSubscription?: Subscription;


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
      this.publicService.searchProductsWithQuery("","",0,10,"id", "desc")
        .subscribe({
          next: (data) => this.products = data.content,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }


}
