import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  credentials = {
  firstname: '',
  lastname: '',
  genre: '',
  email: '',
  password: '',
  confirmPassword: ''
};

  successMessage: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient) {}

  onRegister() {
  const { firstname, lastname, genre, email, password, confirmPassword } = this.credentials;

  if (!firstname || !lastname || !genre || !email || !password || !confirmPassword) {
    this.errorMessage = 'Please fill in all fields';
    this.successMessage = '';
    return;
  }

  if (password !== confirmPassword) {
    this.errorMessage = 'Passwords do not match';
    this.successMessage = '';
    return;
  }

  this.http.post('http://localhost:8080/users/register', { firstname, lastname, genre, email, password }, { responseType: 'text' })
    .subscribe({
      next: (res) => {
        this.successMessage = res;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = err.error || 'Registration failed';
        this.successMessage = '';
      }
    });
  }
}
