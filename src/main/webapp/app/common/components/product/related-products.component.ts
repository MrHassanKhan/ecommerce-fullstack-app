import {Component, inject, Input, OnInit} from "@angular/core";
import {CommonModule} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {PublicService} from "../../services/public.service";
import {ProductDTO} from "../../../product/product.model";
import {ErrorHandler} from "../../error-handler.injectable";
import {ProductView1Component} from "./product-view1.component";

@Component({
  selector: 'app-related-products',
  standalone: true,
  imports: [CommonModule, RouterLink, ProductView1Component],
  styles: [``],
  template: `
    @if(products && products.length > 0) {
      <div class="container pb-16">
        <h2 class="text-2xl font-medium text-gray-800 uppercase mb-6">Related products</h2>
        <div class="grid grid-cols-2 md:grid-cols-4  gap-6">
            <app-product-view1 *ngFor="let product of products" [product]="product"></app-product-view1>
        </div>
      </div>
    }
  `
})
export class RelatedProductsComponent implements OnInit {
  @Input() categoryIds: number[] = [];
  @Input() excludeProductId?: number;

  publicService = inject(PublicService)
  router = inject(Router)
  errorHandler = inject(ErrorHandler)
  products?: ProductDTO[];

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.publicService.shopFilter({categorieIds: this.categoryIds, size:4}).subscribe(
      (data) => {
        this.products = data.content;
        if(this.excludeProductId) {
          this.products = this.products?.filter(product => product.id !== this.excludeProductId);
        }
      },
      (error) => this.errorHandler.handleServerError(error.error)
    );
  }

}
