// import { Component } from '@angular/core';
// import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
// import { NavbarComponent } from './pages/navbar/navbar.component';
// import { CommonModule } from '@angular/common';
// import { filter } from 'rxjs/operators';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [RouterOutlet, NavbarComponent, CommonModule],
//   template: `
//     <app-navbar></app-navbar>
//     <router-outlet></router-outlet>
//   `
// })
// export class AppComponent {
//   title = 'frontend';
//   showNavbar = true;

//   constructor(private router: Router) {
//     this.router.events
//       .pipe(filter(event => event instanceof NavigationEnd))
//       .subscribe((event: any) => {
//         const hiddenRoutes = ['/login', '/register'];
//         this.showNavbar = !hiddenRoutes.includes(event.urlAfterRedirects);
//       });
//   }
// }

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `<router-outlet></router-outlet>`
})
export class AppComponent {
  title = 'frontend';
}