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
  unreadCount = 0;
  userId!: number;

  private timer: any;

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.userId = Number(localStorage.getItem('userId'));

    if (!this.userId) return;

    this.loadNotifications();
    this.loadUnreadCount();

    this.timer = setInterval(() => {
      this.loadLatest();
      this.loadUnreadCount();
    }, 5000);
  }

//    ngOnInit() {
//   this.userId = Number(localStorage.getItem('userId'));

//   if (this.userId) {
//     this.loadNotifications();
//     this.initSSE();
//   }
// }

// initSSE() {
//   const eventSource = new EventSource(`http://localhost:8080/notifications/user/${this.userId}/stream`);
  
//   eventSource.onmessage = (event) => {
//     const notification = JSON.parse(event.data);
//     this.notifications = [notification, ...this.notifications];
//     this.lastNotificationId = notification.id;
//   };

//   eventSource.onerror = () => {
//     console.error('SSE connection error');
//     eventSource.close();
//   };
// }

   ngOnDestroy() {
    clearInterval(this.timer);
  }

  loadNotifications() {
    this.notificationService
      .getNotifications(this.userId, this.page, this.size)
      .subscribe(res => {
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
        this.notifications = [...this.notifications, ...res.content];
      });
  }

  loadUnreadCount() {
    this.notificationService
      .getUnreadCount(this.userId)
      .subscribe(count => {
        this.unreadCount = count;
      });
  }

  markAsRead(notification: any) {
    if (notification.read) return;

    this.notificationService.markAsRead(notification.id).subscribe((res: any) => {
      notification.read = true;
      // this.unreadCount--;
      this.unreadCount = res.unreadCount;
    });
  }
  
  markAsUnread(notification: any) {
  if (!notification.read) return;

  this.notificationService.markAsUnread(notification.id).subscribe((res: any) => {
    notification.read = false;
    this.unreadCount = res.unreadCount; 
  });
}

  readAll() {
    this.notificationService.readAll(this.userId).subscribe((res: any) => {
      this.notifications.forEach(n => n.read = true);
      // this.unreadCount = 0;
      this.unreadCount = res.unreadCount;
    });
  }
}