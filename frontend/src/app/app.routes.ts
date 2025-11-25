import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
  },
  { 
    path: 'home', 
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
    canActivate: [authGuard]
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
  path: 'notifications',
  loadComponent: () => import('./pages/notification/notification.component').then(m => m.NotificationsComponent),
  canActivate: [authGuard]
  },
  {
  path: 'reports/user/:userId',
  loadComponent: () => import('./pages/report/reports-list.component')
    .then(m => m.ReportsListComponent),
  canActivate: [authGuard]
},
{
  path: 'reports/post/:postId',
  loadComponent: () => import('./pages/report/reports-list.component')
    .then(m => m.ReportsListComponent),
  canActivate: [authGuard]
},
{
  path: 'report-details/:id',
  loadComponent: () => import('./pages/report/report-details.component')
    .then(m => m.ReportDetailsComponent),
  canActivate: [authGuard]
},
{
  path: 'admin/reports-admin',
  loadComponent: () =>
    import('./pages/admin/reports-admin.component')
      .then(m => m.ReportsAdminComponent),
  canActivate: [authGuard]
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}