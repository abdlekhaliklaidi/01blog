import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

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

  constructor(private http: HttpClient, private router: Router) {}

  onRegister() {
    const { firstname, lastname, genre, email, password, confirmPassword } = this.credentials;

    if (!firstname || !lastname || !genre || !email || !password || !confirmPassword) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      this.successMessage = '';
      return;
    }

    if (password !== confirmPassword) {
      this.errorMessage = 'Les mots de passe ne correspondent pas.';
      this.successMessage = '';
      return;
    }

    this.http.post('http://localhost:8080/users/register', 
      { firstname, lastname, genre, email, password },
      { responseType: 'text' }
    ).subscribe({
      next: (res) => {
        this.successMessage = 'Compte créé avec succès ! Redirection...';
        this.errorMessage = '';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.errorMessage = err.error || 'Échec de l’inscription.';
        this.successMessage = '';
      }
    });
  }
}