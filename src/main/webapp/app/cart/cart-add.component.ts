import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CartService } from 'app/cart/cart.service';
import { CartDTO } from 'app/cart/cart.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-cart-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './cart-add.component.html'
})
export class CartAddComponent implements OnInit {

  cartService = inject(CartService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  appUserValues?: Map<number,string>;

  addForm = new FormGroup({
    totalAmount: new FormControl(null, [validDouble]),
    appUser: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@cart.create.success:Cart was created successfully.`
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
    const data = new CartDTO(this.addForm.value);
    // this.cartService.createCart(data)
    //     .subscribe({
    //       next: () => this.router.navigate(['/carts'], {
    //         state: {
    //           msgSuccess: this.getMessage('created')
    //         }
    //       }),
    //       error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
    //     });
  }

}
