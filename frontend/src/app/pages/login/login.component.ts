import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = {
    email: '',
    password: ''
  };

  successMessage: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient) {}

  onLogin() {
    const { email, password } = this.credentials;

    if (!email || !password) {
      this.errorMessage = 'Veuillez entrer un email et un mot de passe';
      this.successMessage = '';
      return;
    }

    this.http.post('http://localhost:8080/users/login', { email, password }, { responseType: 'text' })
      .subscribe({
        next: (res) => {
          this.successMessage = res;
          this.errorMessage = '';
        },
        error: (err) => {
          this.errorMessage = err.error || 'Échec de la connexion';
          this.successMessage = '';
        }
      });
  }
}
