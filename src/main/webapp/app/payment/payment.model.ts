export class PaymentDTO {

  constructor(data:Partial<PaymentDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  amount?: number|null;
  status?: string|null;
  appUser?: number|null;
  order?: number|null;

}
