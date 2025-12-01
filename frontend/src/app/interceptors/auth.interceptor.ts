import { HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  let cloned = req;
  if (token) {
    cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(cloned).pipe(
    catchError((err: any) => {
      if (err.status === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
      return throwError(() => err);
    })
  );
};


// import { HttpInterceptorFn } from '@angular/common/http';

// export const authInterceptor: HttpInterceptorFn = (req, next) => {
//   const token = localStorage.getItem('token');

//   if (token) {
//     const cloned = req.clone({
//       setHeaders: { Authorization: `Bearer ${token}` }
//     });
//     return next(cloned);
//   }

//   return next(req);
// };