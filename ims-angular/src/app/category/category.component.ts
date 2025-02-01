import { Component, NgModule } from '@angular/core';
import { ApiService } from '../service/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule, NgModel } from '@angular/forms';
interface Category{
  id: string,
  name:string
}

@Component({
  selector: 'app-category',
  imports: [
    CommonModule,
    FormsModule,

  ],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent {

  categories: Category[]=[];
  categoryName:string='';
  message: string| null='';
  isEditing: boolean= false;
  editingCategoryId: string | null=null;

  constructor(private apiService: ApiService){}
  ngOnInit(): void{
    this.getCategories();
  }

  //GET ALL CATEGORIES
  getCategories():void{
    this.apiService.getAllCategory().subscribe({
      next:(res:any)=>{
        if(res.status === 200){
          this.categories=res.categories;
        }
        
      },
      error:(error)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to register a user" + error);
      }
    })
  }

  //ADD A NEW CATEGORY
  addCategory():void{
    if(!this.categoryName){
      this.showMessage("Category name is required");
      return;
    }
    this.apiService.createCategory({name:this.categoryName}).subscribe({
      next:(res:any)=>{
        if(res.status === 200){
        this.showMessage("Category add successfully")
        this.categoryName= ''
        this.getCategories();
        }
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to add category"+ error);    
    }
  })
  }


  //EDIT CATEGORY
  editCategory():void{
    if(!this.editingCategoryId || !this.categoryName){
      return;
    }
    this.apiService.updateCategory(this.editingCategoryId,{name:this.categoryName}).subscribe({
      next:(res:any)=>{
        if(res.status===200){
          this.showMessage("Category updated successfully")
          this.categoryName='';
          this.isEditing=false;
          this.getCategories();
        }
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to edit category"+ error)
      }
    })
  }


  //set the category to edit
  handleEditCategory(category: Category):void{
    this.isEditing=true;
    this.editingCategoryId=category.id;
    this.categoryName=category.name
  }

  //Delete a Category
  handleDeleteCategory(categoryId: string):void{
    if(window.confirm("Are you sure you want to delete this category?")){
      this.apiService.deleteCategory(categoryId).subscribe({
        next: (res:any)=>{
          if(res.status===200){
            this.showMessage("Category deleted successfully")
            this.getCategories();//reload the category
          }
        },
        error:(error:any)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to Delete category"+error)
        }
      })
    }
  }


  showMessage(message:string){
    this.message=message;
    setTimeout(()=>{
     this.message = ''
    },4000)
  }

}
