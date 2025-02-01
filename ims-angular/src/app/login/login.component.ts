import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../service/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  
    constructor(private apiService: ApiService,
      private router:Router
    ){}
  
    formData: any= {
      email:'',
      password: ''
    };
    message:string| null= null;
  
    async handleSubmit(){
      if(!this.formData.email || 
        !this.formData.password
      ){
        this.showMessage("Email and Password fields are required");
        return;
      }
      try{
        const response: any=await firstValueFrom(
          this.apiService.loginUser(this.formData)
        );
        if(response.status === 200){
          this.apiService.encryptAndSaveTpStorage('token',response.token);
          this.apiService.encryptAndSaveTpStorage('role',response.role);
          this.router.navigate(["/dashboard"])
        }
      }catch(error:any){
        console.log(error);
        this.showMessage(error?.error?.message || error?.message || "Unable to Login a user" + error)
      }
    }
  
    showMessage(message:string){
      this.message=message;
      setTimeout(()=>{
        this.message=null
      },4000)
    }
}
