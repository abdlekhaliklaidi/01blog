import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private http: HttpClient) {}

  onLogin() {
    if (!this.email || !this.password) {
      alert('Please enter email and password');
      return;
    }

    this.http.post('http://localhost:8080/users/login', { email: this.email, password: this.password }, { responseType: 'text' })
      .subscribe({
        next: (res) => alert(res),
        error: (err) => alert(err.error || 'Login failed')
      });
  }
}
