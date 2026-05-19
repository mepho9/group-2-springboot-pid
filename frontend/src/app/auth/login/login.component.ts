import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  template: `
    <div class="auth-container">
      <div class="card auth-card">
        <h2 class="text-center">Connexion</h2>
        <p class="text-center text-muted mb-4">Heureux de vous revoir !</p>
        
        <div *ngIf="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>

        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label for="username">Nom d'utilisateur</label>
            <input type="text" id="username" class="form-control" formControlName="username" placeholder="Entrez votre nom d'utilisateur" />
          </div>
          
          <div class="form-group">
            <label for="password">Mot de passe</label>
            <input type="password" id="password" class="form-control" formControlName="password" placeholder="Entrez votre mot de passe" />
          </div>
          
          <button type="submit" class="btn-primary w-100 mt-4" [disabled]="loginForm.invalid">Se connecter</button>
        </form>
        
        <div class="text-center mt-4 auth-footer">
          <p>Pas encore de compte ? <a routerLink="/register">S'inscrire</a></p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .auth-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 80vh;
      padding: 2rem 0;
    }
    .auth-card {
      width: 100%;
      max-width: 400px;
    }
    .w-100 { width: 100%; padding: 0.75rem; font-size: 1rem; }
    .auth-footer a { color: var(--primary); text-decoration: none; font-weight: 500; }
    .auth-footer a:hover { text-decoration: underline; }
    .error-message {
      background: rgba(239, 68, 68, 0.1);
      color: #ef4444;
      padding: 0.75rem;
      border-radius: 8px;
      margin-bottom: 1rem;
      text-align: center;
      border: 1px solid rgba(239, 68, 68, 0.2);
    }
  `]
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.errorMessage = '';
      this.authService.login(this.loginForm.value).subscribe({
        next: (res) => {
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = 'Identifiant ou mot de passe incorrect.';
        }
      });
    }
  }
}
