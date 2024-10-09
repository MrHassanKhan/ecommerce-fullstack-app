import { Injectable, inject } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { environment } from 'environments/environment';
import { ProductDTO } from 'app/product/product.model';
import {map, Observable} from 'rxjs';
import {transformObjectToAnotherObject, transformRecordToMap} from 'app/common/utils';
import {PaginatedResponse} from "../PaginatedResponse";
import {BrandWithProductCountDTO} from "../brand/brand.model";
import {CategoryProductCountDTO} from "../category/category.model";


@Injectable({
    providedIn: 'root',
})
export class PublicService {

    http = inject(HttpClient);
    resourcePath = environment.apiPath + '/api/public';

    searchProductsWithQuery(searchField: string, searchValue: string,
                            page: number, size: number,
                            sortField: string, sortDirection: string): Observable<PaginatedResponse<ProductDTO>> {
        return this.http.get<PaginatedResponse<ProductDTO>>(this.resourcePath + '/searchProducts', {
            params: {
                searchField: searchField,
                searchValue: searchValue,
                page: page.toString(),
                size: size.toString(),
                sortField: sortField,
                sortDirection: sortDirection
            }
        });
    }

    filterProducts(filter: {searchText?: string, searchField?: string, minPrice?:number, maxPrice?:number, page?: number, size?: number, sortBy?: string, sortDirection?: string}): Observable<PaginatedResponse<ProductDTO>> {
      const params = new HttpParams({
        fromObject: transformObjectToAnotherObject(filter)
      });
      return this.http.get<PaginatedResponse<ProductDTO>>(this.resourcePath + '/filter', { params });
    }

    shopFilter(filter: {categorieIds?: any[], brandIds?:any[], minPrice?: number, maxPrice?: number, page?: number, size?: number, sortBy?: string, sortDirection?: string}): Observable<PaginatedResponse<ProductDTO>> {
      const {brandIds, categorieIds, minPrice, maxPrice} = filter;
      delete filter.categorieIds;
      delete filter.brandIds;
      delete filter.minPrice;
      delete filter.maxPrice;
      const params = new HttpParams({
        fromObject: {
          ...transformObjectToAnotherObject(filter),
        }
      });
      return this.http.post<PaginatedResponse<ProductDTO>>(this.resourcePath + '/shop-filter', {
        brandIds, categorieIds , minPrice, maxPrice
      } ,{ params });
    }

    getBrandsProductCount(): Observable<BrandWithProductCountDTO[]> {
      return this.http.get<BrandWithProductCountDTO[]>(this.resourcePath + '/brand-product-count');
    }

    getCategoriesProductCount(): Observable<CategoryProductCountDTO[]> {
      return this.http.get<CategoryProductCountDTO[]>(this.resourcePath + '/category-product-count');
    }
    getProduct(id: number): Observable<ProductDTO> {
      return this.http.get<ProductDTO>(this.resourcePath + '/getProduct/' + id);
    }
}
