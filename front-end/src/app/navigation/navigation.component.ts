import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../services/token-service/token.service';
import { SocialAuthService } from '@abacritt/angularx-social-login';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {

  tab = 'books';

  constructor(public tokenService: TokenService,
              private socialAuthService: SocialAuthService,
              private router: Router){}

  async logout(): Promise<void> {
    this.socialAuthService.authState.subscribe(async (user) => {
      if(user != null)
      await this.socialAuthService.signOut();
    });
    this.tokenService.clearStorage();
    this.router.navigateByUrl('/login');
  }
}
