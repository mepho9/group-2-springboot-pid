import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ArtistService, Artist } from '../services/artist.service';

@Component({
  selector: 'app-artist-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page-header">
      <a routerLink="/artists" class="btn-link mb-4" style="display: inline-block;">&larr; Retour aux artistes</a>
      <h2>Profil de l'Artiste</h2>
    </div>

    <div *ngIf="loading" class="text-center mt-4">
      <p>Chargement des détails...</p>
    </div>

    <div *ngIf="!loading && !artist" class="text-center mt-4">
      <p>Artiste introuvable.</p>
    </div>

    <div class="artist-profile mt-4" *ngIf="!loading && artist">
      <div class="card profile-card">
        <div class="artist-avatar large">
          {{ artist.firstname.charAt(0) }}{{ artist.lastname.charAt(0) }}
        </div>
        <div class="profile-info">
          <h1>{{ artist.firstname }} {{ artist.lastname }}</h1>
          <p class="text-muted">Acteur / Metteur en Scène</p>
          
          <div class="mt-4">
            <button class="btn-primary">Voir ses spectacles</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .page-header {
      margin-bottom: 2rem;
    }
    .profile-card {
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;
      padding: 3rem 2rem;
    }
    .artist-avatar.large {
      width: 120px;
      height: 120px;
      font-size: 2.5rem;
      margin-bottom: 1.5rem;
      background: linear-gradient(135deg, var(--primary), #a855f7);
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      color: white;
      font-weight: bold;
    }
    .profile-info h1 {
      margin: 0 0 0.5rem 0;
    }
    .btn-link {
      color: var(--primary);
      text-decoration: none;
      font-weight: 500;
    }
    .btn-link:hover { text-decoration: underline; }
  `]
})
export class ArtistDetailComponent implements OnInit {
  artist: Artist | null = null;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private artistService: ArtistService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.artistService.getArtist(id).subscribe({
        next: (data) => {
          this.artist = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Erreur de chargement', err);
          this.loading = false;
        }
      });
    } else {
      this.loading = false;
    }
  }
}
