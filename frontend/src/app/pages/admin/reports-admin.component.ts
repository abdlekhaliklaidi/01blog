import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportService } from '../../services/report.service';
import { AuthService } from '../../services/auth.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-reports-admin',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './reports-admin.component.html',
  styleUrls: ['./reports-admin.component.css']
})
export class ReportsAdminComponent implements OnInit {

  postReports: any[] = [];
  userReports: any[] = [];
  isLoading = true;

  constructor(
    private http: HttpClient,
    private reportService: ReportService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadReports();
  }

  loadReports() {
    this.http.get('http://localhost:8080/report/posts').subscribe({
      next: (data: any) => {
        this.postReports = data;
        this.checkLoading();
      },
      error: (err) => {
        console.error('Error loading post reports:', err);
        this.checkLoading();
      }
    });

    this.http.get('http://localhost:8080/report/users').subscribe({
      next: (data: any) => {
        this.userReports = data;
        this.checkLoading();
      },
      error: (err) => {
        console.error('Error loading user reports:', err);
        this.checkLoading();
      }
    });
  }

  private checkLoading() {
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);
  }

  deleteReport(id: number) {
    if (confirm('Are you sure you want to delete this report?')) {
      this.http.delete(`http://localhost:8080/report/${id}`).subscribe({
        next: () => {
          this.postReports = this.postReports.filter(r => r.id !== id);
          this.userReports = this.userReports.filter(r => r.id !== id);
          alert('Report deleted successfully');
        },
        error: (err) => {
          console.error('Error deleting report:', err);
          alert('Error deleting report');
        }
      });
    }
  }

  takeAction(report: any) {
    alert(`Taking action on report ${report.id}\nReason: ${report.reason}`);
  }
}