import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ShowService, Show } from '../services/show.service';
import { AuthService } from '../services/auth.service';

export interface ReviewComment {
  id: number;
  author: string;
  stars: number;
  review?: string;
  text?: string;
  date: string;
}

@Component({
  selector: 'app-show-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './show-detail.component.html',
  styleUrl: './show-detail.component.css'
})
export class ShowDetailComponent implements OnInit {

  show: Show | null = null;
  loading = true;
  error = '';

  comments: ReviewComment[] = [];
  newRating: number = 5;
  newCommentText: string = '';
  toastMessage: string | null = null;

  authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private showService = inject(ShowService);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = +params['id'];
      if (id) {
        this.fetchShowDetail(id);
        this.fetchShowReviews(id);
      } else {
        this.error = 'Identifiant de spectacle invalide.';
        this.loading = false;
      }
    });
  }

  fetchShowDetail(id: number): void {
    this.loading = true;
    this.showService.getShowById(id).subscribe({
      next: (data) => {
        this.show = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.showService.getShow().subscribe({
          next: (list) => {
            const found = list.find(s => s.id === id);
            if (found) {
              this.show = found;
              this.error = '';
            } else {
              this.error = 'Spectacle introuvable.';
            }
            this.loading = false;
          },
          error: () => {
            this.error = 'Impossible de charger les détails du spectacle.';
            this.loading = false;
          }
        });
      }
    });
  }

  fetchShowReviews(id: number): void {
    this.showService.getShowReviews(id).subscribe({
      next: (data) => {
        this.comments = data.map(item => ({
          id: item.id,
          author: item.author,
          stars: item.stars,
          text: item.review,
          date: item.date
        }));
      },
      error: (err) => {
        console.error('Erreur lors du chargement des avis:', err);
      }
    });
  }

  setRating(stars: number): void {
    this.newRating = stars;
  }

  submitComment(): void {
    if (!this.authService.isLoggedIn()) {
      this.showToast('🔒 Veuillez vous connecter pour publier un avis.');
      return;
    }

    if (!this.newCommentText.trim() || !this.show) {
      return;
    }

    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) return;

    const payload = {
      userLogin: currentUser,
      review: this.newCommentText.trim(),
      stars: this.newRating
    };

    this.showService.addShowReview(this.show.id, payload).subscribe({
      next: () => {
        this.newCommentText = '';
        this.newRating = 5;
        this.showToast('✅ Votre avis a été enregistré en base de données !');
        this.fetchShowReviews(this.show!.id);
      },
      error: (err) => {
        console.error(err);
        this.showToast('⚠️ Erreur lors de l enregistrement de l avis.');
      }
    });
  }

  showToast(msg: string): void {
    this.toastMessage = msg;
    setTimeout(() => {
      if (this.toastMessage === msg) {
        this.toastMessage = null;
      }
    }, 4000);
  }

  getStarArray(count: number): number[] {
    return Array(count || 0).fill(0);
  }
}
