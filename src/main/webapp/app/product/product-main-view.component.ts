import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProductDTO } from 'app/product/product.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import {PublicService} from "../common/services/public.service";
import {RelatedProductsComponent} from "../common/components/product/related-products.component";
import {AddReviewComponent} from "../common/components/review/add-review.component";
import {ListReviewComponent} from "../common/components/review/list-review.component";
import {AuthenticationService} from "../security/authentication.service";


@Component({
  selector: 'app-product-main-view',
  standalone: true,
  imports: [CommonModule, RouterLink,
    ReactiveFormsModule,
    RelatedProductsComponent,
    ListReviewComponent,
    InputRowComponent],
  templateUrl: './product-main-view.component.html'
})
export class ProductMainViewComponent implements OnInit {
  publicService = inject(PublicService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  private productId?: number;
  product?: ProductDTO;


  ngOnInit() {
    this.route.params.subscribe(
      (params) => {
        this.productId = +params['id'];
        if(this.productId) {
          this.publicService.getProduct(this.productId)
            .subscribe({
              next: (data) => this.product = data,
              error: (error) => this.errorHandler.handleServerError(error.error)
            });
        }
      }
    )
    // this.productId = +this.route.snapshot.params['id'];
    // if(this.productId) {
    //   this.publicService.getProduct(this.productId)
    //     .subscribe({
    //       next: (data) => this.product = data,
    //       error: (error) => this.errorHandler.handleServerError(error.error)
    //     });
    // }

  }

  getStars(rating: number): string[] {
    const stars = [];
    for (let i = 0; i < 5; i++) {
      if (i < rating) {
        if (i < Math.floor(rating)) {
          stars.push('full');
        } else {
          stars.push('half');
        }
      } else {
        stars.push('empty');
      }
    }
    return stars;
  }

}
