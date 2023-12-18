import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthorService } from 'src/app/services/author-service/author.service';

@Component({
  selector: 'app-add-author',
  templateUrl: './add-author.component.html',
  styleUrls: ['./add-author.component.scss']
})
export class AddAuthorComponent implements OnInit {

  addAuthorForm: FormGroup = new FormGroup({});

  constructor(private formBuilder: FormBuilder,
              private authorService: AuthorService,
              private _snackBar: MatSnackBar,
              private router: Router) { }

  ngOnInit(): void {
    this.addAuthorForm = this.formBuilder.group({
      'firstName': new FormControl('',[Validators.required]),
      'lastName': new FormControl('',[Validators.required]),
    });

  }

  getFirstNameErrorMessage(){
    return this.addAuthorForm.get('firstName')?.hasError('required') ? 'You must enter an author name' : '';
  }

  getLastNameErrorMessage(){
    return this.addAuthorForm.get('lastName')?.hasError('required') ? 'You must enter an author lastname' : '';
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }

  addAuthor(){
    this.authorService.addAuthor(this.addAuthorForm.value.firstName,
       this.addAuthorForm.value.lastName)
      .subscribe(
        (response) => {
          console.log('Author added successfully', response);
          this.openSnackBar("Author Added","");
          this.router.navigateByUrl("/admin/authors");
        },
        (error) => {
          console.error('Error adding author', error);
        }
      );
  }
}
