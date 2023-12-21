import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BookComponent } from './book/book.component';
import { AddBookComponent } from './book/add-book/add-book.component';
import { ListBooksComponent } from './book/list-books/list-books.component';
import { BookService } from './services/book-service/book.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MdbAccordionModule } from 'mdb-angular-ui-kit/accordion';
import { MdbCarouselModule } from 'mdb-angular-ui-kit/carousel';
import { MdbCheckboxModule } from 'mdb-angular-ui-kit/checkbox';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { MdbDropdownModule } from 'mdb-angular-ui-kit/dropdown';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { MdbModalModule } from 'mdb-angular-ui-kit/modal';
import { MdbPopoverModule } from 'mdb-angular-ui-kit/popover';
import { MdbRadioModule } from 'mdb-angular-ui-kit/radio';
import { MdbRangeModule } from 'mdb-angular-ui-kit/range';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';
import { MdbScrollspyModule } from 'mdb-angular-ui-kit/scrollspy';
import { MdbTabsModule } from 'mdb-angular-ui-kit/tabs';
import { MdbTooltipModule } from 'mdb-angular-ui-kit/tooltip';
import { MdbValidationModule } from 'mdb-angular-ui-kit/validation';
import { NgxStripeModule, StripeService } from 'ngx-stripe';
import { MAT_ERROR, MatError, MatFormField, MatFormFieldModule } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { EditBookComponent } from './book/edit-book/edit-book.component';

import { 
	IgxButtonModule,
	IgxCardModule,
	IgcFormsModule,
	IgxIconModule
 } from "igniteui-angular";
import { CartService } from './services/cart-service/cart.service';
import { CartComponent } from './cart/cart.component';
import { CartItemService } from './services/cart-item-service/cart-item.service';
import { PaymentComponent } from './payment/payment.component';
import { NavigationComponent } from './navigation/navigation.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { FacebookLoginProvider, GoogleLoginProvider, GoogleSigninButtonModule, SocialAuthServiceConfig, SocialLoginModule } from '@abacritt/angularx-social-login';
import { OrderComponent } from './order/order.component';
import { RecommendedComponent } from './recommended/recommended.component';
import { AuthInterceptor } from './http-interceptor/auth-interceptor.interceptor';
import { AdminComponent } from './admin/admin.component';
import { AddAuthorComponent } from './author/add-author/add-author.component';
import { EditAuthorComponent } from './author/edit-author/edit-author.component';
import { AddGenreComponent } from './genre/add-genre/add-genre.component';
import { EditGenreComponent } from './genre/edit-genre/edit-genre.component';
import { EditUserComponent } from './user/edit-user/edit-user.component';
import { PurchaseDialogComponent } from './purchase-dialog/purchase-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    BookComponent,
    AddBookComponent,
    ListBooksComponent,
    EditBookComponent,
    CartComponent,
    PaymentComponent,
    NavigationComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    OrderComponent,
    RecommendedComponent,
    AdminComponent,
    AddAuthorComponent,
    EditAuthorComponent,
    AddGenreComponent,
    EditGenreComponent,
    EditUserComponent,
    PurchaseDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule, 
    BrowserAnimationsModule,
    MatSidenavModule,
    MatTabsModule,
    MatToolbarModule,
    MatButtonModule,
    MatPaginatorModule,
    MatListModule,
    MatCardModule,
    MatSelectModule,
    MatSnackBarModule,
    MdbAccordionModule,
    MdbCarouselModule,
    MdbCheckboxModule,
    MdbCollapseModule,
    MdbDropdownModule,
    MdbFormsModule,
    MdbModalModule,
    MatDialogModule,
    MdbPopoverModule,
    MdbRadioModule,
    MdbRangeModule,
    MdbRippleModule,
    MdbScrollspyModule,
    MdbTabsModule,
    MdbTooltipModule,
    MdbValidationModule,
    MatFormFieldModule,
    MatInputModule,
    IgxButtonModule,
    IgxCardModule,
    IgcFormsModule,
    IgxIconModule,
    SocialLoginModule,
    GoogleSigninButtonModule, 
    NgxStripeModule.forRoot('pk_test_51NiuZRHkw4d7BXiyLnd9YZ7S4lwUVO5D1nvFRjCxTox3bdaxI9XWwhjaLFyuNiujGCHmLqtOqDj0jC00eAWHoSgr009hh28V3A'),
  ],
  providers: [BookService, CartService, CartItemService, StripeService,
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('331216804332-3sgudcmob97149ckghh3dmei6ucvkl2u.apps.googleusercontent.com', {
              scopes: 'profile email https://www.googleapis.com/auth/calendar.readonly',
            }),
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('972437717384615')
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule { }
