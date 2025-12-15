import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService, User } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

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
  newPostContent = '';

  isFollowingUser = false;

  constructor(
    private userService: UserService,
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
            name: "Wilcom" + ' ' + user.firstname + ' ' + user.lastname,
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
    this.userInfo.name = this.editName;
    this.userInfo.bio = this.editBio;
    this.showEditProfileModal = false;
  }

  openCreatePost() {
    this.showCreatePostModal = true;
  }

  savePost() {
    console.log("New Post:", this.newPostContent);
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
}
