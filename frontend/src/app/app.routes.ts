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
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'artists', component: ArtistListComponent, canActivate: [authGuard] },
  { path: 'artists/:id', component: ArtistDetailComponent, canActivate: [authGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'users', component: UserListComponent, canActivate: [authGuard] },
  { path: 'profile', component: UserProfileComponent, canActivate: [authGuard] },
  { path: 'show', component: ShowListComponent },
  { path: 'shows/:id', component: ShowDetailComponent },
  { path: '**', redirectTo: '' }
];
