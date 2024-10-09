export class ProductDTO {

  constructor(data:Partial<ProductDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;
  description?: string|null;
  price?: number|null;
  stock?: number|null;
  addedby?: string|null;
  imageUrl?: |null;
  brandId?: number|null;
  categoryId?: number|null;
  brandName?: string|null;
  categoryName?: string|null;
  totalReviews?: number|null;
  averageRating?: number|null;
}
