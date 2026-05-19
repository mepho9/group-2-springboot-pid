import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ArtistService, Artist } from '../services/artist.service';

@Component({
  selector: 'app-artist-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page-header">
      <h2>🎭 Nos Artistes</h2>
      <p class="text-muted">Découvrez les talents qui font vivre nos spectacles.</p>
    </div>

    <div *ngIf="loading" class="text-center mt-4">
      <p>Chargement des artistes...</p>
    </div>

    <div *ngIf="!loading && artists.length === 0" class="text-center mt-4">
      <p>Aucun artiste trouvé.</p>
    </div>

    <div class="artist-grid mt-4" *ngIf="!loading && artists.length > 0">
      <div class="card artist-card" *ngFor="let artist of artists">
        <div class="artist-avatar">
          {{ artist.firstname.charAt(0) }}{{ artist.lastname.charAt(0) }}
        </div>
        <div class="artist-info">
          <h3>{{ artist.firstname }} {{ artist.lastname }}</h3>
          <a [routerLink]="['/artists', artist.id]" class="btn-link">Voir Profil &rarr;</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .page-header {
      margin-bottom: 3rem;
      border-bottom: 1px solid var(--border);
      padding-bottom: 1rem;
    }
    .artist-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1.5rem;
    }
    .artist-card {
      display: flex;
      align-items: center;
      gap: 1rem;
    }
    .artist-avatar {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background: linear-gradient(135deg, var(--primary), #a855f7);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.25rem;
      font-weight: bold;
      color: white;
    }
    .artist-info h3 {
      margin: 0 0 0.25rem 0;
      font-size: 1.1rem;
    }
    .btn-link {
      color: var(--primary);
      text-decoration: none;
      font-size: 0.9rem;
      font-weight: 500;
    }
    .btn-link:hover {
      text-decoration: underline;
    }
    .text-muted { color: var(--text-muted); }
  `]
})
export class ArtistListComponent implements OnInit {
  artists: Artist[] = [];
  loading = true;

  constructor(private artistService: ArtistService) {}

  ngOnInit(): void {
    this.artistService.getArtists().subscribe({
      next: (data) => {
        this.artists = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des artistes', err);
        this.loading = false;
      }
    });
  }
}
