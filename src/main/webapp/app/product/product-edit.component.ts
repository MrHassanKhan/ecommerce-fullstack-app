import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProductService } from 'app/product/product.service';
import { ProductDTO } from 'app/product/product.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validDouble } from 'app/common/utils';
import {FileService} from "../common/services/file.service";


@Component({
  selector: 'app-product-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './product-edit.component.html'
})
export class ProductEditComponent implements OnInit {

  productService = inject(ProductService);
  fileService = inject(FileService)
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);


  currentId?: number;
  categoryValues?: Map<number, string>;
  brandValues?: Map<number, string>;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    name: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    description: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    price: new FormControl(null, [validDouble]),
    stock: new FormControl(null),
    file: new FormControl<any>(null),
    categoryId: new FormControl(null, [Validators.required]),
    brandId: new FormControl(null, [Validators.required]),
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@product.update.success:Product was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
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
    this.productService.getProduct(this.currentId!)
        .subscribe({
          next: (data) => {
            updateForm(this.editForm, data);
            if(data.imageUrl) {
              this.serveFile(data.imageUrl);
            }
          },
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  serveFile(imageUrl: string) {
    this.fileService.serveFile(imageUrl).subscribe({
      next: (data) => {
        const file = new File([data], imageUrl);
        this.editForm.get('file')?.setValue(file || null);
      },
      error: (error) => this.errorHandler.handleServerError(error.error)
    });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    // const data = new ProductDTO(this.editForm.value);
    const data = new FormData();
    data.append('file', this.editForm.get('file')?.value || '');
    let dto = this.editForm.value;
    delete dto.file;
    data.append('productDTO', JSON.stringify(dto));
    this.productService.updateProduct(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/products'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
