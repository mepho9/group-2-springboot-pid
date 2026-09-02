import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ArtistListComponent } from './artist-list/artist-list.component';
import { ArtistDetailComponent } from './artist-detail/artist-detail.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ShowListComponent } from './show-list/show-list.component';
import { ShowDetailComponent } from './show-detail/show-detail.component';
import { UserReservationsComponent } from './user-reservations/user-reservations.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'artists', component: ArtistListComponent, canActivate: [adminGuard] },
  { path: 'artists/:id', component: ArtistDetailComponent, canActivate: [adminGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'users', component: UserListComponent, canActivate: [adminGuard] },
  { path: 'profile', component: UserProfileComponent, canActivate: [authGuard] },
  { path: 'reservations', component: UserReservationsComponent, canActivate: [authGuard] },
  { path: 'show', component: ShowListComponent },
  { path: 'shows/:id', component: ShowDetailComponent },
  { path: '**', redirectTo: '' }
];
