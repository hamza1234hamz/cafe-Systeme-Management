import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../service/user.service';
import { SnackbarService } from '../service/snackbar.service';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordForm!:FormGroup;
  responseMessage:any;
  constructor(private formBuilder:FormBuilder,
    private userService:UserService,
    private snackbarService:SnackbarService,
    public dialogRef:MatDialogRef<ForgotPasswordComponent>,
    private ngxService:NgxUiLoaderService) { }

  ngOnInit(): void {
    this.forgotPasswordForm=this.formBuilder.group({
      email:[null,[Validators.required,Validators.pattern(GlobalConstants.emailRegex)]]
    });
  }

  
  handleSubmit(){
    this.ngxService.start();
    var formData=this.forgotPasswordForm.getRawValue();
        console.log(formData)
    this.userService.forgotPassword(formData).subscribe((response:any)=>{
      this.ngxService.stop();
      this.responseMessage=response?.message;
      this.dialogRef.close();
      this.snackbarService.openSnackBar(this.responseMessage,"");
    },(error)=>{
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
