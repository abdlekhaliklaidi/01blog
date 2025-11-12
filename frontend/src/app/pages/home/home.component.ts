import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PostService } from '../../services/post.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
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

 newPost = {
  title: '',
  content: ''
  };
  
  selectedFile: File | null = null;

  constructor(private router: Router, private postService: PostService) {}

  ngOnInit() {
    this.userInfo = {
      id: 1,
      name: 'Ali Student',
      bio: 'Full Stack Developer | Angular & Spring Boot',
      avatar: 'https://i.pravatar.cc/100?img=12'
    };

    this.followers = [
      { id: 1, name: 'Amina Dev', avatar: 'https://i.pravatar.cc/40?img=1', status: 'pending' },
      { id: 2, name: 'Youssef Code', avatar: 'https://i.pravatar.cc/40?img=2', status: 'pending' },
      { id: 3, name: 'Hassan UI', avatar: 'https://i.pravatar.cc/40?img=3', status: 'accepted' }
    ];

    this.following = [
      { id: 1, name: 'Sara Dev', avatar: 'https://i.pravatar.cc/40?img=4', following: true },
      { id: 2, name: 'Omar JS', avatar: 'https://i.pravatar.cc/40?img=5', following: false }
    ];

    this.loadPosts();
  }

    // this.posts = [
    //   {
    //     id: 1,
    //     author: 'Ali Student',
    //     content: 'Sharing my first experience with Spring Boot!',
    //     imageUrl: 'https://picsum.photos/600/300?1',
    //     likes: 12,
    //     comments: 3,
    //     timestamp: '2025-11-02T10:00:00'
    //   },
    //   {
    //     id: 2,
    //     author: 'Sara Dev',
    //     content: 'Today I learned how to create a REST API in Angular 🔥',
    //     imageUrl: 'https://picsum.photos/600/300?2',
    //     likes: 22,
    //     comments: 5,
    //     timestamp: '2025-11-01T14:30:00'
    //   }
    // ];
  //    this.loadPosts();
  // }

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

  toggleUserMenu() {
    this.showUserMenu = !this.showUserMenu;
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  acceptFollower(follower: any) {
    follower.status = 'accepted';
  }

  rejectFollower(follower: any) {
    this.followers = this.followers.filter(f => f.id !== follower.id);
  }

  toggleFollow(person: any) {
    person.following = !person.following;
  }
  
  toggleLike(post: any) {
  this.postService.toggleLike(this.userInfo.id, post.id).subscribe({
    next: (res) => {
      if (res === null) {
        post.likes = post.likes.filter((l: any) => l.user.id !== this.userInfo.id);
      } else {
        post.likes.push({ user: { id: this.userInfo.id } });
      }
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
    formData.append('authorId', this.userInfo.id.toString());
    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

   this.postService.createPost(formData).subscribe({
      next: (created) => {
        // this.posts.unshift({
        //   ...created,
        //   likes: [],
        //   comments: [],
        //   showComments: false,
        //   newComment: ''
        // });
        this.posts.unshift({
  ...created,
  likes: created.likes || [],
  comments: created.comments || [],
  showComments: false,
  newComment: '',
  imageBase64: created.imageBase64 
});
        this.closeCreatePost();
      },
      error: (err) => console.error('Error creating post', err)
    });
  }
}