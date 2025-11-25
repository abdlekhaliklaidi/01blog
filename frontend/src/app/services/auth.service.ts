import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/users';
  private tokenKey = 'token';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private http: HttpClient) { }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<{token: string}>(`${this.baseUrl}/login`, { email, password })
      .pipe(
        tap(res => {
          localStorage.setItem(this.tokenKey, res.token);
          const decoded: any = jwtDecode(res.token);
          this.adminStatus.next(decoded.role === 'ROLE_ADMIN');
          this.loggedIn.next(true);
        })
      );
  }
  private adminStatus = new BehaviorSubject<boolean>(false);
  isAdmin$ = this.adminStatus.asObservable();

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.loggedIn.next(false);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAdmin(): boolean {
  const token = this.getToken();
  if (!token) return false;

  try {
    const decoded: any = jwtDecode(token);
    console.log("Decoded role:", decoded.role);
    return decoded.role === 'ROLE_ADMIN';
  } catch {
    return false;
  }
}

  getCurrentUserEmail(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const decodedToken: any = jwtDecode(token);
      return decodedToken.sub || decodedToken.email;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }
}