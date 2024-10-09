export class OrderDTO {

  constructor(data:Partial<OrderDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  totalAmount?: number|null;
  status?: string|null;
  stripePaymentIntentId?: string|null;
  appUser?: number|null;

}


export class CreateOrderDTO {

  constructor(data:Partial<CreateOrderDTO>) {
    Object.assign(this, data);
  }

  shippingAddress?: string|null;
  shippingCity?: string|null;
  shippingState?: string|null;
  shippingZip?: string|null;
  shippingCountry?: string|null;
}
