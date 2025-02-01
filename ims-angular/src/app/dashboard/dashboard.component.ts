import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    NgxChartsModule,
    FormsModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  transactions:any[]=[];
  transactionTypeData: any[]= [];
  transactionAmountData:any[] =[];
  monthlyTransactionData: any[]=[];

  months=[
    
    {name: 'January',value:'01'},
    {name: 'February',value:'02'},
    {name: 'March',value:'03'},
    {name: 'April',value:'04'},
    {name: 'May',value:'05'},
    {name: 'June',value:'06'},
    {name: 'July',value:'07'},
    {name: 'August',value:'08'},
    {name: 'September',value:'09'},
    {name: 'October',value:'10'},
    {name: 'November',value:'11'},
    {name: 'December',value:'12'},

  ];

  years=Array.from({length:10},(_, i)=>new Date().getFullYear()-i);

  selectedMonth='';
  selectedYear='';

  view:[number,number]=[700,400];
  showLegend= true;
  showLabels= true;
  animations= true;

  constructor(private apiService: ApiService){}

  ngOnInit():void{
    this.loadTransactions();
  }
  loadTransactions():void{
    this.apiService.getAllTransactions("").subscribe((data)=>{
      this.transactions=data.transactions;
      console.log(this.transactions);
      this.processChartData();
    });
  }

  processChartData():void{

    const typeCounts: {[key:string]: number}= {};
    const amountByType: {[key:string]:number}={};

    this.transactions.forEach((transaction)=>{
      const type=transaction.transactionType;
      typeCounts[type]=(typeCounts[type] || 0)+1;
      amountByType[type]=(amountByType[type] || 0)+ transaction.totalPrice;
    });

    this.transactionTypeData=Object.keys(typeCounts).map((type)=> ({
      name:type,
      value: typeCounts[type],
    }));

    this.transactionAmountData = Object.keys(amountByType).map((type)=>({
      name:type,
      value: amountByType[type]
    }))
  }

  loadMonthData():void{

    if(!this.selectedMonth || !this.selectedYear){
      return;
    }

    this.apiService.getTransactionsByMonthAndYear(Number.parseInt(this.selectedMonth),
  Number.parseInt(this.selectedYear)).subscribe((data)=>{
    this.transactions=data.transactions;
    console.log(this.transactions)
    this.processChartData();
    this.processMonthData(data.transactions);
  })
  }

  processMonthData(transactions: any[]):void{

    const dailyTotals: {[key:string]:number}={};

    transactions.forEach((transaction)=>{
      const date = new Date(transaction.createdAt).getDate().toString();
      dailyTotals[date]= (dailyTotals[date] || 0)+ transaction.totalPrice;


    });

    this.monthlyTransactionData=Object.keys(dailyTotals).map((day)=>({
      name: `Day ${day}`,
      value: dailyTotals[day],
    }))
  }

 

}
