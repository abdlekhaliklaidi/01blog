import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  firstname?: string;
  lastname?: string;
  name?: string;
  avatar: string;
  bio?: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = 'http://localhost:8080/users';
  private followersApi = 'http://localhost:8080/followers';

  constructor(private http: HttpClient) {}

  getUserById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
  
  getAllUsers() {
    return this.http.get<any[]>(`${this.apiUrl}`);
  }
  
  getMe(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me`);
  }

  getFollowers(userId: number) {
    return this.http.get<any[]>(`${this.followersApi}/followers/${userId}`);
  }

  getFollowing(userId: number) {
  return this.http.get<any[]>(`${this.followersApi}/following/${userId}`);
  }

  followUser(followerId: number, followingId: number): Observable<any> {
    const body = { follower: { id: followerId }, following: { id: followingId } };
    return this.http.post(`http://localhost:8080/followers`, body);
  }

  unfollowUser(followerId: number, followingId: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/followers/${followerId}/${followingId}`);
  }
}
