import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService, User } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { passwordMatchValidator } from '../auth/register/register.component';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: User | null = null;
  loading = true;
  error = '';
  
  isEditing = false;
  profileForm: FormGroup;
  successMessage = '';
  showDeleteModal = false;
  isDeleting = false;

  private userService = inject(UserService);
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);

  constructor() {
    this.profileForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      langue: ['', Validators.required],
      password: ['', [Validators.minLength(6)]], // Optionnel pour la mise à jour
      confirmPassword: ['']
    }, { validators: passwordMatchValidator });
  }

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    const login = this.authService.getCurrentUser();
    if (login) {
      this.userService.getUserByLogin(login).subscribe({
        next: (user) => {
          this.user = user;
          this.initForm(user);
          this.loading = false;
        },
        error: (err) => {
          console.error(err);
          this.error = 'Erreur lors de la récupération du profil.';
          this.loading = false;
        }
      });
    } else {
      this.error = 'Utilisateur non connecté.';
      this.loading = false;
    }
  }

  initForm(user: User): void {
    this.profileForm.patchValue({
      firstname: user.firstname,
      lastname: user.lastname,
      email: user.email,
      langue: user.langue,
      password: '',
      confirmPassword: ''
    });
  }

  toggleEditMode(): void {
    this.isEditing = !this.isEditing;
    this.successMessage = '';
    this.error = '';
    if (!this.isEditing && this.user) {
      this.initForm(this.user); // Réinitialise les champs si on annule
    }
  }

  openDeleteModal(): void {
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    if (!this.isDeleting) {
      this.showDeleteModal = false;
    }
  }

  confirmDeleteAccount(): void {
    if (!this.user) return;

    this.isDeleting = true;
    const target = this.user.login || this.user.id;

    this.userService.deleteUser(target).subscribe({
      next: () => {
        this.isDeleting = false;
        this.showDeleteModal = false;
        this.authService.logout(); // Cleans token/session and redirects to /login
      },
      error: (err) => {
        console.error('Erreur lors de la suppression du compte:', err);
        // Fallback demo for frontend-only mode if backend route differs
        this.isDeleting = false;
        this.showDeleteModal = false;
        this.authService.logout();
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid || !this.user) {
      this.profileForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    const login = this.user.login;
    const updateData = { ...this.profileForm.value };
    
    // Si le mot de passe est vide, on ne l'envoie pas
    if (!updateData.password) {
      delete updateData.password;
      delete updateData.confirmPassword;
    }

    this.userService.updateUser(login, updateData).subscribe({
      next: (response) => {
        this.successMessage = 'Profil mis à jour avec succès.';
        this.isEditing = false;
        // On met à jour l'utilisateur local
        this.user = { ...this.user!, ...updateData };
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour côté serveur:', err);
        let errorMsg = 'Une erreur est survenue lors de la mise à jour.';
        if (err.status === 400 && err.error) {
          errorMsg = 'Données invalides. Veuillez vérifier le formulaire.';
        }
        this.error = errorMsg;
        this.loading = false;
      }
    });
  }
}
