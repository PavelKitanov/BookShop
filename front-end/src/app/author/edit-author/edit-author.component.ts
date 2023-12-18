import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthorService } from 'src/app/services/author-service/author.service';

@Component({
  selector: 'app-edit-author',
  templateUrl: './edit-author.component.html',
  styleUrls: ['./edit-author.component.scss']
})
export class EditAuthorComponent implements OnInit {

  updateAuthorForm: FormGroup = new FormGroup({});

  author: any;

  disabled!: boolean;

  constructor(private route: ActivatedRoute,
              private authorService: AuthorService,
              private formBuilder: FormBuilder,
              private router: Router,
              private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.authorService.getAuthorById(id).subscribe( data => {
      console.log(data);
      this.author = data;
    });

    this.disabled = true;

    this.updateAuthorForm = this.formBuilder.group({
      'firstName': new FormControl({value: this.author?.firstName,disabled: this.disabled}), 
      'lastName': new FormControl({value: this.author?.lastName,disabled: this.disabled}),
    });

  }

  edit(){
    this.disabled = !this.disabled;
    if(!this.disabled){
      this.updateAuthorForm.controls['firstName'].enable();
      this.updateAuthorForm.controls['lastName'].enable();
    }
    else{
      this.updateAuthorForm.controls['firstName'].disable();
      this.updateAuthorForm.controls['lastName'].disable();
    }
  }

  updateAuthor(id: any){
    this.authorService.updateAuthor(id,
      this.updateAuthorForm.value.firstName = this.updateAuthorForm.value.firstName || this.author.firstName,
      this.updateAuthorForm.value.lastName = this.updateAuthorForm.value.lastName || this.author.lastName)
    .subscribe(data => {
      this.openSnackBar("Author Edited","");
      this.edit();
      console.log(data);
      this.author = data;
    }, (error: any) => {
      console.log(error);
    });
  }

  openSnackBar(message: string,action: string): MatSnackBarRef<SimpleSnackBar> {
    return this._snackBar.open(message, action, {
      duration: 2000
    });
  }
}
