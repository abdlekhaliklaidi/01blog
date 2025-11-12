import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private readonly baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllPosts(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/posts`);
  }

  getPostsByAuthor(authorId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/posts/author/${authorId}`);
  }

  createPost(formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/posts`, formData);
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/posts/${id}`);
  }

  toggleLike(userId: number, postId: number): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/likes/toggle?userId=${userId}&postId=${postId}`, {});
  }

  addComment(postId: number, userId: number, content: string): Observable<any> {
    const body = {
      content,
      post: { id: postId },
      user: { id: userId }
    };
    return this.http.post<any>(`${this.baseUrl}/comments`, body);
  }

  getCommentsByPost(postId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/comments/post/${postId}`);
  }
}



// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';

// @Injectable({
//   providedIn: 'root'
// })
// export class PostService {

//   private apiUrl = 'http://localhost:8080/posts';

//   constructor(private http: HttpClient) {}

//   getAllPosts(): Observable<any[]> {
//     return this.http.get<any[]>(this.apiUrl);
//   }

//   getPostsByAuthor(authorId: number): Observable<any[]> {
//     return this.http.get<any[]>(`${this.apiUrl}/author/${authorId}`);
//   }

//   // createPost(postData: any): Observable<any> {
//   //   return this.http.post<any>(this.apiUrl, postData);
//   // }

//   createPost(formData: FormData) {
//     return this.http.post<any>(this.apiUrl, formData);
//   }

//   deletePost(id: number): Observable<void> {
//     return this.http.delete<void>(`${this.apiUrl}/${id}`);
//   }
// }
