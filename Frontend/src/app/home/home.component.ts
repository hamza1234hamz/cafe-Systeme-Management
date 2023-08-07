import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { SingupComponent } from '../singup/singup.component';
import { ForgotPasswordComponent } from '../forgot-password/forgot-password.component';
import { LoginComponent } from '../login/login.component';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';
import { error } from 'console';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private dialog:MatDialog,
    private userService:UserService,
    private router:Router) { }

  ngOnInit(): void {
    this.userService.checkToken().subscribe((response:any)=>{
      this.router.navigate(['/cafe/dashboard']);
    },(error:any)=>{
      console.log(error);
    })
  }

  handleSingupAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width="550px";
    this.dialog.open(SingupComponent,dialogConfig);
  }

  handleForgotPasswordAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width="550px";
    this.dialog.open(ForgotPasswordComponent,dialogConfig);
  }

  
  handleLoginAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width="550px";
    this.dialog.open(LoginComponent,dialogConfig);
  }
}
