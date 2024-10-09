import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {PublicService} from "../common/services/public.service";
import {ErrorHandler} from "../common/error-handler.injectable";
import {ProductView1Component} from "../common/components/product/product-view1.component";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, ProductView1Component],
})
export class ShopComponent implements OnInit{

  publicService = inject(PublicService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);

  productData?: any[];
  brandData?: any[];
  categoryData?: any[];

  categoryIds: any[] = [];
  brandIds: any[] = [];
  minPrice: number = 0;
  maxPrice: number = 0;
  sortDirection: string = 'ASC';
  sortField: string = 'price';

  ngOnInit() {
    this.loadData();
    this.loadBrandsData();
    this.loadCategoriesData();
  }

  loadData() {
    if(this.sortDirection === 'latestProduct') {
      this.sortDirection = 'DESC';
      this.sortField = "id";
    } else {
      this.sortField = "price";
    }
    this.publicService.shopFilter({
      categorieIds: this.categoryIds,
      brandIds: this.brandIds,
      minPrice: this.minPrice,
      maxPrice: this.maxPrice,
      sortDirection: this.sortDirection,
      sortBy: this.sortField
    }).subscribe({
      next: (data) => {
        // console.log(data)
        this.productData = data.content;
      },
      error: (error) => this.errorHandler.handleServerError(error.error)
    });
  }

  loadBrandsData() {
    this.publicService.getBrandsProductCount().subscribe({
      next: (data) => {
        // console.log(data)
        this.brandData = data
      },
      error: (error) => this.errorHandler.handleServerError(error.error)
    });
  }

  loadCategoriesData() {
    this.publicService.getCategoriesProductCount().subscribe({
      next: (data) => {
        // console.log(data)
        this.categoryData = data
      },
      error: (error) => this.errorHandler.handleServerError(error.error)
    });
  }


  onBrandChange(event: any, brandId: number) {
    if(event?.target?.checked) {
      this.brandIds.push(brandId);
    } else {
      this.brandIds = this.brandIds.filter((id) => id !== brandId);
    }

    this.loadData();
  }

  onCategoryChange(event: any, categoryId: number) {
    if(event?.target?.checked) {
      this.categoryIds.push(categoryId);
    } else {
      this.categoryIds = this.categoryIds.filter((id) => id !== categoryId);
    }

    this.loadData();
  }


}
