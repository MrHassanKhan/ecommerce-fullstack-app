import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CartService } from 'app/cart/cart.service';
import { CartDTO } from 'app/cart/cart.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validDouble } from 'app/common/utils';


@Component({
  selector: 'app-cart-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './cart-edit.component.html'
})
export class CartEditComponent implements OnInit {

  cartService = inject(CartService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  appUserValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    totalAmount: new FormControl(null, [validDouble]),
    appUser: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@cart.update.success:Cart was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    // this.cartService.getAppUserValues()
    //     .subscribe({
    //       next: (data) => this.appUserValues = data,
    //       error: (error) => this.errorHandler.handleServerError(error.error)
    //     });
    // this.cartService.getCart(this.currentId!)
    //     .subscribe({
    //       next: (data) => updateForm(this.editForm, data),
    //       error: (error) => this.errorHandler.handleServerError(error.error)
    //     });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new CartDTO(this.editForm.value);
    // this.cartService.updateCart(this.currentId!, data)
    //     .subscribe({
    //       next: () => this.router.navigate(['/carts'], {
    //         state: {
    //           msgSuccess: this.getMessage('updated')
    //         }
    //       }),
    //       error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
    //     });
  }

}
