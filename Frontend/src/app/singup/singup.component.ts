import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { SnackbarService } from '../service/snackbar.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-singup',
  templateUrl: './singup.component.html',
  styleUrls: ['./singup.component.scss']
})
export class SingupComponent implements OnInit {
  password=true;
  confirmPassword=true;
  singupFrom!:FormGroup;
  responseMessage:any

  constructor(private formBuilder:FormBuilder,
    private router:Router,
    private userService:UserService,
    private snackbarService:SnackbarService,
    public dialogRef:MatDialogRef<SingupComponent>,
    private ngxService:NgxUiLoaderService
) { }

  ngOnInit(): void {
    this.singupFrom=this.formBuilder.group({
      name:[null,[Validators.required,Validators.pattern(GlobalConstants.nameRegex)]],
      email:[null,[Validators.required,Validators.pattern(GlobalConstants.emailRegex)]],
      contactNumber:[null,[Validators.required,Validators.pattern(GlobalConstants.contactNumberRegex)]],
      password:[null,[Validators.required]],
      confirmPassword:[null,[Validators.required]]
    })

  }

  validateSubmit() {
    if(this.singupFrom.controls['password'].value != this.singupFrom.controls['confirmPassword'].value){
      return true;
    }
    else{
      return false;
    }
  }

  handleSubmit(){
    this.ngxService.start();
    var formData=this.singupFrom.getRawValue();
        console.log(formData)
    this.userService.singup(formData).subscribe((response:any)=>{
      this.ngxService.stop();
      this.dialogRef.close();
      this.responseMessage=response?.message;
      this.snackbarService.openSnackBar(this.responseMessage,"");
      this.router.navigate(['/']);
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
