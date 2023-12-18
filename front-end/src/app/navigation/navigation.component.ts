import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../services/token-service/token.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {

  tab = 'books';

  constructor(public tokenService: TokenService, 
              private router: Router){}

  logout(): void {
    this.tokenService.clearStorage();
    this.router.navigate(['/login']);
  }
}
