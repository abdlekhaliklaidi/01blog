import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
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

  constructor(private http: HttpClient, private router: Router) {}

  onLogin() {
    const { email, password } = this.credentials;

    if (!email || !password) {
      this.errorMessage = 'Veuillez entrer un email et un mot de passe';
      this.successMessage = '';
      return;
    }

    this.http.post<{ token: string }>(
      'http://localhost:8080/users/login',
      { email, password }
    ).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);

        this.successMessage = 'Connexion réussie!';
        this.errorMessage = '';

        this.getUsersList();

        this.router.navigate(['/home']);
      },
      error: (err) => {
        if (err.status === 401) {
          this.errorMessage = 'Email ou mot de passe incorrect';
        } else if (err.status === 403) {
          this.errorMessage = 'Accès interdit';
        } else {
          this.errorMessage = 'Erreur serveur, veuillez réessayer';
        }
        this.successMessage = '';
      }
    });
  }

  getUsersList() {
    const token = localStorage.getItem('token');
    if (!token) {
      console.warn('Aucun token trouvé.');
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    this.http.get('http://localhost:8080/users', { headers })
      .subscribe({
        next: data => {
          console.log('Liste des utilisateurs:', data);
        },
        error: err => {
          console.error('Erreur lors du chargement des utilisateurs:', err);
        }
      });
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
