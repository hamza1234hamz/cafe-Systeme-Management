import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { UserService } from 'src/app/service/user.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  oldPassword=true;
  newPasswor=true;
  confirmPassword=true;
  changePasswordForm:any=FormGroup;
  responseMessage:any;

  constructor(private formBuilder:FormBuilder,
    private userService:UserService,
    private snackbarService:SnackbarService,
    public dialogRef:MatDialogRef<ChangePasswordComponent>,
    private ngxService:NgxUiLoaderService) { }

  ngOnInit(): void {
    this.changePasswordForm=this.formBuilder.group({
      oldPassword:[null,Validators.required],
      newPasswor:[null,Validators.required],
      confirmPassword:[null,Validators.required]
    })
  }

  validateSubmit(){
    if(this.changePasswordForm.controls['newPasswor'].value != this.changePasswordForm.controls['confirmPassword'].value){
      return true;
    }
    else{
      return false;
    }
  }

  handleChangePasswordSubmit(){
    this.ngxService.start();
    var formData=this.changePasswordForm.getRawValue();
        console.log(formData)
    this.userService.changePassword(formData).subscribe((response:any)=>{
      this.ngxService.stop();
      this.responseMessage=response?.message;
      this.dialogRef.close();
      this.snackbarService.openSnackBar(this.responseMessage,"success");
    },(error)=>{
      console.log(error);
      this.ngxService.stop();
      if(error.error?.message){
        this.responseMessage=error.error?.message;
      }
      else{        
        this.responseMessage=GlobalConstants.genericError;
      }
      this.snackbarService.openSnackBar(this.responseMessage,GlobalConstants.error);
    }
    )
  }

}
