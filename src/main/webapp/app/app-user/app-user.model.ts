export class AppUserDTO {

  constructor(data:Partial<AppUserDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  username?: string|null;
  password?: string|null;
  fullname?: string|null;
  role?: string|null;

}
