import { Component } from '@angular/core';
import { ApiService } from '../service/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-purchase',
  imports: [
    CommonModule,
    FormsModule,

  ],
  templateUrl: './purchase.component.html',
  styleUrl: './purchase.component.css'
})
export class PurchaseComponent {

  constructor(private apiService:ApiService,

  ){}

  products:any[]=[]
  suppliers:any[]=[]
  productId: string=''
  supplierId:string=''
  description: string=''
  quantity:string=''
  message:string=''

  ngOnInit():void{
    this.fetchProductsAndSuppliers();

  }

  fetchProductsAndSuppliers():void{
    this.apiService.getAllProducts().subscribe({
      next:(res:any)=>{
        if(res.status === 200){
          this.products=res.products;
        }
      },
      error:(error:any)=>{
        this.showMessage(
          error?.error?.message || error?.message || "Unable to get Products"+ error
        );
      },
    });

    this.apiService.getAllSuppliers().subscribe({
      next:(res:any)=>{
        if(res.status === 200){
          this.suppliers =res.suppliers;
        }
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to get suppliers"+ error)
      }
    })
  }

  //Handle form submission
  handleSubmit():void{
    if(!this.productId || !this.supplierId || !this.quantity){
      this.showMessage("Please fill all fields")
      return;
    }

    const body={
      productId:this.productId,
      supplierId:this.supplierId,
      quantity: parseInt(this.quantity,10),
      description: this.description
    }

    this.apiService.purchaseProduct(body).subscribe({
      next:(res:any)=>{
        if(res.status===200){
          this.showMessage(res.message)
          this.resetForm();
        }
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to get purchase a product"+error)
      },
    })
  }

  resetForm():void{
    this.productId='',
    this.supplierId='',
    this.description='',
    this.quantity='';
  }

  showMessage(message:string):void{
    this.message=message;
    setTimeout(()=>{
      this.message=''
    },4000
    )
  }
}
