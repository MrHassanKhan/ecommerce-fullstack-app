import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import {CreateOrderDTO, OrderDTO} from 'app/order/order.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class OrderService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/orders';

  getAllOrders() {
    return this.http.get<OrderDTO[]>(this.resourcePath);
  }

  getOrder(id: number) {
    return this.http.get<OrderDTO>(this.resourcePath + '/' + id);
  }

  createOrder(orderDTO: CreateOrderDTO) {
    return this.http.post<number>(this.resourcePath, orderDTO);
  }

  updateOrderStatus(id: number, status: string) {
    return this.http.patch(this.resourcePath + '/' + id, null, {params: {status: status}});
  }

}
