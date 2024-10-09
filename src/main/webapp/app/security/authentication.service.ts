import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from 'environments/environment';
import { AuthenticationRequest, AuthenticationResponse, RegistrationRequest } from 'app/security/authentication.model';


export const CUSTOMER = 'CUSTOMER';
export const VENDOR = 'VENDOR';
export const ADMIN = 'ADMIN';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  http = inject(HttpClient);
  router = inject(Router);
  loginPath = environment.apiPath + '/authenticate';
  registerPath = environment.apiPath + '/register';

  loginSuccessUrl: string = '/';

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      loginRequired: $localize`:@@authentication.login.required:Please login to access this area.`,
      missingRole: $localize`:@@authentication.role.missing:You do not have sufficient rights to access this area.`,
      logoutSuccess: $localize`:@@authentication.logout.success:Your logout was successful.`
    };
    return messages[key];
  }

  checkAccessAllowed(route: ActivatedRouteSnapshot) {
    const roles = route.data['roles'];
    if (roles && !this.isLoggedIn()) {
      // show login page
      const targetUrl = this.router.getCurrentNavigation()?.finalUrl?.toString();
      if (targetUrl && targetUrl !== '/login' && targetUrl !== '/register') {
        this.loginSuccessUrl = targetUrl;
      }
      this.router.navigate(['/login'], {
            state: {
              msgInfo: this.getMessage('loginRequired')
            }
          });
      return false;
    } else if (roles && !this.hasAnyRole(roles)) {
      // show error page with message
      this.router.navigate(['/error'], {
            state: {
              errorStatus: '403',
              msgError: this.getMessage('missingRole')
            }
          });
      return false;
    }
    return true;
  }

  getLoginSuccessUrl() {
    return this.loginSuccessUrl;
  }

  login(authenticationRequest: AuthenticationRequest) {
    return this.http.post<AuthenticationResponse>(this.loginPath, authenticationRequest)
        .pipe(tap((data) => this.setSession(data)));
  }

  private setSession(authenticationResponse: AuthenticationResponse) {
    localStorage.setItem('access_token', authenticationResponse.accessToken!);
  }

  isLoggedIn() {
    // check token available
    if (!this.getToken()) {
      return false;
    }
    // check token not expired
    const tokenData = this.getTokenData();
    return Math.floor((new Date()).getTime() / 1000) < tokenData.exp;
  }

  hasAnyRole(requiredRoles: string[]) {
    const tokenData = this.getTokenData();
    if (!tokenData) {
      return false;
    }
    return requiredRoles.some((requiredRole) => tokenData.roles.includes(requiredRole));
  }


  hasRole(requiredRole: string) {
    const tokenData = this.getTokenData();
    return tokenData.roles.includes(requiredRole);
  }

  getToken() {
    return localStorage.getItem('access_token');
  }

  getTokenData() {
    const token = this.getToken()!!;
    if (!token) {
      return null;
    }
    return JSON.parse(atob(token.split('.')[1]));
  }

  logout() {
    if (this.isLoggedIn()) {
      this.loginSuccessUrl = '/';
    }
    localStorage.removeItem('access_token');
    this.router.navigate(['/login'], {
          state: {
            msgInfo: this.getMessage('logoutSuccess')
          }
        });
  }

  register(registrationRequest: RegistrationRequest) {
    return this.http.post(this.registerPath, registrationRequest);
  }

}
