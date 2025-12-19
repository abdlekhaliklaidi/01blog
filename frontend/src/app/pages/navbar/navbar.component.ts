import { Component, OnInit, HostListener, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  showUserMenu = false;
  isDarkMode = false;
  userEmail: string | null = null;
  userInfo: any;
  private isBrowser = false;

  constructor(
    public auth: AuthService,
    private userService: UserService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngOnInit(): void {
    if (!this.isBrowser) return;

    // this.userEmail = this.auth.getCurrentUserEmail();
     this.userService.getMe().subscribe({
      next: (user) => {
        this.userInfo = {
          id: user.id,
          name: user.firstname + ' ' + user.lastname
        };
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    });
    this.loadTheme();
}

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  @HostListener('document:click', ['$event'])
  closeMenu(event: MouseEvent): void {
    if (!this.isBrowser) return;

    const target = event.target as HTMLElement;
    if (!target.closest('.nav-right')) {
      this.showUserMenu = false;
    }
  }

  toggleTheme(): void {
    if (!this.isBrowser) return;

    this.isDarkMode = !this.isDarkMode;
    document.body.classList.toggle('dark-mode', this.isDarkMode);
    localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light');
  }

  loadTheme(): void {
    if (!this.isBrowser) return;

    const savedTheme = localStorage.getItem('theme');
    this.isDarkMode = savedTheme === 'dark';
    document.body.classList.toggle('dark-mode', this.isDarkMode);
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
