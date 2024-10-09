export  class CategoryProductCountDTO {
  constructor(data:Partial<CategoryProductCountDTO>) {
    Object.assign(this, data);
  }
  id?: number;
  name?: string;
  productCount?: number;
}
