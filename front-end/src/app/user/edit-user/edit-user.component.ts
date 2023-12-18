import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { RoleService } from 'src/app/services/role/role.service';
import { UserService } from 'src/app/services/user-service/user.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {
  
  updateUserForm: FormGroup = new FormGroup({});
  user: any;
  roles: any = [];
  disabled!: boolean;
  role: any;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private formBuilder: FormBuilder,
              private roleService: RoleService,
              private router: Router,
              private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.getUserCurrentRole();
    this.disabled = true;
    this.getRoles();
    this.getUserById();

    this.updateUserForm = this.formBuilder.group({
      'username': new FormControl({value: this.user?.username,disabled: this.disabled}), 
      'email': new FormControl({value: this.user?.email,disabled: this.disabled}),
      'role': new FormControl({value: this.user?.role?.name,disabled: this.disabled}),
    });
  }
  
  getRoles(){
    this.roleService.getRoles().subscribe( data => {
      this.roles = data;
      console.log(this.roles);
    });
  }

  getUserById(){
    const id = Number(this.route.snapshot.paramMap.get("id"));
      this.userService.getUserById(id).subscribe( data => {
      console.log(data);
      this.user = data;
      this.role = data.role.roleId;
    });
  }

  async getUserCurrentRole(){
    const id = Number(this.route.snapshot.paramMap.get("id"));
    (await this.userService.getUserRole(id)).subscribe( data => {
      this.role = data;
    });
  }

  edit(){
    this.disabled = !this.disabled;
    if(!this.disabled){
      this.updateUserForm.controls['role'].enable();
    }
    else{
      this.updateUserForm.controls['role'].disable();
    }
  }

  updateUser(userId: any){
    console.log(this.user);
    console.log(this.updateUserForm.value.role);
    this.userService.updateUser(userId,
      this.updateUserForm.value.role = this.updateUserForm.value.role || this.user.role.name)
    .subscribe( data => {
      this.openSnackBar("Role Edited","");
      this.edit();
      console.log(data);
      this.user = data;
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
