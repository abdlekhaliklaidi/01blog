import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportService } from '../../services/report.service';
import { AuthService } from '../../services/auth.service';
import { HttpClient} from '@angular/common/http';
import { AdminService } from '../../services/admin.service';
import { RouterModule } from '@angular/router';
import { PostModalComponent } from '../post-modal/post-modal.component';


@Component({
  selector: 'app-reports-admin',
  standalone: true,
  imports: [CommonModule, RouterModule, PostModalComponent],
  templateUrl: './reports-admin.component.html',
  styleUrls: ['./reports-admin.component.css']
})
export class ReportsAdminComponent implements OnInit {

  postReports: any[] = [];
  userReports: any[] = [];
  // showTable: 'post' | 'user' = 'post';
  isLoading = true;
  users: any[] = [];
  allPosts: any[] = [];
  showTable: 'post' | 'user' | 'posts' = 'posts';
  selectedPost: any = null;
  showPostModal = false;


  constructor(
    private http: HttpClient,
    private reportService: ReportService,
    private adminService: AdminService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.loadReports();
    this.loadUsers();
    if (this.showTable === 'posts') {
      this.loadAllPosts();
    }
  }
  
  loadUsers() {
  this.adminService.getAllUsers().subscribe({
    next: (data) => {
      this.users = data;
    },
    error: (err) => {
      console.error('Error loading users:', err);
      console.error('Status:', err.status);
      console.error('Message:', err.message);
    }
  });
}
  
  loadAllPosts() {
  this.adminService.getAllPosts().subscribe({
    next: (data) => this.allPosts = data,
    error: (err) => console.error('Error loading posts', err)
  });
}

  showPostsManagement() {
  this.showTable = 'posts';
  this.loadAllPosts();
}

  loadReports() {
    this.http.get('http://localhost:8080/reports/posts').subscribe({
      next: (data: any) => {
        this.postReports = data;
        this.checkLoading();
      },
      error: (err) => {
        console.error('Error loading post reports:', err);
        this.checkLoading();
      }
    });

    this.http.get('http://localhost:8080/reports/users').subscribe({
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
      this.http.delete(`http://localhost:8080/reports/${id}`).subscribe({
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

  deletePost(postId: number) {
  if (confirm("Are you sure you want to delete this post?")) {
    this.adminService.deletePost(postId).subscribe({
      next: () => {
        alert("Post deleted successfully");
        this.loadAllPosts();
      },
      error: (err) => {
        console.error(err);
        alert("Error deleting post");
      }
    });
  }
}

  banUser(userId: number) {
  if (confirm('Are you sure you want to ban this user?')) {
    this.adminService.banUser(userId).subscribe({
      next: () => {
        alert('User banned successfully');
        this.loadReports();
        // const user = this.userReports.find(u => u.reportedUserId === userId);
        // if (user) user.isBanned = true;
        // alert('User banned successfully');
      },
      error: (err) => {
        console.error(err);
        alert('Error banning user');
      }
    });
  }
}

  unbanUser(userId: number) {
  if (confirm('Êtes-vous sûr de vouloir débloquer cet utilisateur ?')) {
    this.adminService.unbanUser(userId).subscribe({
      next: () => {
        alert('Utilisateur débloqué avec succès');
        this.loadReports();
      },
      error: (err) => {
        console.error(err);
        alert('Erreur lors du déblocage');
      }
    });
  }
}

  hidePost(postId: number) {
  this.adminService.hidePost(postId).subscribe({
    next: () => {
      this.loadAllPosts();
    },
    error: (err) => {
      console.error('Error hiding post', err);
    }
  });
}

  unhidePost(postId: number) {
  this.adminService.unhidePost(postId).subscribe({
    next: () => {
      this.loadAllPosts();
    },
    error: (err) => {
      console.error('Error unhiding post', err);
    }
  });
}
   
  showPost(postId: number) {
  this.adminService.getPostById(postId).subscribe({
    next: (post) => {
      this.selectedPost = post;
      this.showPostModal = true;
    },
    error: () => {
      alert('Post not found');
    }
  });
}


closePostModal() {
  this.showPostModal = false;
  this.selectedPost = null;
}

  takeAction(report: any) {
    alert(`Taking action on report ${report.id}\nReason: ${report.reason}`);
  }
}