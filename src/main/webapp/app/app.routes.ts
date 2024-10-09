import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AppUserListComponent } from './app-user/app-user-list.component';
import { AppUserAddComponent } from './app-user/app-user-add.component';
import { AppUserEditComponent } from './app-user/app-user-edit.component';
import { ProductListComponent } from './product/product-list.component';
import { ProductAddComponent } from './product/product-add.component';
import { ProductEditComponent } from './product/product-edit.component';
import { OrderListComponent } from './order/order-list.component';
import { PaymentListComponent } from './payment/payment-list.component';
import { PaymentAddComponent } from './payment/payment-add.component';
import { PaymentEditComponent } from './payment/payment-edit.component';
import { CartListComponent } from './cart/cart-list.component';
import { CartAddComponent } from './cart/cart-add.component';
import { CartEditComponent } from './cart/cart-edit.component';
import { AuthenticationComponent } from './security/authentication.component';
import { RegistrationComponent } from './security/registration.component';
import { ErrorComponent } from './error/error.component';
import { AuthenticationService, CUSTOMER, VENDOR, ADMIN } from 'app/security/authentication.service';
import {OrderAddComponent} from "./order/order-add.component";
import {ShopComponent} from "./shop/shop.component";
import {ProductMainViewComponent} from "./product/product-main-view.component";


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path:'shop',
    component: ShopComponent,
    title: $localize`:@@shop.index.headline:Shop`
  },
  {
    path: 'appUsers',
    component: AppUserListComponent,
    title: $localize`:@@appUser.list.headline:App Users`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'appUsers/add',
    component: AppUserAddComponent,
    title: $localize`:@@appUser.add.headline:Add App User`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'appUsers/edit/:id',
    component: AppUserEditComponent,
    title: $localize`:@@appUser.edit.headline:Edit App User`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'products',
    component: ProductListComponent,
    title: $localize`:@@product.list.headline:Products`,
    data: {
      roles: [CUSTOMER, VENDOR, ADMIN]
    }
  },
  {
    path: 'products/add',
    component: ProductAddComponent,
    title: $localize`:@@product.add.headline:Add Product`,
    data: {
      roles: [CUSTOMER, VENDOR, ADMIN]
    }
  },
  {
    path: 'products/:id',
    component: ProductEditComponent,
    title: $localize`:@@product.edit.headline:Edit Product`,
  },
  {
    path: 'products/view/:id',
    component: ProductMainViewComponent,
    title: $localize`:@@product.edit.headline:View Product`
  },
  {
    path: 'orders',
    component: OrderListComponent,
    title: $localize`:@@order.list.headline:Orders`,
    data: {
      roles: [CUSTOMER, VENDOR, ADMIN]
    }
  },
  {
    path: 'orders/create',
    component: OrderAddComponent,
    title: $localize`:@@order.add.headline:Add Order`,
    data: {
      roles: [CUSTOMER, VENDOR, ADMIN]
    }
  },
  {
    path: 'payments',
    component: PaymentListComponent,
    title: $localize`:@@payment.list.headline:Payments`,
    data: {
      roles: [CUSTOMER, VENDOR, ADMIN]
    }
  },
  {
    path: 'payments/add',
    component: PaymentAddComponent,
    title: $localize`:@@payment.add.headline:Add Payment`,
    data: {
      roles: [CUSTOMER, VENDOR, ADMIN]
    }
  },
  {
    path: 'payments/edit/:id',
    component: PaymentEditComponent,
    title: $localize`:@@payment.edit.headline:Edit Payment`,
    data: {
      roles: [CUSTOMER, VENDOR, ADMIN]
    }
  },
  {
    path: 'carts',
    component: CartListComponent,
    title: $localize`:@@cart.list.headline:Carts`,
    data: {
      roles: [CUSTOMER, ADMIN]
    }
  },
  {
    path: 'carts/add',
    component: CartAddComponent,
    title: $localize`:@@cart.add.headline:Add Cart`,
    data: {
      roles: [CUSTOMER, ADMIN]
    }
  },
  {
    path: 'carts/edit/:id',
    component: CartEditComponent,
    title: $localize`:@@cart.edit.headline:Edit Cart`,
    data: {
      roles: [CUSTOMER, ADMIN]
    }
  },
  {
    path: 'login',
    component: AuthenticationComponent,
    title: $localize`:@@authentication.login.headline:Login`
  },
  {
    path: 'register',
    component: RegistrationComponent,
    title: $localize`:@@registration.register.headline:Registration`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];

// add authentication check to all routes
for (const route of routes) {
  route.canActivate = [(route: ActivatedRouteSnapshot) => inject(AuthenticationService).checkAccessAllowed(route)];
}
