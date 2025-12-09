import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

interface User {
  id: number;
  name: string;
  avatar: string;
  bio?: string;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  imports: [RouterModule],
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: User = {
    id: 1,
    name: 'John Doe',
    avatar: 'https://i.pravatar.cc/100?img=12',
    bio: 'This is my bio.'
  };

  userInfo: User = {
    id: 2,
    name: 'Jane Smith',
    avatar: 'https://i.pravatar.cc/150?img=2',
    bio: 'Hello there!'
  };

  followers: User[] = [
    { id: 3, name: 'Bob Johnson', avatar: 'https://i.pravatar.cc/40?img=3' }
  ];

  following: User[] = [
    { id: 1, name: 'John Doe', avatar: 'https://i.pravatar.cc/40?img=1' }
  ];

  showEditProfileModal = false;
  editName = '';
  editBio = '';

  constructor() { }

  ngOnInit(): void { }

  isCurrentUserProfile(): boolean {
    return this.currentUser.id === this.userInfo.id;
  }

  openEditProfile() {
    this.editName = this.userInfo.name;
    this.editBio = this.userInfo.bio || '';
    this.showEditProfileModal = true;
  }

  saveProfile() {
    this.userInfo.name = this.editName;
    this.userInfo.bio = this.editBio;
    this.showEditProfileModal = false;
  }
  
  showCreatePostModal = false;
  newPostContent = '';

openCreatePost() {
  this.showCreatePostModal = true;
}

savePost() {
  console.log("New Post:", this.newPostContent);
  this.showCreatePostModal = false;
}

  toggleFollow() {
    if (this.isFollowing()) {
      this.following = this.following.filter(u => u.id !== this.userInfo.id);
    } else {
      this.following.push(this.userInfo);
    }
  }

  isFollowing(): boolean {
    return this.following.some(u => u.id === this.userInfo.id);
  }
}