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
  page = 0;
  size = 6;
  lastNotificationId = 0;
  private timer: any;
  userId!: number;

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
  this.userId = Number(localStorage.getItem('userId'));

  if (this.userId) {
    this.loadNotifications();

    this.timer = setInterval(() => {
      this.loadLatest();
    }, 5000);
  }
}

  ngOnDestroy() {
  clearInterval(this.timer);
}

  loadNotifications() {
  this.notificationService
    .getNotifications(this.userId, this.page, this.size)
    .subscribe(res => {
      console.log('Page:', res.number);
      console.log('Notifications:', res.content.length);
      this.notifications = res.content;

      if (this.notifications.length > 0) {
        this.lastNotificationId = this.notifications[0].id;
      }
    });
}
  loadLatest() {
  if (!this.lastNotificationId) return;

  this.notificationService
    .getLatestNotifications(this.userId, this.lastNotificationId)
    .subscribe(data => {
      console.log('New notifications:', data.length);

      if (data.length > 0) {
        this.notifications = [...data, ...this.notifications];
        this.lastNotificationId = data[0].id;
      }
    });
  }

  loadMore() {
  this.page++;
  this.notificationService
    .getNotifications(this.userId, this.page, this.size)
    .subscribe(res => {
      this.notifications = [
        ...this.notifications,
        ...res.content
      ];
    });
  }
}