import { HttpInterceptorFn } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  let cloned = req;
  if (token) {
    cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(cloned).pipe(
    catchError((err) => {
      if (err.status === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login';
      } else if (err.status === 403 && err.error === 'User is banned') {
        // User banned
        alert('Your account has been banned. You will be redirected to login.');
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
      return throwError(() => err);
    })
  );
};