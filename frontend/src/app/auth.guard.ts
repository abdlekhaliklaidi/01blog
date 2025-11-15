import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  // prevent localStorage access on the server (SSR)
  if (typeof window === 'undefined') {
    return false;
  }

  const token = localStorage.getItem('token');

  if (token) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};