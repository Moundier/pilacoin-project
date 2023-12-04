import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Validators } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-page-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatInputModule, MatIconModule, MatButtonModule],
  templateUrl: './page-login.component.html',
  styleUrl: './page-login.component.css'
})
export class PageLoginComponent {

  email = new FormControl('', [Validators.required, Validators.email]);
  name = new FormControl('', [Validators.required, Validators.minLength(6)]);
  password = new FormControl('', [Validators.required, Validators.pattern(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/)]);
  confirmPassword = new FormControl('', [Validators.required]);

  constructor(private router: Router) {

  }

  getErrorMessage(field: string): string {

    if (field === 'email' && this.email.hasError('required')) 
      return 'You must enter a value';
    
    if (field === 'name' && this.name.hasError('minlength'))
      return 'Name must be more than 5 characters';
    
    if (field === 'password' && this.password.hasError('pattern'))
      return 'Password must contain at least 8 characters, including at least one letter and one number.';
    
    if (field === 'confirmPassword' && this.confirmPassword.hasError('required'))
      return 'Confirm Password is required';
    
    if (field === 'confirmPassword' && this.password.value !== this.confirmPassword.value)
      return 'Passwords do not match';

    return '';
  }

  handleButtonClick(): void {
    this.router.navigate([`tabs/tab-1`]);
  }
}
