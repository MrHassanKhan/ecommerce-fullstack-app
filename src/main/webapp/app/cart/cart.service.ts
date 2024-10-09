import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CartDTO } from 'app/cart/cart.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class CartService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/carts';

  getMyCart() {
    return this.http.get<CartDTO>(this.resourcePath);
  }
  addToCart(productId?: number | null, quantity?: number|null) {
    return this.http.post<CartDTO>(this.resourcePath + '/addToCart?productId=' + productId + '&quantity=' + quantity, null);
  }
  removeFromCart(productId: number) {
    return this.http.post<CartDTO>(this.resourcePath + '/removeFromCart?productId=' + productId, null);
  }

  clearCart() {
    return this.http.post<CartDTO>(this.resourcePath + '/clearCart', null);
  }

  // getAppUserValues() {
  //   return this.http.get<Record<string,string>>(this.resourcePath + '/appUserValues')
  //       .pipe(map(transformRecordToMap));
  // }

}
