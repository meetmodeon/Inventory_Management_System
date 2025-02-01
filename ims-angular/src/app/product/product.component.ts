import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { PaginationComponent } from '../pagination/pagination.component';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product',
  imports: [
    CommonModule,
    PaginationComponent
  ],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent {

  constructor(private apiService:ApiService,
    private router:Router
  ){}
  products: any[]=[];
  message:string='';
  currentPage: number=1;
  totalPages:number=0;
  itemsPerPage:number = 10;

  ngOnInit():void{
    this.fetchProducts();
  }

  fetchProducts():void{
    this.apiService.getAllProducts().subscribe({
      next:(res:any)=>{
        const products= res.products || [];

        this.totalPages=Math.ceil(products.length/this.itemsPerPage);
        this.products = products.slice(
          (this.currentPage-1)*this.itemsPerPage,
          this.currentPage*this.itemsPerPage
        );
       
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to edit category"+error)
      }
    })
  }


  //Delete A product
  handleProductDelete(productId:string):void{
    if(window.confirm("Are you sure you want to delete this product?")){
      this.apiService.deleteProduct(productId).subscribe({
        next:(res:any)=>{
          if(res.status === 200){
            this.showMessage("Product deleted successfully")
            this.fetchProducts();
          }
        },
        error:(error:any)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to Delete product"+ error)
        }
      })
    }
  }

  onPageChange(page:number):void{
    this.currentPage=page;
    this.fetchProducts();
  }

  //NAVIGATE TO ADD PRODUCT PAGE
  navigateToAddProduct():void{
    this.router.navigate(['/add-product']);
  }

  //NAVIGATE TO EDIT PRODUCT PAGE
  navigateToEditProductPage(productId: string){
    this.router.navigate([`/edit-product/${productId}`]);
  }




  showMessage(message:string){
    this.message=message;
    setTimeout(()=>{
      this.message=""
    }, 4000 )
  }
}
