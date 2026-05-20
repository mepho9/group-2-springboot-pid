import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ShowService, Show} from "../services/show.service";

@Component({
  selector: 'app-show-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './show-list.component.html',
  styleUrl: './show-list.component.css'
})
export class ShowListComponent implements OnInit {

  show: Show[] = [];
  error: string | null = null;

  constructor(private showService: ShowService) {
  }

  activeIndex = 0;

  ngOnInit() {
    this.fetchShows();
  }

  fetchShows() {
    this.showService.getShow().subscribe({
      next: (data) => {
        this.show = data;
      },
      error:(err) =>{
        this.error = "Impossible de charger la liste des spétacles.";
        console.error(err)
      }
    })
  }

  onScroll(event: Event) {
    const container = event.target as HTMLElement;
    const scrollTop = container.scrollTop;
    const height = container.clientHeight;

    const index = Math.round(scrollTop / height);
    if (index !== this.activeIndex && index >= 0 && index < this.show.length) {
      this.activeIndex = index;
    }
  }

  scrollTo(index: number) {
    this.activeIndex = index;
    const container = document.querySelector('.slider-viewport');
    if (container) {
      const height = container.clientHeight;
      container.scrollTo({
        top: index * height,
        behavior: 'smooth'
      });
    }
  }
}
