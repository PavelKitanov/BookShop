import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { AuthService } from '../services/auth-service/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { PurchaseDialogComponent } from '../purchase-dialog/purchase-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup = new FormGroup({});
  showPassword = false;

  constructor(private formBuilder: FormBuilder,
              private dialog: MatDialog,
              private authService: AuthService,
              private router: Router){}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      'username': new FormControl('',[Validators.required]),
      'email': new FormControl('',[Validators.required, Validators.email]),
      'password': new FormControl('',[Validators.required]),
    });
  }

register(){
  this.authService.register(this.registerForm.get('username').value,
                            this.registerForm.get('email').value,
                            this.registerForm.get('password').value)
  .subscribe(data => {
    console.log("da");
    const dialogRef = this.dialog.open(PurchaseDialogComponent, {
      data: {
        messageLine1: 'A verification email has been sent to your email address.',
        messageLine2: 'Please check your inbox and click on the verification link to activate your account.',
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      this.router.navigateByUrl('/login');
    });
  });
};

togglePasswordVisibility() {
  this.showPassword = !this.showPassword;
}

getUsernameErrorMessage(){
  return this.registerForm.get('username')?.hasError('required') ? 'You must enter a username' : '';
}

getEmailErrorMessage(){
  return this.registerForm.get('email')?.hasError('required') || this.registerForm.get('email')?.hasError('email') ? 'You must enter a email' : '';

}

getPasswordErrorMessage(){
  return this.registerForm.get('password')?.hasError('required') ? 'You must enter a password' : '';
}

}
