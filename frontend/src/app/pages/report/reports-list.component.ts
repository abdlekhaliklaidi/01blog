import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../services/report.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports-list.component.html',
  styleUrls: ['./reports-list.component.css']
})
export class ReportsListComponent implements OnInit {

  reports: any[] = [];
  userId!: number;
  postId!: number;

  constructor(
    private reportService: ReportService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {

    this.userId = Number(this.route.snapshot.paramMap.get('userId'));
    this.postId = Number(this.route.snapshot.paramMap.get('postId'));

    if (this.userId) {
      this.reportService.getReportsByUser(this.userId).subscribe(data => {
        this.reports = data;
      });
    }

    if (this.postId) {
      this.reportService.getReportsForPost(this.postId).subscribe(data => {
        this.reports = data;
      });
    }
  }

  openDetails(report: any) {
    this.router.navigate(['/report-details', report.id]);
  }
}
