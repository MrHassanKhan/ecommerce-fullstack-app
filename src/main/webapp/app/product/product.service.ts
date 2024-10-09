import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ProductDTO } from 'app/product/product.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class ProductService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/products';

  getAllProducts() {
    return this.http.get<ProductDTO[]>(this.resourcePath);
  }

  getMyProducts() {
    return this.http.get<ProductDTO[]>(this.resourcePath + '/myProducts');
  }

  getProduct(id: number) {
    return this.http.get<ProductDTO>(this.resourcePath + '/' + id);
  }

  createProduct(productDTO: FormData) {
    return this.http.post<number>(this.resourcePath, productDTO);
  }

  updateProduct(id: number, productDTO: FormData) {
    return this.http.put<number>(this.resourcePath + '/' + id, productDTO);
  }

  deleteProduct(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getCategoryValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/categoryValues')
        .pipe(map(transformRecordToMap));
  }

  getBrandValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/brandValues')
        .pipe(map(transformRecordToMap));
  }

  getCartValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/cartValues')
        .pipe(map(transformRecordToMap));
  }

}
