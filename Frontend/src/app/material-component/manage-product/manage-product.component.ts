import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ProductService } from 'src/app/service/product.service';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ProductComponent } from '../dialog/product/product.component';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-manage-product',
  templateUrl: './manage-product.component.html',
  styleUrls: ['./manage-product.component.scss']
})
export class ManageProductComponent implements OnInit {

  displayedColumns: string[] =['name','categoryName','description','price','edit'];
  dataSource:any;
  responseMessage:any;
  length1:any;

  constructor(private productService:ProductService,
    private ngxServcie:NgxUiLoaderService,
    private router:Router,
    private dialog:MatDialog,
    private snackbarService:SnackbarService) { }

  ngOnInit(): void {
    this.ngxServcie.start();
    this.tableData();
  }

  tableData(){
    this.productService.getProducts().subscribe((response:any)=>{
      this.ngxServcie.stop();
      this.dataSource = new MatTableDataSource(response);
    },(error:any)=>{
      this.ngxServcie.stop();
      console.log(error.error?.message);
      if(error.error?.message){
        this.responseMessage=error.error?.message;
      }
      else{        
        this.responseMessage=GlobalConstants.genericError;
      }
      this.snackbarService.openSnackBar(this.responseMessage,GlobalConstants.error);
    })
  }

  applyFilter(event:Event){
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter=filterValue.trim().toLowerCase();
  }

  handleAddAction(){
    const dialogConfig=new MatDialogConfig();
    dialogConfig.data={
      action:'Add'
    };
    dialogConfig.width="850px";
    const dialogRef=this.dialog.open(ProductComponent,dialogConfig);
    this.router.events.subscribe(()=>{
      dialogRef.close();
    });
    const sub =dialogRef.componentInstance.OnAddProduct.subscribe((response)=>{
      this.tableData();
    })
  }

  handleEditAction(values:any){
    const dialogConfig=new MatDialogConfig();
    dialogConfig.data={
      action:'Edit',
      data:values
    };
    dialogConfig.width="850px";
    const dialogRef=this.dialog.open(ProductComponent,dialogConfig);
    this.router.events.subscribe(()=>{
      dialogRef.close();
    });
    const sub =dialogRef.componentInstance.onEditProduct.subscribe((response)=>{
      this.tableData();
    })
  }

  handleDeleteAction(values:any){
    const dialogConfig=new MatDialogConfig();
    dialogConfig.data={
      message:'delete '+values.name+' product',
      confirmation:true
    }
    const dialogRef=this.dialog.open(ConfirmationComponent,dialogConfig);
    const sub =dialogRef.componentInstance.onEmitStatusChange.subscribe((response)=>{
      this.ngxServcie.start();
      this.deleteProduct(values.id);
      dialogRef.close();
    })    

  }

  deleteProduct(id:any){
    this.productService.delete(id).subscribe((response:any)=>{
      this.ngxServcie.stop();
      this.tableData();
      this.responseMessage = response?.message;
      this.snackbarService.openSnackBar(this.responseMessage,"success");
    },(error:any)=>{
      this.ngxServcie.stop();
      console.log(error);
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

  onChange(status:any,id:any){
    this.ngxServcie.start();
    var data={
      status:status.toString(),
      id:id
    }
    this.productService.updateStatus(data).subscribe((response:any)=>{
      this.ngxServcie.stop();
      this.responseMessage = response?.message;
      this.snackbarService.openSnackBar(this.responseMessage,"success");
    },(error:any)=>{
      this.ngxServcie.stop();
      console.log(error);
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
