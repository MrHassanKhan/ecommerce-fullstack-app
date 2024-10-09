import {Component, inject, Input} from "@angular/core";
import {CommonModule} from "@angular/common";
import {ProductDTO} from "../../../product/product.model";
import {CartService} from "../../../cart/cart.service";
import {ErrorHandler} from "../../error-handler.injectable";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-product-view1',
  standalone: true,
  imports: [CommonModule, RouterLink],
  styles: [``],
  template: `
    <div class="bg-white shadow rounded overflow-hidden group">
      <div class="relative">
        <img [src]="product?.imageUrl" alt="product 1" class="w-full max-h-[180px] h-[180px] object-cover transform duration-500 ease-in-out hover:scale-110">
        <div class="absolute inset-0 bg-black bg-opacity-40 flex items-center
                    justify-center gap-2 opacity-0 group-hover:opacity-100 transition">
          <a routerLink="/products/view/{{product?.id}}"
             class="text-white text-lg w-9 h-8 rounded-full bg-primary flex items-center justify-center hover:bg-gray-800 transition"
             title="view product">
            <i class="fa-solid fa-magnifying-glass"></i>
          </a>
          <a href="#"
             class="text-white text-lg w-9 h-8 rounded-full bg-primary flex items-center justify-center hover:bg-gray-800 transition"
             title="add to wishlist">
            <i class="fa-solid fa-heart"></i>
          </a>
        </div>
      </div>
      <div class="pt-4 pb-3 px-4">
        <a href="#">
          <h4 class="uppercase font-medium text-xl mb-2 text-gray-800 hover:text-primary transition">
            {{product?.name}}
          </h4>
        </a>
        <div class="flex items-baseline mb-1 space-x-2">
          <p class="text-xl text-primary font-semibold">{{product?.price}}</p>
          <p class="text-sm text-gray-400 line-through">{{((product?.price || 0) + 10)}}</p>
        </div>
        <div class="flex items-center">
          <div class="flex gap-1 text-sm text-yellow-400">
            <span><i class="fa-solid fa-star"></i></span>
            <span><i class="fa-solid fa-star"></i></span>
            <span><i class="fa-solid fa-star"></i></span>
            <span><i class="fa-solid fa-star"></i></span>
            <span><i class="fa-solid fa-star"></i></span>
          </div>
          <div class="text-xs text-gray-500 ml-3">(150)</div>
        </div>
      </div>
      <a (click)="addToCart(product)" href="javascript:void(0)"
         class="block w-full py-1 text-center text-white bg-primary border border-primary rounded-b hover:bg-transparent hover:text-primary transition">Add
        to cart</a>
    </div>
  `,
})
export class ProductView1Component {
  @Input({required: true}) product?: ProductDTO;

  cartService = inject(CartService);
  errorHandler = inject(ErrorHandler);

  addToCart(product?: ProductDTO){
    this.cartService.addToCart(product?.id, 1).subscribe({
      next: (data) => {
        console.log(data);
      },
      error: (error) => this.errorHandler.handleServerError(error.error)
    });
  }
}
