import { Component, OnInit } from '@angular/core';

interface User {
  id: number;
  name: string;
  avatar: string;
  bio?: string;
}

interface Post {
  id: number;
  authorId: number;
  authorFirstName: string;
  title: string;
  content: string;
  imageUrl?: string;
  videoUrl?: string;
  likesCount?: number;
  comments?: any[];
  showComments?: boolean;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userInfo: User = {
    id: 1,
    name: 'John Doe',
    avatar: 'https://i.pravatar.cc/150?img=1',
    bio: 'This is my bio.'
  };

  followers: User[] = [
    { id: 2, name: 'Jane Smith', avatar: 'https://i.pravatar.cc/40?img=2' },
    { id: 3, name: 'Bob Johnson', avatar: 'https://i.pravatar.cc/40?img=3' }
  ];

  following: User[] = [
    { id: 4, name: 'Alice Brown', avatar: 'https://i.pravatar.cc/40?img=4' }
  ];

  posts: Post[] = [
    {
      id: 1,
      authorId: 1,
      authorFirstName: 'John',
      title: 'My First Post',
      content: 'Hello world!',
      likesCount: 10,
      comments: []
    },
    {
      id: 2,
      authorId: 1,
      authorFirstName: 'John',
      title: 'Another Post',
      content: 'Angular is awesome!',
      likesCount: 5,
      comments: []
    }
  ];

  showEditProfileModal = false;
  editName = '';
  editBio = '';

  newPost = {
    title: '',
    content: '',
    file: null as File | null
  };

  showCreatePost = false;

  constructor() {}

  ngOnInit(): void {}

  get userPosts() {
    return this.posts.filter(post => post.authorId === this.userInfo.id);
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

  openCreatePost() {
    this.showCreatePost = true;
  }

  closeCreatePost() {
    this.showCreatePost = false;
    this.newPost = { title: '', content: '', file: null };
  }

  createPost() {
    const newPostObj: Post = {
      id: this.posts.length + 1,
      authorId: this.userInfo.id,
      authorFirstName: this.userInfo.name,
      title: this.newPost.title,
      content: this.newPost.content,
      imageUrl: this.newPost.file ? URL.createObjectURL(this.newPost.file) : undefined,
      likesCount: 0,
      comments: []
    };
    this.posts.unshift(newPostObj);
    this.closeCreatePost();
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) this.newPost.file = file;
  }
}
