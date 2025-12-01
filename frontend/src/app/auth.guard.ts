import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const publicGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  if (typeof window === 'undefined') return false;

  const token = localStorage.getItem('token');

  if (token) {
    router.navigate(['/home'], { replaceUrl: true });
    return false;
  }

  return true;
};

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  if (typeof window === 'undefined') return false;

  const token = localStorage.getItem('token');

  if (token) return true;

  router.navigate(['/login'], { replaceUrl: true });
  return false;
};
