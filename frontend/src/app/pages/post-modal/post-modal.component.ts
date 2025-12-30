import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-modal.component.html',
  styleUrls: ['./post-modal.component.css']
})
export class PostModalComponent {
  @Input() post: any;        
  @Input() visible = false;   
  @Output() close = new EventEmitter<void>(); 

  hide() {
    this.close.emit();
  }
}
