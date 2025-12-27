import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService, User } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ReportService } from '../../services/report.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class ProfileComponent implements OnInit {
  currentUser!: User;
  userInfo!: User;

  followers: User[] = [];
  following: User[] = [];

  showEditProfileModal = false;
  editName = '';
  editBio = '';

  showCreatePostModal = false;
  // newPostContent = '';

  newPost = {
  title: '',
  content: '',
  file: null as File | null
  };
  
  showReportUserModal = false;
  reportUserReason = '';

  isFollowingUser = false;

  constructor(
    private userService: UserService,
    private reportService: ReportService,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.userService.getMe().subscribe({
      next: currentUser => {
        this.currentUser = currentUser;

        const userId = Number(this.route.snapshot.paramMap.get('id')) || this.currentUser.id;

        this.userService.getUserById(userId).subscribe({
          next: user => {
            this.userInfo = {
            id: user.id,
            name: user.firstname + ' ' + user.lastname,
            bio: 'Zone01 Oujda',
            avatar: 'https://i.pravatar.cc/100?img=12'
       };
            this.loadFollowers(user.id);
            this.loadFollowing(user.id);
            if (this.currentUser.id !== this.userInfo.id) {
            this.userService
              .isFollowing(this.currentUser.id, this.userInfo.id)
              .subscribe(res => this.isFollowingUser = res);
          }
        },
        error: err => console.error('Error loading user by id:', err)
      });
    },
    error: err => console.error('Error loading current user:', err)
  });
}

  loadFollowers(userId: number) {
    this.userService.getFollowers(userId).subscribe({
      next: data => this.followers = data,
      error: err => console.error('Error loading followers:', err)
    });
  }

  loadFollowing(userId: number) {
    this.userService.getFollowing(userId).subscribe({
      next: data => this.following = data,
      error: err => console.error('Error loading following:', err)
    });
  }

  isCurrentUserProfile(): boolean {
    return this.currentUser && this.userInfo && this.currentUser.id === this.userInfo.id;
  }

  openEditProfile() {
    this.editName = this.userInfo.name || '';
    this.editBio = this.userInfo.bio || '';
    this.showEditProfileModal = true;
  }

  saveProfile() {
  const parts = this.editName.trim().split(' ');
  const firstname = parts.shift() || '';
  const lastname = parts.join(' ');

  const updatedUser = {
    firstname,
    lastname,
    bio: this.editBio,
    avatar: this.userInfo.avatar
  };

  this.userService.updateUser(this.currentUser.id, updatedUser)
    .subscribe({
      next: (updated) => {
        this.userInfo = {
          ...this.userInfo,
          name: updated.firstname + ' ' + updated.lastname,
          bio: updated.bio
        };
        this.showEditProfileModal = false;
      },
      error: err => console.error('Update profile error', err)
    });
}

  openCreatePost() {
    this.showCreatePostModal = true;
  }

  onFileSelected(event: any) {
  this.newPost.file = event.target.files[0];
  }

  createPost() {
  console.log('New Post:', this.newPost);
  this.newPost = {
    title: '',
    content: '',
    file: null
  };

  this.showCreatePostModal = false;
}

  toggleFollow() {
  if (!this.userInfo || !this.currentUser) return;

  if (this.isFollowingUser) {
    this.userService
      .unfollowUser(this.currentUser.id, this.userInfo.id)
      .subscribe(() => {
        this.isFollowingUser = false;
      });
  } else {
    this.userService
      .followUser(this.currentUser.id, this.userInfo.id)
      .subscribe(() => {
        this.isFollowingUser = true;
      });
  }
}

  isFollowing(user: User): boolean {
    if (!this.following || !user) return false;
    return this.following.some(f => f.id === user.id);
  }

  openReportUserModal() {
  this.showReportUserModal = true;
}

  closeReportUserModal() {
  this.showReportUserModal = false;
  this.reportUserReason = '';
}
  
submitReportUser() {
  if (!this.reportUserReason.trim()) {
    alert('Please enter a reason for reporting.');
    return;
  }

  const report = {
    reason: this.reportUserReason,
    reporter: { id: this.currentUser.id }
  };

  this.reportService.reportUser(this.userInfo.id, report).subscribe({
    next: () => {
      alert('Report submitted successfully!');
      this.closeReportUserModal();
    },
    error: err => console.error('Error reporting user:', err)
  });
}

}
