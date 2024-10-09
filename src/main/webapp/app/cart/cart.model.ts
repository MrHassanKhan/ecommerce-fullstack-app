export class CartDTO {

  constructor(data:Partial<CartDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  totalAmount?: number|null;
  userId?: number|null;
  cartItems?: CartItemDTO[];
}
export class CartItemDTO {
  constructor(data:Partial<CartDTO>) {
    Object.assign(this, data);
  }
  id?: number|null;
  price?: number|null;
  productId?: number|null;
  productName?: string|null;
  imageUrl?: string|null;
  quantity?: number|null;
}
