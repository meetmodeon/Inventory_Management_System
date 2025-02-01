import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-sell',
  imports: [
    CommonModule,
    FormsModule,

  ],
  templateUrl: './sell.component.html',
  styleUrl: './sell.component.css'
})
export class SellComponent {
  constructor(private apiService:ApiService){}

  products:any[]=[]
  suppliers: any[]=[]
  productId: string=''
  supplierId:string =''
  description: string=''
  quantity:string=''
  message:string=''

  ngOnInit():void{
    this.fetchProducts();
  }

  fetchProducts():void{
    this.apiService.getAllProducts().subscribe({
      next:(res:any)=>{
        if(res.status === 200){
          this.products=res.products;
        }
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unabel to get products"+ error)
      }
    })
  }

  handleSubmit():void{
    if(!this.productId || !this.quantity){
      this.showMessage("Please fill all fields")
      return;
    }
    const body={
      productId: this.productId,
      quantity: parseInt(this.quantity,10),
      description: this.description
    }

    this.apiService.sellProduct(body).subscribe({
      next:(res:any)=>{
        if(res.status === 200){
          this.showMessage(res.message)
          this.resetForm();
        }
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to sell a product"+ error)
      }
    })

  }

  resetForm():void{
    this.productId='',
    this.quantity='',
    this.description=''
  }

  showMessage(message:string):void{
    this.message=message;
    setTimeout(()=>{
      this.message=''
    },4000)
  }
}
