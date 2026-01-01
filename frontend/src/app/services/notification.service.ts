import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private apiUrl = 'http://localhost:8080/notifications';

  constructor(private http: HttpClient) {}

  // getUserNotifications(userId: number): Observable<any[]> {
  //   return this.http.get<any[]>(`${this.apiUrl}/user/${userId}`);
  // }

  getNotifications(userId: number, page: number, size: number) {
    return this.http.get<any>(
      `${this.apiUrl}/user/${userId}?page=${page}&size=${size}`
    );
  }

  getLatestNotifications(userId: number, lastId: number) {
    return this.http.get<any[]>(
      `${this.apiUrl}/user/${userId}/latest?lastId=${lastId}`
    );
  }

  getUnreadCount(userId: number) {
  return this.http.get<number>(
    `http://localhost:8080/notifications/user/${userId}/unread/count`
  );
}
  
  markAsRead(notificationId: number) {
  return this.http.put(
    `${this.apiUrl}/${notificationId}/read`,
    {}
  );
}

  readAll(userId: number) {
  return this.http.put(
    `${this.apiUrl}/user/${userId}/read-all`,
    {}
  );
}
  
  markAsUnread(notificationId: number) {
  return this.http.put(`${this.apiUrl}/${notificationId}/unread`, {});
}

}
