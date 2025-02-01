import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { ApiService } from './service/api.service';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterModule,
    CommonModule,


  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  title = 'ims-angular';

  constructor(private apiService: ApiService,
    private router:Router,
    private cdr: ChangeDetectorRef
  ){}
  
isAuth():boolean{
return this.apiService.isAuthenticated();
}

isAdmin():boolean{
  return this.apiService.isAdmin();
}
logout(): void{
  this.apiService.logout();
  this.router.navigate(["/login"])
  this.cdr.detectChanges();
}

}
