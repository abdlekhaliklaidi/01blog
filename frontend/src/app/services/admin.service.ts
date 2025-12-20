import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private api = 'http://localhost:8080/admin';

  constructor(private http: HttpClient) {}

  // deletePost(postId: number) {
  //   return this.http.delete(`${this.api}/delete-post/${postId}`);
  // }
  
  deletePost(postId: number) {
    return this.http.delete(`http://localhost:8080/admin/delete-post/${postId}`);
  }

  banUser(userId: number) {
    return this.http.post(`${this.api}/ban-user/${userId}`, {});
  }
}
