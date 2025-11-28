import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PostService } from '../../services/post.service';
import { ReportService } from '../../services/report.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts: any[] = [];
  followers: any[] = [];
  following: any[] = [];
  userInfo: any;
  showUserMenu = false;
  showCreatePost = false;

  newPost = { title: '', content: '' };
  selectedFile: File | null = null;

  showReportModal = false;
  reportReason = '';
  selectedPostId: number | null = null;

  constructor(
    private router: Router,
    private postService: PostService,
    private reportService: ReportService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    this.userInfo = {
      id: 1,
      name: 'Ali Student',
      bio: 'Full Stack Developer | Angular & Spring Boot',
      avatar: 'https://i.pravatar.cc/100?img=12'
    };
    
    // this.followers = [
    //   { id: 1, name: 'Amina Dev', avatar: 'https://i.pravatar.cc/40?img=1', status: 'pending' },
    //   { id: 2, name: 'Youssef Code', avatar: 'https://i.pravatar.cc/40?img=2', status: 'pending' },
    //   { id: 3, name: 'Hassan UI', avatar: 'https://i.pravatar.cc/40?img=3', status: 'accepted' }
    // ];

    // this.following = [
    //   { id: 1, name: 'Sara Dev', avatar: 'https://i.pravatar.cc/40?img=4', following: true },
    //   { id: 2, name: 'Omar JS', avatar: 'https://i.pravatar.cc/40?img=5', following: false }
    // ];

    this.getFollowers(this.userInfo.id);
    this.getFollowing(this.userInfo.id);

    this.loadPosts();
  }

  loadPosts() {
    this.postService.getAllPosts().subscribe({
      next: (data) => {
        this.posts = (data || []).map((p: any) => ({
          ...p,
          likes: p.likes || [],
          comments: p.comments || [],
          showComments: false,
          newComment: ''
        }));
      },
      error: (err) => {
        console.error('Error loading posts:', err);
        this.posts = [];
      }
    });
  }

  toggleLike(post: any) {
    this.postService.toggleLike(post.id).subscribe({
      next: (count: number) => {
        post.likesCount = count;
        post.likedByCurrentUser = !post.likedByCurrentUser;
      },
      error: (err) => console.error('Error toggling like', err)
    });
  }

  addComment(post: any) {
    if (!post.newComment?.trim()) return;

    this.postService.addComment(post.id, this.userInfo.id, post.newComment).subscribe({
      next: (comment) => {
        post.comments.push(comment);
        post.newComment = '';
      },
      error: (err) => console.error('Error adding comment', err)
    });
  }

  openCreatePost() {
    this.showCreatePost = true;
  }

  closeCreatePost() {
    this.showCreatePost = false;
    this.newPost = { title: '', content: '' };
    this.selectedFile = null;
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  createPost() {
    if (!this.newPost.title.trim() || !this.newPost.content.trim()) {
      alert('Please enter title and content');
      return;
    }

    const formData = new FormData();
    formData.append('title', this.newPost.title.trim());
    formData.append('content', this.newPost.content.trim());

    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    this.postService.createPost(formData).subscribe({
      next: (created) => {
        this.posts.unshift({
          ...created,
          likes: created.likes || [],
          comments: created.comments || [],
          showComments: false,
          newComment: '',
          imageUrl: created.imageUrl
        });
        this.closeCreatePost();
      },
      error: (err) => console.error('Error creating post', err)
    });
  }

  getFollowers(userId: number) {
    this.postService.getFollowers(userId).subscribe({
      next: (data) => this.followers = data,
      error: (err) => console.error('Error fetching followers:', err)
    });
  }

  getFollowing(userId: number) {
    this.postService.getFollowing(userId).subscribe({
      // next: (data) => this.following = data,
      next: (data) => {
        this.following = data;
        // error: (err) => console.error('Error fetching following:', err)
      }
    });
  }

  acceptFollower(follower: any) {
    follower.status = 'accepted';
  }

  rejectFollower(follower: any) {
    this.followers = this.followers.filter(f => f.id !== follower.id);
  }

  toggleFollow(person: any) {
  if (person.following) {
    // UNFOLLOW
    this.postService.unfollowUser(this.userInfo.id, person.id).subscribe({
      next: () => {
        person.following = false;
      }
    });
  } else {
    // FOLLOW
    const followData = {
      follower: { id: this.userInfo.id },
      following: { id: person.id }
    };

    this.postService.followUser(followData).subscribe({
      next: () => {
        person.following = true;
        console.log("FOLLOW sent:", followData);
      },
      error: (err) => console.error("FOLLOW ERROR:", err)
    });
  }
}

  toggleUserMenu() {
    this.showUserMenu = !this.showUserMenu;
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  openReportModal(post: any) {
    this.selectedPostId = post.id;
    this.showReportModal = true;
  }

  closeReportModal() {
    this.showReportModal = false;
    this.reportReason = '';
    this.selectedPostId = null;
  }

  submitReport() {
    if (!this.selectedPostId || !this.reportReason.trim()) {
      alert('Please enter a reason');
      return;
    }

    const report = {
      reason: this.reportReason,
      user: { id: this.userInfo.id },
      post: { id: this.selectedPostId }
    };

    this.reportService.createReport(report).subscribe({
      next: () => {
        alert('Report submitted successfully');
        this.closeReportModal();
      },
      error: (err) => console.error('Error submitting report:', err)
    });
  }

}
