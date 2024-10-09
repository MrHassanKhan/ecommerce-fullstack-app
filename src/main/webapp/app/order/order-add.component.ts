import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { OrderService } from 'app/order/order.service';
import {CreateOrderDTO, OrderDTO} from 'app/order/order.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-order-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './order-add.component.html'
})
export class OrderAddComponent implements OnInit {

  orderService = inject(OrderService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  appUserValues?: Map<number,string>;

  addForm = new FormGroup({
    shippingAddress: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    shippingCity: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    shippingState: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    shippingZip: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    shippingCountry: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@order.create.success:Order was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {

  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new CreateOrderDTO(this.addForm.value);
    this.orderService.createOrder(data)
        .subscribe({
          next: () => this.router.navigate(['/orders'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
