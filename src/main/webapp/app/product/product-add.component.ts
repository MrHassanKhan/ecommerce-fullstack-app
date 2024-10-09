import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProductService } from 'app/product/product.service';
import { ProductDTO } from 'app/product/product.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-product-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './product-add.component.html'
})
export class ProductAddComponent implements OnInit {

  productService = inject(ProductService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  categoryValues?: Map<number, string>;
  brandValues?: Map<number, string>;

  addForm = new FormGroup({
    name: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    description: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    price: new FormControl(null, [validDouble]),
    stock: new FormControl(null),
    file: new FormControl(null),
    categoryId: new FormControl(null, [Validators.required]),
    brandId: new FormControl(null, [Validators.required]),
  }, { updateOn: 'submit' });


  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@product.create.success:Product was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.productService.getCategoryValues()
        .subscribe({
          next: (data) => this.categoryValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.productService.getBrandValues()
        .subscribe({
          next: (data) => this.brandValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    // this.productService.getCartValues()
    //     .subscribe({
    //       next: (data) => this.cartValues = data,
    //       error: (error) => this.errorHandler.handleServerError(error.error)
    //     });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    // const data = new ProductDTO(this.addForm.value);
    const data = new FormData();
    data.append('file', this.addForm.get('file')?.value || '');
    let dto = this.addForm.value;
    delete dto.file;
    data.append('productDTO', JSON.stringify(dto));
    this.productService.createProduct(data)
        .subscribe({
          next: () => this.router.navigate(['/products'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
