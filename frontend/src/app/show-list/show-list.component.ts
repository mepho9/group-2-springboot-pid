import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ShowService, Show } from "../services/show.service";

@Component({
  selector: 'app-show-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './show-list.component.html',
  styleUrl: './show-list.component.css'
})
export class ShowListComponent implements OnInit {

  show: Show[] = [];
  error: string | null = null;

  // Search, Filter & Sort states
  searchQuery: string = '';
  statusFilter: string = 'all'; // 'all' | 'available' | 'soldout'
  sortBy: string = 'default';   // 'default' | 'title-asc' | 'title-desc' | 'price-asc' | 'price-desc'

  // Share & Notification states
  toastMessage: string | null = null;
  copiedShowId: number | null = null;
  highlightedShowId: number | null = null;

  private showService = inject(ShowService);
  private route = inject(ActivatedRoute);

  ngOnInit() {
    this.fetchShows();
  }

  fetchShows() {
    this.showService.getShow().subscribe({
      next: (data) => {
        this.show = data;
        this.checkQueryParam();
      },
      error: (err) => {
        this.error = "Impossible de charger la liste des spectacles.";
        console.error(err);
      }
    });
  }

  // Check URL query parameter (?show=slug or ?show=id) on load
  checkQueryParam() {
    this.route.queryParams.subscribe(params => {
      const target = params['show'];
      if (target && this.show.length > 0) {
        const found = this.show.find(
          s => s.slug === target || s.id.toString() === target
        );
        if (found) {
          this.highlightedShowId = found.id;
          setTimeout(() => {
            const el = document.getElementById(`show-card-${found.id}`);
            if (el) {
              el.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
          }, 300);
        }
      }
    });
  }

  // Getter computing search, filter, and sort
  get filteredShows(): Show[] {
    let result = [...this.show];

    // 1. Search Query Filter
    if (this.searchQuery.trim()) {
      const q = this.searchQuery.toLowerCase().trim();
      result = result.filter(s =>
        (s.title && s.title.toLowerCase().includes(q)) ||
        (s.description && s.description.toLowerCase().includes(q))
      );
    }

    // 2. Status Filter
    if (this.statusFilter === 'available') {
      result = result.filter(s => s.bookable);
    } else if (this.statusFilter === 'soldout') {
      result = result.filter(s => !s.bookable);
    }

    // 3. Sorting
    switch (this.sortBy) {
      case 'title-asc':
        result.sort((a, b) => a.title.localeCompare(b.title));
        break;
      case 'title-desc':
        result.sort((a, b) => b.title.localeCompare(a.title));
        break;
      case 'price-asc':
        result.sort((a, b) => a.price - b.price);
        break;
      case 'price-desc':
        result.sort((a, b) => b.price - a.price);
        break;
    }

    return result;
  }

  shareShow(item: Show, event: Event) {
    event.stopPropagation();
    const shareTarget = item.slug || item.id;
    const shareUrl = `${window.location.origin}${window.location.pathname}?show=${shareTarget}`;

    navigator.clipboard.writeText(shareUrl).then(() => {
      this.copiedShowId = item.id;
      this.showToast(`🔗 Lien direct pour "${item.title}" copié dans le presse-papier !`);
      setTimeout(() => {
        if (this.copiedShowId === item.id) {
          this.copiedShowId = null;
        }
      }, 3000);
    }).catch(err => {
      console.error('Erreur lors de la copie du lien:', err);
    });
  }

  showToast(msg: string) {
    this.toastMessage = msg;
    setTimeout(() => {
      if (this.toastMessage === msg) {
        this.toastMessage = null;
      }
    }, 3500);
  }
}
