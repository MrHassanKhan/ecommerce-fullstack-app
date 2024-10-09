export class ReviewDto {

  constructor(data: Partial<ReviewDto>) {
    Object.assign(this, data);
  }
  id?: number;
  rating?: number;
  comment?: string;
  productId?: number;
  userId?: number;
  userName?: string;
}
