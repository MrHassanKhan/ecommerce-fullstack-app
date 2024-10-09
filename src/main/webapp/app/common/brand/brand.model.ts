export class BrandWithProductCountDTO {
  constructor(data:Partial<BrandWithProductCountDTO>) {
    Object.assign(this, data);
  }
  id?: number;
  name?: string;
  productCount?: number;
}
