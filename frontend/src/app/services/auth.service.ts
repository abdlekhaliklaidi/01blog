import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/users';
  private tokenKey = 'token';

  private loggedIn = new BehaviorSubject<boolean>(false);
  private adminStatus = new BehaviorSubject<boolean>(false);

  isAdmin$ = this.adminStatus.asObservable();

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (this.isBrowser()) {
      this.loggedIn.next(this.hasToken());
      this.adminStatus.next(this.isAdmin());
    }
  }

  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  private hasToken(): boolean {
    if (!this.isBrowser()) return false;
    return !!localStorage.getItem(this.tokenKey);
  }

  login(email: string, password: string): Observable<any> {
    return this.http
      .post<{ token: string }>(`${this.baseUrl}/login`, { email, password })
      .pipe(
        tap(res => {
          if (!this.isBrowser()) return;

          localStorage.setItem(this.tokenKey, res.token);
          const decoded: any = jwtDecode(res.token);

          this.adminStatus.next(decoded.role === 'ROLE_ADMIN');
          this.loggedIn.next(true);
        })
      );
  }

  logout(): void {
    if (this.isBrowser()) {
      localStorage.removeItem(this.tokenKey);
    }
    this.loggedIn.next(false);
    this.adminStatus.next(false);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  getToken(): string | null {
    if (!this.isBrowser()) return null;
    return localStorage.getItem(this.tokenKey);
  }

  isAdmin(): boolean {
    if (!this.isBrowser()) return false;

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
    if (!this.isBrowser()) return null;

    const token = this.getToken();
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.sub || decoded.email;
    } catch {
      return null;
    }
  }
}
