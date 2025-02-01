import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-transaction-details',
  imports: [
    CommonModule,
    FormsModule,

  ],
  templateUrl: './transaction-details.component.html',
  styleUrl: './transaction-details.component.css'
})
export class TransactionDetailsComponent {

  constructor(private apiService:ApiService,
    private route: ActivatedRoute,
  private router: Router ){}

  transactionId:string | null='';
  transaction:any=null;
  status:string='';
  message:string='';

  ngOnInit():void{
    //extract transaction id from routes
    this.transactionId=this.route.snapshot.paramMap.get('transactionId');
    this.getTransactionDetails();
  }

  getTransactionDetails():void{
    if(this.transactionId){
      this.apiService.getTransactionsById(this.transactionId).subscribe({
        next:(transactionData:any)=>{
          if(transactionData.status === 200){
            this.transaction=transactionData.transaction;
            this.status=this.transaction.status;
            console.log(this.transaction)

          }
        },
        error:(error:any)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to Get Transaction by Id "+error)
        }
      })
    }
  }
  //UPDATE STATUS
  handleUpdateStatus():void{
    if(this.transactionId && this.status){
      this.apiService.updateTransactionStatus(this.transactionId,this.status).subscribe({
        next:(result:any)=>{
          this.router.navigate(['/transaction'])
        },
        error:(error:any)=>{
          this.showMessage(error?.error?.message || error?.message || "Unable to Update a Transaction "+ error)
        }
      })
    }
  }

  showMessage(message:string):void{
    this.message=message;
    setTimeout(()=>{
      this.message=''
    },4000)
  }

}
