import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PaymentService } from 'app/payment/payment.service';
import { PaymentDTO } from 'app/payment/payment.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-payment-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './payment-add.component.html'
})
export class PaymentAddComponent implements OnInit {

  paymentService = inject(PaymentService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  appUserValues?: Map<number,string>;
  orderValues?: Map<number,string>;

  addForm = new FormGroup({
    amount: new FormControl(null, [validDouble]),
    status: new FormControl(null),
    appUser: new FormControl(null),
    order: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@payment.create.success:Payment was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.paymentService.getAppUserValues()
        .subscribe({
          next: (data) => this.appUserValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.paymentService.getOrderValues()
        .subscribe({
          next: (data) => this.orderValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new PaymentDTO(this.addForm.value);
    this.paymentService.createPayment(data)
        .subscribe({
          next: () => this.router.navigate(['/payments'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
