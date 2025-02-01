import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-profile',
  imports: [
    CommonModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  constructor(private apiService:ApiService){}

  user:any=null;
  message: string='';

  ngOnInit():void{
    this.fetchUserInfo();
  }

  fetchUserInfo():void{
    this.apiService.getLoggedInUserInfo().subscribe({
      next:(res:any)=>{
        this.user=res;
      },
      error:(error:any)=>{
        this.showMessage(error?.error?.message || error?.message || "Unable to get User Profile"+error);
      }
    })
  }

  showMessage(message:string):void{
    this.message=message;
    setTimeout(()=>{
      this.message=''
    },4000);
  }

}
