import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { AppUserService } from 'app/app-user/app-user.service';
import { AppUserDTO } from 'app/app-user/app-user.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-app-user-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './app-user-add.component.html'
})
export class AppUserAddComponent {

  appUserService = inject(AppUserService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    username: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    fullname: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    role: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@appUser.create.success:App User was created successfully.`,
      APP_USER_USERNAME_UNIQUE: $localize`:@@Exists.appUser.username:This Username is already taken.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new AppUserDTO(this.addForm.value);
    this.appUserService.createAppUser(data)
        .subscribe({
          next: () => this.router.navigate(['/appUsers'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
