import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../services/notification.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationsComponent implements OnInit, OnDestroy {

  notifications: any[] = [];
  private timer: any;

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    const userId = Number(localStorage.getItem('userId'));

    if (userId) {
      this.loadNotifications(userId);

      this.timer = setInterval(() => {
        this.loadNotifications(userId);
      }, 5000);
    }
  }

  ngOnDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

  loadNotifications(userId: number) {
    this.notificationService.getUserNotifications(userId).subscribe({
      next: (data) => {
        this.notifications = data;
      }
    });
  }
}