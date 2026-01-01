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
  
  // deletePost(postId: number) {
  //   return this.http.delete(`http://localhost:8080/admin/delete-post/${postId}`);
  // }

  deletePost(postId: number) {
  return this.http.delete(`http://localhost:8080/admin/posts/${postId}`);
  }

  banUser(userId: number) {
    return this.http.post(`${this.api}/ban-user/${userId}`, {});
  }

  unbanUser(userId: number) {
  return this.http.post(`http://localhost:8080/admin/unban-user/${userId}`, {});
  }
  
  getAllUsers() {
  return this.http.get<any[]>('http://localhost:8080/admin/users');
  }

  getAllPosts() {
  return this.http.get<any[]>('http://localhost:8080/admin/posts');
  }

  hidePost(postId: number) {
  return this.http.post(`http://localhost:8080/admin/hide-post/${postId}`, {});
  }

  unhidePost(postId: number) {
  return this.http.post(`http://localhost:8080/admin/unhide-post/${postId}`, {});
  }
  
  getPostById(id: number) {
  return this.http.get(`http://localhost:8080/admin/posts/${id}`);
  }
  
  getUsersPaginated(lastId: number = 0, limit: number = 10) {
  return this.http.get<any[]>(`${this.api}/users/paginated?lastId=${lastId}&limit=${limit}`);
  }

  getPostsPaginated(lastId: number = 0, limit: number = 10) {
  return this.http.get<any[]>(`${this.api}/posts/paginated?lastId=${lastId}&limit=${limit}`);
  }
  
  
}
