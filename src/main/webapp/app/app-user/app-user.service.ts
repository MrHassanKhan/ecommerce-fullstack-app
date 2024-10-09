import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { AppUserDTO } from 'app/app-user/app-user.model';


@Injectable({
  providedIn: 'root',
})
export class AppUserService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/appUsers';

  getAllAppUsers() {
    return this.http.get<AppUserDTO[]>(this.resourcePath);
  }

  getAppUser(id: number) {
    return this.http.get<AppUserDTO>(this.resourcePath + '/' + id);
  }

  createAppUser(appUserDTO: AppUserDTO) {
    return this.http.post<number>(this.resourcePath, appUserDTO);
  }

  updateAppUser(id: number, appUserDTO: AppUserDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, appUserDTO);
  }

  deleteAppUser(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
