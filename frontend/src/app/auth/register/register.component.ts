import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { UserService } from '../../services/user.service';

export function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.get('password')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;
  if (password !== confirmPassword) {
    control.get('confirmPassword')?.setErrors({ passwordMismatch: true });
    return { passwordMismatch: true };
  } else {
    const confirmControl = control.get('confirmPassword');
    if (confirmControl && confirmControl.hasError('passwordMismatch')) {
      delete confirmControl.errors?.['passwordMismatch'];
      if (!Object.keys(confirmControl.errors || {}).length) {
        confirmControl.setErrors(null);
      }
    }
  }
  return null;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  template: `
    <div class="auth-container">
      <div class="card auth-card">
        <h2 class="text-center">Créer un compte</h2>
        <p class="text-center text-muted mb-4">Rejoignez-nous pour réserver vos places.</p>
        
        <div *ngIf="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
        
        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
          <div class="form-row">
            <div class="form-group half">
              <label for="firstname">Prénom</label>
              <input type="text" id="firstname" class="form-control" formControlName="firstname" />
              <div *ngIf="registerForm.get('firstname')?.touched && registerForm.get('firstname')?.hasError('required')" class="text-danger small">Prénom requis</div>
              <div *ngIf="registerForm.get('firstname')?.hasError('backendError')" class="text-danger small">{{ registerForm.get('firstname')?.getError('backendError') }}</div>
            </div>
            <div class="form-group half">
              <label for="lastname">Nom</label>
              <input type="text" id="lastname" class="form-control" formControlName="lastname" />
              <div *ngIf="registerForm.get('lastname')?.touched && registerForm.get('lastname')?.hasError('required')" class="text-danger small">Nom requis</div>
              <div *ngIf="registerForm.get('lastname')?.hasError('backendError')" class="text-danger small">{{ registerForm.get('lastname')?.getError('backendError') }}</div>
            </div>
          </div>
          
          <div class="form-group">
            <label for="login">Nom d'utilisateur</label>
            <input type="text" id="login" class="form-control" formControlName="login" />
            <div *ngIf="registerForm.get('login')?.touched && registerForm.get('login')?.hasError('required')" class="text-danger small">Login requis</div>
            <div *ngIf="registerForm.get('login')?.touched && registerForm.get('login')?.hasError('minlength')" class="text-danger small">Min 4 caractères</div>
            <div *ngIf="registerForm.get('login')?.hasError('backendError')" class="text-danger small">{{ registerForm.get('login')?.getError('backendError') }}</div>
          </div>

          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" class="form-control" formControlName="email" />
            <div *ngIf="registerForm.get('email')?.touched && registerForm.get('email')?.hasError('required')" class="text-danger small">Email requis</div>
            <div *ngIf="registerForm.get('email')?.touched && registerForm.get('email')?.hasError('email')" class="text-danger small">Email invalide</div>
            <div *ngIf="registerForm.get('email')?.hasError('backendError')" class="text-danger small">{{ registerForm.get('email')?.getError('backendError') }}</div>
          </div>
          
          <div class="form-group">
            <label for="password">Mot de passe</label>
            <input type="password" id="password" class="form-control" formControlName="password" />
            <div *ngIf="registerForm.get('password')?.touched && registerForm.get('password')?.hasError('required')" class="text-danger small">Mot de passe requis</div>
            <div *ngIf="registerForm.get('password')?.touched && registerForm.get('password')?.hasError('minlength')" class="text-danger small">Min 6 caractères</div>
            <div *ngIf="registerForm.get('password')?.hasError('backendError')" class="text-danger small">{{ registerForm.get('password')?.getError('backendError') }}</div>
          </div>
          
          <div class="form-group">
            <label for="confirmPassword">Confirmer le mot de passe</label>
            <input type="password" id="confirmPassword" class="form-control" formControlName="confirmPassword" />
            <div *ngIf="registerForm.get('confirmPassword')?.touched && registerForm.get('confirmPassword')?.hasError('required')" class="text-danger small">Confirmation requise</div>
            <div *ngIf="registerForm.get('confirmPassword')?.touched && registerForm.get('confirmPassword')?.hasError('passwordMismatch')" class="text-danger small">Les mots de passe ne correspondent pas</div>
            <div *ngIf="registerForm.get('confirmPassword')?.hasError('backendError')" class="text-danger small">{{ registerForm.get('confirmPassword')?.getError('backendError') }}</div>
          </div>

          <div class="form-group">
            <label for="langue">Langue préférée</label>
            <select id="langue" class="form-control" formControlName="langue">
              <option value="fr">Français</option>
              <option value="en">English</option>
              <option value="nl">Nederlands</option>
            </select>
            <div *ngIf="registerForm.get('langue')?.hasError('backendError')" class="text-danger small">{{ registerForm.get('langue')?.getError('backendError') }}</div>
          </div>
          
          <button type="submit" class="btn-primary w-100 mt-4" [disabled]="registerForm.invalid || isSubmitting">
            {{ isSubmitting ? 'Inscription en cours...' : 'S\\'inscrire' }}
          </button>
        </form>
        
        <div class="text-center mt-4 auth-footer">
          <p>Déjà un compte ? <a routerLink="/login">Se connecter</a></p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .auth-container {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 2rem 0;
    }
    .auth-card {
      width: 100%;
      max-width: 500px;
    }
    .form-row {
      display: flex;
      gap: 1rem;
    }
    .half { flex: 1; }
    .w-100 { width: 100%; padding: 0.75rem; font-size: 1rem; }
    .auth-footer a { color: var(--primary); text-decoration: none; font-weight: 500; }
    .auth-footer a:hover { text-decoration: underline; }
    .text-danger { color: #dc3545; }
    .small { font-size: 0.875em; margin-top: 0.25rem; }
    .alert { padding: 1rem; margin-bottom: 1rem; border: 1px solid transparent; border-radius: 0.25rem; }
    .alert-danger { color: #842029; background-color: #f8d7da; border-color: #f5c2c7; }
  `]
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';
  isSubmitting: boolean = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      login: ['', [Validators.required, Validators.minLength(4)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      langue: ['fr', Validators.required]
    }, { validators: passwordMatchValidator });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.isSubmitting = true;
      this.errorMessage = '';
      
      this.userService.registerUser(this.registerForm.value).subscribe({
        next: (response) => {
          console.log('Registration successful', response);
          this.isSubmitting = false;
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Registration failed', err);
          this.isSubmitting = false;
          if (err.status === 400 && err.error && typeof err.error === 'object') {
             for (const field in err.error) {
               if (this.registerForm.controls[field]) {
                 this.registerForm.controls[field].setErrors({ backendError: err.error[field] });
               } else if (field === 'userRegistrationDto') {
                 this.errorMessage = err.error[field];
               }
             }
             if (!this.errorMessage && !Object.keys(err.error).length) {
                this.errorMessage = "Données invalides. Veuillez vérifier le formulaire.";
             }
          } else if (err.status === 409) {
             this.errorMessage = "Login ou Email déjà utilisé.";
          } else {
             this.errorMessage = "Une erreur est survenue lors de l'inscription.";
          }
        }
      });
    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}
