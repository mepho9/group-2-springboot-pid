import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ShowService } from '../services/show.service';
import { AuthService } from '../services/auth.service';

export interface ReservationItem {
  id: number;
  showId: number;
  showTitle: string;
  showPoster: string;
  showPrice: number;
  bookingDate: string;
  status: string;
}

@Component({
  selector: 'app-user-reservations',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-reservations.component.html',
  styleUrl: './user-reservations.component.css'
})
export class UserReservationsComponent implements OnInit {

  reservations: ReservationItem[] = [];
  loading = true;
  error = '';

  authService = inject(AuthService);
  private showService = inject(ShowService);

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.fetchReservations(currentUser);
    } else {
      this.error = 'Veuillez vous connecter pour consulter vos réservations.';
      this.loading = false;
    }
  }

  fetchReservations(userLogin: string): void {
    this.loading = true;
    this.showService.getUserReservations(userLogin).subscribe({
      next: (data) => {
        this.reservations = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Impossible de charger vos réservations.';
        this.loading = false;
      }
    });
  }
}
