import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="hero text-center">
      <h1 class="hero-title">Bienvenue sur TheaterBooking</h1>
      <p class="hero-subtitle">Réservez vos places de théâtre pour les meilleurs spectacles de la région.</p>
      <div class="hero-actions mt-4">
        <a routerLink="/artists" class="btn-primary" style="margin-right: 1rem;">Découvrir les Artistes</a>
        <a routerLink="/register" class="btn-secondary">Créer un compte</a>
      </div>
    </div>

    <div class="features mt-4">
      <div class="card">
        <h3>🎭 Spectacles Variés</h3>
        <p>Découvrez une sélection éclectique de pièces de théâtre, comédies et drames.</p>
      </div>
      <div class="card">
        <h3>⭐ Artistes Talents</h3>
        <p>Retrouvez vos acteurs et metteurs en scène préférés sur les planches.</p>
      </div>
      <div class="card">
        <h3>🎫 Réservation Rapide</h3>
        <p>Réservez vos billets en quelques clics en toute sécurité.</p>
      </div>
    </div>
  `,
  styles: [`
    .hero {
      padding: 4rem 0;
    }
    .hero-title {
      font-size: 3rem;
      background: linear-gradient(90deg, var(--primary), #a855f7);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      margin-bottom: 1rem;
    }
    .hero-subtitle {
      font-size: 1.25rem;
      color: var(--text-muted);
      max-width: 600px;
      margin: 0 auto;
    }
    .btn-secondary {
      background: transparent;
      color: var(--text-main);
      padding: 0.5rem 1.25rem;
      border-radius: 8px;
      font-weight: 600;
      border: 1px solid var(--border);
      cursor: pointer;
      text-decoration: none;
      transition: var(--transition);
    }
    .btn-secondary:hover {
      background: var(--surface);
      border-color: var(--text-muted);
    }
    .features {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 2rem;
      margin-top: 4rem;
    }
  `]
})
export class HomeComponent { }
