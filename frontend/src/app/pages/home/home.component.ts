import { Component, OnInit, HostListener } from '@angular/core';
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
  editingPost: any = null;
  isEditMode = false;
  lastPostId: number | null = null;
  loadingMore = false;
  noMorePosts = false;


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
      console.log('First load count:', data.length);
      this.posts = data.map(p => ({
      ...p,
      showComments: false,
      newComment: '',
      comments: p.comments.map(c => ({ ...c, showOptions: false }))
    }));

      if (data.length > 0) {
        this.lastPostId = data[data.length - 1].id;
        console.log('Posts in array:', this.posts.length);
      } else {
        this.noMorePosts = true;
      }
    }
  });
}

  @HostListener('window:scroll', [])
  onWindowScroll() {
  if (this.loadingMore || this.noMorePosts) return;

  const threshold = 200;
  const position = window.innerHeight + window.scrollY;
  const height = document.body.offsetHeight;

  if (position >= height - threshold) {
    this.loadMorePosts();
  }
}


 loadMorePosts() {
  if (!this.lastPostId) return;

  this.loadingMore = true;
  this.postService
    .getFeed(this.userInfo.id, this.lastPostId)
    .subscribe({
      next: (data) => {
        console.log('Loaded on scroll:', data.length);
        if (data.length === 0) {
          this.noMorePosts = true;
          this.loadingMore = false;
          return;
        }

        this.posts.push(
          ...data.map(p => ({
            ...p,
            showComments: false,
            newComment: '',
            comments: (p.comments || []).map(c => ({ ...c, showOptions: false }))
          }))
        );
        console.log('Total posts now:', this.posts.length);

        this.lastPostId = data[data.length - 1].id;
        this.loadingMore = false;
      },
      error: () => (this.loadingMore = false)
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
  this.resetPostForm();
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

  if (this.isEditMode && this.editingPost) {

    const updateData = {
      title: this.newPost.title.trim(),
      content: this.newPost.content.trim()
    };

    this.postService.updatePost(this.editingPost.id, updateData).subscribe({
      next: (updated) => {
        const index = this.posts.findIndex(p => p.id === updated.id);
      if (index !== -1) {
      this.posts[index] = {
      ...this.posts[index],
      ...updated
      };
    }
        this.resetPostForm();
      },
      error: (err) => {
      if (err.status === 429) {
        alert(err.error);
      } else {
        console.error('Create error', err);
      }
    }
  });
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
        comments: (created.comments || []).map(c => ({ ...c, showOptions: false })),
        showComments: false,
        newComment: ''
      });
      this.resetPostForm();
    },
    error: err => console.error('Create error', err)
  });
}

  resetPostForm() {
  this.showCreatePost = false;
  this.isEditMode = false;
  this.editingPost = null;
  this.newPost = { title: '', content: '' };
  this.selectedImage = null;
  this.selectedVideo = null;
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

deletePost(post: any) {
  if (!confirm('Are you sure you want to delete this post?')) return;

  this.postService.deletePost(post.id).subscribe({
    next: () => {
      this.posts = this.posts.filter(p => p.id !== post.id);
    },
    error: err => console.error('Delete error', err)
  });
}

editPost(post: any) {
  if (post.authorId !== this.userInfo.id) return;

  this.isEditMode = true;
  this.editingPost = post;

  this.newPost = {
    title: post.title,
    content: post.content
  };

  this.selectedImage = null;
  this.selectedVideo = null;
  this.showCreatePost = true;
  }

  deleteComment(post: any, comment: any) {
  if (!confirm('Are you sure you want to delete this comment?')) return;

  this.postService.deleteComment(comment.id).subscribe({
    next: () => {
      post.comments = post.comments.filter((c: any) => c.id !== comment.id);
    },
    error: (err) => console.error('Error deleting comment:', err)
  });
  }

}