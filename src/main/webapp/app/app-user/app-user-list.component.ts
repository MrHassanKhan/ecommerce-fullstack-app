import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { AppUserService } from 'app/app-user/app-user.service';
import { AppUserDTO } from 'app/app-user/app-user.model';


@Component({
  selector: 'app-app-user-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './app-user-list.component.html'})
export class AppUserListComponent implements OnInit, OnDestroy {

  appUserService = inject(AppUserService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  appUsers?: AppUserDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@appUser.delete.success:App User was removed successfully.`,
      'appUser.product.addedby.referenced': $localize`:@@appUser.product.addedby.referenced:This entity is still referenced by Product ${details?.id} via field Addedby.`,
      'appUser.order.appUser.referenced': $localize`:@@appUser.order.appUser.referenced:This entity is still referenced by Order ${details?.id} via field App User.`,
      'appUser.payment.appUser.referenced': $localize`:@@appUser.payment.appUser.referenced:This entity is still referenced by Payment ${details?.id} via field App User.`,
      'appUser.cart.appUser.referenced': $localize`:@@appUser.cart.appUser.referenced:This entity is still referenced by Cart ${details?.id} via field App User.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.appUserService.getAllAppUsers()
        .subscribe({
          next: (data) => this.appUsers = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.appUserService.deleteAppUser(id)
          .subscribe({
            next: () => this.router.navigate(['/appUsers'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/appUsers'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }

}
