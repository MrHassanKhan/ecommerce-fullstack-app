import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PaymentDTO } from 'app/payment/payment.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class PaymentService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/payments';

  getAllPayments() {
    return this.http.get<PaymentDTO[]>(this.resourcePath);
  }

  getPayment(id: number) {
    return this.http.get<PaymentDTO>(this.resourcePath + '/' + id);
  }

  createPayment(paymentDTO: PaymentDTO) {
    return this.http.post<number>(this.resourcePath, paymentDTO);
  }

  updatePayment(id: number, paymentDTO: PaymentDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, paymentDTO);
  }

  deletePayment(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getAppUserValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/appUserValues')
        .pipe(map(transformRecordToMap));
  }

  getOrderValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/orderValues')
        .pipe(map(transformRecordToMap));
  }

}
