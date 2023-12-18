import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookComponent } from './book/book.component';
import { AddBookComponent } from './book/add-book/add-book.component';
import { EditBookComponent } from './book/edit-book/edit-book.component';
import { CartComponent } from './cart/cart.component';
import { PaymentComponent } from './payment/payment.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuardService } from './services/auth-guard/auth-guard.service';
import { OrderComponent } from './order/order.component';
import { RecommendedComponent } from './recommended/recommended.component';
import { AdminComponent } from './admin/admin.component';
import { AddAuthorComponent } from './author/add-author/add-author.component';
import { EditAuthorComponent } from './author/edit-author/edit-author.component';
import { AddGenreComponent } from './genre/add-genre/add-genre.component';
import { EditGenreComponent } from './genre/edit-genre/edit-genre.component';
import { EditUserComponent } from './user/edit-user/edit-user.component';

const routes: Routes = [
  { path: 'books', component: BookComponent, canActivate: [AuthGuardService] },
  { path: 'addBook', component: AddBookComponent, canActivate: [AuthGuardService] },
  { path: 'editBook/:id', component: EditBookComponent, canActivate: [AuthGuardService] },
  { path: 'addAuthor', component: AddAuthorComponent, canActivate: [AuthGuardService] },
  { path: 'editAuthor/:id', component: EditAuthorComponent, canActivate: [AuthGuardService] },
  { path: 'addGenre', component: AddGenreComponent, canActivate: [AuthGuardService] },
  { path: 'editGenre/:id', component: EditGenreComponent, canActivate: [AuthGuardService] },
  { path: 'editUser/:id', component: EditUserComponent, canActivate: [AuthGuardService] },
  { path: 'cart', component: CartComponent, canActivate: [AuthGuardService] },
  { path: 'payment/:amount/:couponCode', component: PaymentComponent, canActivate: [AuthGuardService] },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuardService] },
  { path: 'orders', component: OrderComponent, canActivate: [AuthGuardService] },
  { path: 'recommended', component: RecommendedComponent, canActivate: [AuthGuardService] },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuardService] },
  { path: 'admin/:tab', component: AdminComponent, canActivate: [AuthGuardService] },
  { path: 'login', component: LoginComponent},
  { path: 'signup', component: RegisterComponent},
  { path: '', redirectTo: '/login', pathMatch: 'full' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
