import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PostService } from '../../services/post.service';
import { ReportService } from '../../services/report.service';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';

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
  allUsers: any[] = [];
  userInfo: any;
  showUserMenu = false;
  showCreatePost = false;
  isDarkMode = false;

  newPost = { title: '', content: '' };
  selectedFile: File | null = null;

  showReportModal = false;
  reportReason = '';
  selectedPostId: number | null = null;

  constructor(
    private router: Router,
    private postService: PostService,
    private reportService: ReportService,
    private userService: UserService,
    public auth: AuthService
  ) {}
  
  ngOnInit() {
    console.log('ngOnInit started');
  const savedTheme = localStorage.getItem('theme');
  this.isDarkMode = savedTheme === 'dark';
  document.body.classList.toggle('dark-mode', this.isDarkMode);
  console.log('Theme applied:', this.isDarkMode);
this.userService.getMe().subscribe({
    next: (user) => {
      this.userInfo = {
        id: user.id,
        name: "Wilcom" + ' ' + user.firstname + ' ' + user.lastname,
        // bio: 'Full Stack Developer | Angular & Spring Boot',
        bio: 'Zone01 Oujda',
        avatar: 'https://i.pravatar.cc/100?img=12'
      };

      this.getFollowers(this.userInfo.id);
      this.getFollowing(this.userInfo.id);
      this.loadPosts();
      this.loadAllUsers();
    },
    error: (err) => {
      console.error('Error loading user info:', err);
      this.router.navigate(['/login']);
    }
  });
}
    
  toggleTheme() {
  this.isDarkMode = !this.isDarkMode;

  if (this.isDarkMode) {
    document.body.classList.add('dark-mode');
    localStorage.setItem('theme', 'dark');
  } else {
    document.body.classList.remove('dark-mode');
    localStorage.setItem('theme', 'light');
  }
}

  loadAllUsers() {
  this.userService.getAllUsers().subscribe({
    next: (users) => {
      this.allUsers = users.filter(u => u.id !== this.userInfo.id);

      this.postService.getFollowing(this.userInfo.id).subscribe({
        next: (followingData) => {
          this.following = followingData;
          this.allUsers = this.allUsers.map(u => ({
            ...u,
            following: !!this.following.find(f => f.id === u.id)
          }));
        },
        error: (err) => console.error('Error fetching following:', err)
      });
    },
    error: (err) => console.error('Error loading users:', err)
  });
}
  
  loadPosts() {
  this.postService.getFeed(this.userInfo.id).subscribe({
  next: (data) => {
    this.posts = data.map((p: any) => ({
      ...p,
      likes: p.likes || [],
      comments: p.comments || [],
      showComments: false,
      newComment: ''
    }));
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
  
selectedImage: File | null = null;
selectedVideo: File | null = null;

onFileSelected(event: Event) {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files.length > 0) {
    const file = input.files[0];
    if (file.type.startsWith('image/')) this.selectedImage = file;
    else if (file.type.startsWith('video/')) this.selectedVideo = file;
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

  if (this.selectedImage) formData.append('image', this.selectedImage);
  if (this.selectedVideo) formData.append('video', this.selectedVideo);

  this.postService.createPost(formData).subscribe({
    next: (created) => {
      this.posts.unshift({
        ...created,
        likes: created.likes || [],
        comments: created.comments || [],
        showComments: false,
        newComment: '',
        imageUrl: created.imageUrl,
        videoUrl: created.videoUrl
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
  if (this.userInfo.id === person.id) return;

  if (person.following) {
    // UNFOLLOW
    this.postService.unfollowUser(this.userInfo.id, person.id).subscribe({
      next: () => {
        person.following = false;
        this.getFollowing(this.userInfo.id);
      },
      error: (err) => console.error('Error unfollowing user', err)
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
        this.getFollowing(this.userInfo.id);
      },
      error: (err) => console.error('Error following user', err)
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
    post: { id: this.selectedPostId }
  };

  const reporterId = this.userInfo.id;

  this.reportService.createReport(report, reporterId).subscribe({
    next: () => {
      alert('Report submitted successfully');
      this.closeReportModal();
    },
    error: (err) => console.error('Error submitting report:', err)
  });
}

}