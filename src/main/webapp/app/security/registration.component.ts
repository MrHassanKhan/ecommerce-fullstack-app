import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { AuthenticationService } from 'app/security/authentication.service';
import { RegistrationRequest } from 'app/security/authentication.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './registration.component.html'
})
export class RegistrationComponent {

  authenticationService = inject(AuthenticationService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  registrationForm = new FormGroup({
    username: new FormControl("admin", [Validators.required, Validators.maxLength(255)]),
    password: new FormControl("12345", [Validators.required, Validators.maxLength(255)]),
    fullname: new FormControl("admin admin", [Validators.required, Validators.maxLength(255)]),
    role: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      registrationSuccess: $localize`:@@registration.register.success:Your registration is complete. You may now login.`,
      APP_USER_USERNAME_UNIQUE: $localize`:@@registration.register.taken:This account is already taken. Please login.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.registrationForm.markAllAsTouched();
    if (!this.registrationForm.valid) {
      return;
    }
    const data = new RegistrationRequest(this.registrationForm.value);
    // data.role = 'ADMIN';
    this.authenticationService.register(data)
        .subscribe({
          next: () => this.router.navigate(['/login'], {
            state: {
              msgSuccess: this.getMessage('registrationSuccess')
            }
          }),
          // error: (error) => this.errorHandler.handleServerError(error.error, this.registrationForm, this.getMessage)
        });
  }

}
