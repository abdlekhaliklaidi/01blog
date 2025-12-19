import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private baseUrl = 'http://localhost:8080/reports';

  constructor(private http: HttpClient) {}

  getReportsByUser(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`);
  }

  getReportsForPost(postId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/post/${postId}`);
  }
  
  getAllReports(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}`);
  }

  deleteReport(id: number): Observable<void> {
  return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  createReport(report: any): Observable<any> {
    console.log(report);
    
    return this.http.post<any>(`${this.baseUrl}/post/${report.post.id}`, report);
  }
  
  reportUser(userId: number, report: any) {
  return this.http.post(`http://localhost:8080/reports/user/${userId}`, report);
 }

}
