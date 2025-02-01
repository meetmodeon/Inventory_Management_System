import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from '../service/api.service';


@Component({
  selector: 'app-add-edit-supplier',
  imports: [
    FormsModule,
    CommonModule,
    RouterModule
  ],
  templateUrl: './add-edit-supplier.component.html',
  styleUrl: './add-edit-supplier.component.css'
})
export class AddEditSupplierComponent {

  constructor(private apiService:ApiService,
    private router: Router
  ){}
  message: string= ''
  isEditing:boolean=false;
  supplierId:string| null=null;

  formData:any={
    name:'',
    address:'',
  }

  ngOnInit():void{
    this.supplierId= this.router.url.split('/')[2]; //extraction supplier id from url;
    console.log(this.supplierId)
    if(this.supplierId){
      this.isEditing=true;
      this.fetchSupplier();
    }
  
  }
  fetchSupplier():void{
    this.apiService.getSupplierById(this.supplierId!).subscribe({
      next:(res:any)=>{
        if(res.status === 200){
          this.formData={
            name: res.supplier.name || '',
            address: res.supplier.address || ''
          };
        }
      },
      error:(error)=>{
        this.showMessage(error?.error?.message || error?.message || "Unabel to get supplier by id"+ error);
      }
    })
  }

  //HANDLE FORM SUBMISSION
  handleSubmit(){
    if(!this.formData.name || ! this.formData.address){
    this.showMessage("All fields are required") 
    return;
    }
    
    //prepate data for submission
    const supplierData= {
      name:this.formData.name ,
      address:this.formData.address
    }
    if(this.isEditing){
      this.apiService.updateSupplier(this.supplierId!,supplierData).subscribe({
        next:(res:any)=>{
          if(res.status === 200){
            this.showMessage("Supplier updated successfully");
            this.router.navigate(['/supplier'])
          }
        },
        error:(error:any)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to update supplier"+error)
        }
      })
    }else{
      this.apiService.addSupplier(supplierData).subscribe({
        next:(res:any)=>{
          if(res.status === 200){
            this.showMessage("Supplier added successfully");
            this.router.navigate(['/supplier'])
          }
        },
        error:(error:any)=>{
          this.showMessage(error?.error?.message || error?.message|| "Unable to add supplier"+error)
        }
      })
    }
   
  }

  


  showMessage(message:string):void{
    this.message=message;
    setTimeout(()=>{
      this.message='';
    },4000)
  }
}

